package com.srg.neighbourhoodwatchcompanion.domain.usecase.incident

import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.data.repo.incidents.IncidentRepo
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject


class GetIncidentSignedUrlsByIncidentIdUseCase @Inject constructor(
    private val incidentRepo: IncidentRepo
) : DataStateUseCase<String, List<String>>() {
    override suspend fun FlowCollector<DataState<List<String>>>.execute(
        params: String
    ) {
        emit(
            apiCall {
                incidentRepo.getIncidentImagesSignedUrlsByIncidentId(params)
            }
        )
    }

}