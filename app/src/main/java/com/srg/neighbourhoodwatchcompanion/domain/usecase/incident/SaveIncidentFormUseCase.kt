package com.srg.neighbourhoodwatchcompanion.domain.usecase.incident

import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentInfo
import com.srg.neighbourhoodwatchcompanion.data.repo.incidents.IncidentRepo
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class SaveIncidentFormUseCase @Inject constructor(
    private val incidentRepo: IncidentRepo
) : DataStateUseCase<IncidentInfo, String>() {
    override suspend fun FlowCollector<DataState<String>>.execute(
        params: IncidentInfo
    ) {
        emit(
            apiCall {
                incidentRepo.saveIncidentForm(params)
            }
        )
    }
}