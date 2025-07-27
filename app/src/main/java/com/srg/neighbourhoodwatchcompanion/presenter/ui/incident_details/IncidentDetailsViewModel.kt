package com.srg.neighbourhoodwatchcompanion.presenter.ui.incident_details

import com.srg.framework.base.mvi.BaseViewState
import com.srg.framework.base.mvi.MviViewModel
import com.srg.neighbourhoodwatchcompanion.domain.usecase.incident.GetIncidentSignedUrlsByIncidentIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IncidentDetailsViewModel @Inject constructor(
    private val getIncidentSignedUrlsByIncidentIdUseCase: GetIncidentSignedUrlsByIncidentIdUseCase
) :
    MviViewModel<BaseViewState<IncidentDetailData>, IncidentDetailEvent>() {
    override fun onTriggerEvent(eventType: IncidentDetailEvent) {
        TODO("Not yet implemented")
    }

    private val _imageUrls = MutableStateFlow<List<String>>(emptyList())
    val imageUrls: StateFlow<List<String>> get() = _imageUrls


    fun getSignedUrls(incidentId: String) {
        safeLaunch {
            execute(getIncidentSignedUrlsByIncidentIdUseCase(incidentId)) { urls ->
                Timber.tag("Images").d("$urls")
                _imageUrls.tryEmit(urls)
            }
        }
    }
}