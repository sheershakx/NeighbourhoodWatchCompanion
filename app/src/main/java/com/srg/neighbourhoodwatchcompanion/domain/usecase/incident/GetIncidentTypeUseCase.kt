package com.srg.neighbourhoodwatchcompanion.domain.usecase.incident

import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentType
import com.srg.neighbourhoodwatchcompanion.data.repo.incidents.IncidentRepo
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class GetIncidentTypeUseCase @Inject constructor(
    private val incidentRepo: IncidentRepo
) : DataStateUseCase<Unit, List<IncidentType>>() {
    override suspend fun FlowCollector<DataState<List<IncidentType>>>.execute(
        params: Unit
    ) {
        emit(
            apiCall {
                incidentRepo.getIncidentType()
            }
        )
    }
}