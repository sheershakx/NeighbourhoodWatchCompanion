package com.srg.neighbourhoodwatchcompanion.domain.usecase.incident

import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident
import com.srg.neighbourhoodwatchcompanion.data.repo.incidents.IncidentRepo
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject


class GetDetailedIncidentsUseCase @Inject constructor(
    private val incidentRepo: IncidentRepo
) : DataStateUseCase<Unit, List<GetDetailedIncident>>() {
    override suspend fun FlowCollector<DataState<List<GetDetailedIncident>>>.execute(
        params: Unit
    ) {
        emit(
            apiCall {
                incidentRepo.getDetailedIncidents()
            }
        )
    }

}