package com.srg.neighbourhoodwatchcompanion.domain.usecase.incident

import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentInfo
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentLocationInformation
import com.srg.neighbourhoodwatchcompanion.data.repo.incidents.IncidentRepo
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class SaveIncidentFormUseCase @Inject constructor(
    private val incidentRepo: IncidentRepo
) : DataStateUseCase<Pair<IncidentInfo, IncidentLocationInformation>, String>() {

    override suspend fun FlowCollector<DataState<String>>.execute(
        params: Pair<IncidentInfo, IncidentLocationInformation>
    ) {
        val incidentInfo = params.first
        val incidentId = apiCall { incidentRepo.saveIncidentForm(incidentInfo) }
        val incidentLocationInformation =
            params.second.copy(incidentId = (incidentId as DataState.Success).result)
        apiCall { incidentRepo.insertLocationInformation(incidentLocationInformation) }

        emit(incidentId)


    }
}