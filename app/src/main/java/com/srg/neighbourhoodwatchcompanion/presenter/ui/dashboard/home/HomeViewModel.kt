package com.srg.neighbourhoodwatchcompanion.presenter.ui.dashboard.home

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.data.dataStore.DataStoreRepo
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident
import com.srg.neighbourhoodwatchcompanion.data.model.LatLngData
import com.srg.neighbourhoodwatchcompanion.domain.usecase.incident.GetDetailedIncidentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dataStoreRepo: DataStoreRepo,
    val getDetailedIncidentsUseCase: GetDetailedIncidentsUseCase,
    @ApplicationContext private val context: Context
) : MviViewModel<BaseViewState<HomeState>, HomeEvent>() {

    val userName =
        dataStoreRepo.firstName.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), false)

    val profileImage =
        dataStoreRepo.profileImage.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3000),
            false
        )

    private var _detailedIncidents = MutableStateFlow<List<GetDetailedIncident>>(emptyList())
    val detailedIncidents: StateFlow<List<GetDetailedIncident>> get() = _detailedIncidents

    private var _currentLocationData = MutableStateFlow<LatLngData>(LatLngData.empty())
    val currentLocationData: StateFlow<LatLngData> get() = _currentLocationData


    private var _neighbourhoodName = MutableStateFlow<String?>("")
    val neighbourhoodName: StateFlow<String?> get() = _neighbourhoodName

    init {
        safeLaunch {
            execute(getDetailedIncidentsUseCase(Unit)) {
                _detailedIncidents.tryEmit(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onTriggerEvent(eventType: HomeEvent) {
        when (eventType) {
            HomeEvent.IncidentFormButtonClicked -> {
                setState(BaseViewState.Data(HomeState(openIncidentForm = true)))
            }

            HomeEvent.GetNeighbourhoodNameEvent -> {
                safeLaunch {
                    getNeighbourhoodName { it ->
                        _neighbourhoodName.tryEmit(it)
                        safeLaunch {
                            dataStoreRepo.saveUserLocationData(
                                currentLocationData.value.latitude,
                                currentLocationData.value.longitude,
                                it.orEmpty()
                            )
                        }

                    }
                }
            }

        }
    }

    fun updateCurrentLocation(latLngData: LatLngData) {
        _currentLocationData.tryEmit(latLngData)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun getNeighbourhoodName(
        refreshedNeighbourhoodName: (String?) -> Unit
    ) {
        // Check cache: if user hasn't moved > 500m, reuse previous neighborhood
        suspend fun getCachedLatitude(): String? = dataStoreRepo.userLatitude.firstOrNull()
        suspend fun getCachedLongitude(): String? = dataStoreRepo.userLongitude.firstOrNull()
        suspend fun getCachedNeighbourhood(): String? = dataStoreRepo.neighbourhood.firstOrNull()

        val cachedLat = getCachedLatitude()
        val cachedLng = getCachedLongitude()
        val cachedNeighbourhood = getCachedNeighbourhood()


        if (!cachedLat.isNullOrEmpty() && !cachedLng.isNullOrEmpty() && !cachedNeighbourhood.isNullOrEmpty()) {
            val distance = FloatArray(1)
            Location.distanceBetween(
                currentLocationData.value.latitude,
                currentLocationData.value.longitude,
                cachedLat.toDouble(),
                cachedLng.toDouble(),
                distance
            )
            if (distance[0] < 500) { // 500 meters threshold
                return refreshedNeighbourhoodName(cachedNeighbourhood)
            }
        }

        //  Try Android Geocoder first (offline, free)
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            // API 33+
            geocoder.getFromLocation(
                currentLocationData.value.latitude,
                currentLocationData.value.longitude,
                1
            ) { addresses ->
                refreshedNeighbourhoodName(addresses.firstOrNull()?.thoroughfare)
            }
            return
        } catch (e: Exception) {
            Timber.e("Failed to get neighborhood: ${e.message}")
            refreshedNeighbourhoodName("An Error Occurred!")
        }
    }
}



