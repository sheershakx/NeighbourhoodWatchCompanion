package com.srg.neighbourhoodwatchcompanion.presenter.ui.incident

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitFetchPlace
import com.google.android.libraries.places.api.net.kotlin.awaitFindAutocompletePredictions
import com.google.android.libraries.places.compose.autocomplete.models.AutocompletePlace
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.BaseViewState.*
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentInfo
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentLocationInformation
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentType
import com.srg.neighbourhoodwatchcompanion.domain.usecase.incident.GetIncidentTypeUseCase
import com.srg.neighbourhoodwatchcompanion.domain.usecase.incident.SaveIncidentFormUseCase
import com.srg.neighbourhoodwatchcompanion.domain.usecase.incident.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import timber.log.Timber
import javax.inject.Inject
import kotlin.text.isEmpty
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@HiltViewModel
class IncidentFormViewModel @Inject constructor(
    private val getIncidentTypeUseCase: GetIncidentTypeUseCase,
    private val saveIncidentFormUseCase: SaveIncidentFormUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val placesClient: PlacesClient

) : MviViewModel<BaseViewState<IncidentFormState>, IncidentFormEvent>() {

    private val _incidentTypeOptions = MutableStateFlow<List<IncidentType>>(emptyList())
    val incidentTypeOptions: StateFlow<List<IncidentType>> get() = _incidentTypeOptions


    private val _selectedIncidentType = MutableStateFlow<Pair<IncidentType, String>>(
        Pair(
            IncidentType.empty(), ""
        )
    )
    val selectedIncidentType: StateFlow<Pair<IncidentType, String>> get() = _selectedIncidentType

    private val _title = MutableStateFlow<Pair<String, String>>(Pair("", ""))
    val title: StateFlow<Pair<String, String>> get() = _title

    private val _description = MutableStateFlow<Pair<String, String>>(Pair("", ""))
    val description: StateFlow<Pair<String, String>> get() = _description

    private val _date = MutableStateFlow<Pair<String, String>>(Pair("", ""))
    val date: StateFlow<Pair<String, String>> get() = _date

    private val _time = MutableStateFlow<Pair<String, String>>(Pair("", ""))
    val time: StateFlow<Pair<String, String>> get() = _time

    private val _locationSearchText = MutableStateFlow<Pair<String, String>>(Pair("", ""))
    val locationSearchText: StateFlow<Pair<String, String>> get() = _locationSearchText

    private val _casualties = MutableStateFlow<Pair<String, String>>(Pair("", ""))
    val casualties: StateFlow<Pair<String, String>> get() = _casualties

    private val _dateTimeCombined = MutableStateFlow<Instant>(Clock.System.now())
    val dateTimeCombined: StateFlow<Instant> get() = _dateTimeCombined

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> get() = _imageUris

    private val _predictions = MutableStateFlow(emptyList<AutocompletePrediction>())
    val predictions: StateFlow<List<AutocompletePrediction>> get() = _predictions

    private val _previewSelectedLocation = mutableStateOf<AutocompletePlace?>(null)
    val previewSelectedLocation: MutableState<AutocompletePlace?> = _previewSelectedLocation

    private val _incidentLocationInformation = MutableStateFlow(IncidentLocationInformation.empty())
    val incidentLocationInformation: StateFlow<IncidentLocationInformation> get() = _incidentLocationInformation

    init {
        safeLaunch {
            execute(getIncidentTypeUseCase(Unit), false) { data ->
                _incidentTypeOptions.value = data
            }
            _locationSearchText.debounce(500.milliseconds).collect { query: Pair<String, String> ->
                if (_previewSelectedLocation.value == null) {
                    val response = placesClient.awaitFindAutocompletePredictions {
//                    typesFilter = listOf(PlaceTypes.INTERSECTION)
                        countries = listOf("CA","US","NP","IN","AU")
                        this.query = query.first
                    }
                    _predictions.value = response.autocompletePredictions
                }
            }


        }
    }

    override fun onTriggerEvent(eventType: IncidentFormEvent) {
        when (eventType) {
            is IncidentFormEvent.SubmitIncident -> {
                if (validateInputFields()) {
                    val incidentModel = IncidentInfo(
                        type = selectedIncidentType.value.first.id,
                        title = title.value.first,
                        description = description.value.first,
                        date = "${date.value.first} ${time.value.first}:00",
                        casualties = casualties.value.first,
                    )
                    safeLaunch {
                        execute(
                            saveIncidentFormUseCase(
                                Pair(
                                    incidentModel,
                                    incidentLocationInformation.value
                                )
                            )
                        ) { incidentId ->
                            if (imageUris.value.isEmpty()) {
                                setState(Data(IncidentFormState(incidentFormSavedSuccessful = true)))
                                return@execute
                            }
                            async(context = this.coroutineContext) {
                                uploadImages(incidentId)
                            }

                        }
                    }
                }

            }


            is IncidentFormEvent.AddImageToPreview -> {
                updateImageUris(eventType.uri)
            }

            is IncidentFormEvent.RemoveImageFromPreview -> {
                _imageUris.value = _imageUris.value.minus(eventType.uri)
            }

            IncidentFormEvent.LocationInformationSave -> {
                previewSelectedLocation.value?.let {
                    _incidentLocationInformation.value = IncidentLocationInformation(
                        placeId = it.placeId,
                        lat = it.latLng?.latitude ?: 0.0,
                        lng = it.latLng?.longitude ?: 0.0,
                        primaryText = it.primaryText.toString(),
                        secondaryText = it.secondaryText.toString()

                    )
                }


            }
        }
    }

    private suspend fun uploadImages(incidentId: String) {
        val uploadJobs = imageUris.value.map { uri ->
            viewModelScope.async {
                Timber.d("Uploading image $incidentId")
                execute(uploadImageUseCase(Pair(uri, incidentId)))
            }
        }
        uploadJobs.awaitAll().run {
            setState(Data(IncidentFormState(imageUploadSuccessful = true)))
            setState(
                Data(
                    IncidentFormState(
                        incidentFormSavedSuccessful = true
                    )
                )
            )

        }


    }

    private fun validateInputFields(): Boolean {
        var validationStatus = true

        fun validateField(
            value: Pair<String, String>,
            emitter: (Pair<String, String>) -> Unit,
            errorMessage: String
        ) {
            if (value.first.trim().isEmpty()) {
                validationStatus = false
                emitter(Pair(value.first, errorMessage))
            }
        }

        validateField(title.value, _title::tryEmit, "Title is required")
        validateField(description.value, _description::tryEmit, "Description is required")
        validateField(date.value, _date::tryEmit, "Date is required")
        validateField(time.value, _time::tryEmit, "Time is required")
        validateField(
            locationSearchText.value,
            _locationSearchText::tryEmit,
            "Location is required"
        )

        if (!incidentTypeOptions.value.contains(selectedIncidentType.value.first)) {
            validationStatus = false
            _selectedIncidentType.tryEmit(
                Pair(
                    selectedIncidentType.value.first, "Incident type is required"
                )
            )
        }

        return validationStatus
    }


    fun onTitleUpdated(title: String) {
        _title.tryEmit(Pair(title, ""))
    }

    fun onIncidentTypeSelected(incidentType: IncidentType) {
        _selectedIncidentType.tryEmit(Pair(incidentType, ""))
    }

    fun onDescriptionUpdated(description: String) {
        _description.tryEmit(Pair(description, ""))
    }

    fun onDateUpdated(date: String) {
        _date.tryEmit(Pair(date, ""))
    }

    fun onTimeUpdated(time: String) {
        _time.tryEmit(Pair(time, ""))
    }

    fun onCasualtiesUpdated(casualties: String) {
        _casualties.tryEmit(Pair(casualties, ""))
    }

    fun updateImageUris(uri: Uri) {
        _imageUris.value = _imageUris.value + uri
    }

    fun onLocationSearchUpdated(location: String) {
        _previewSelectedLocation.value = null
        _locationSearchText.tryEmit(Pair(location, ""))
    }

    fun onLocationSelected(selectedLocation: AutocompletePlace) {
        safeLaunch {
            val latLng = getPlaceCoordinatesById(selectedLocation.placeId)
            _previewSelectedLocation.value = selectedLocation.copy(latLng = latLng)
            _locationSearchText.value = Pair(selectedLocation.primaryText.toString(), "")
            _predictions.value = emptyList()
        }
    }


    suspend fun getPlaceCoordinatesById(placeId: String): LatLng? {
        return placesClient.awaitFetchPlace(
            placeId, listOf(Place.Field.LAT_LNG)
        ).place.location

    }
}
