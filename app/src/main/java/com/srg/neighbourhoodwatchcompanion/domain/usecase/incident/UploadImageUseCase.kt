package com.srg.neighbourhoodwatchcompanion.domain.usecase.incident

import android.net.Uri
import androidx.core.net.toFile
import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.common.ImageCompressor
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentImage
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentInfo
import com.srg.neighbourhoodwatchcompanion.data.repo.incidents.IncidentRepo
import kotlinx.coroutines.flow.FlowCollector
import timber.log.Timber
import java.io.File
import java.net.URI
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val incidentRepo: IncidentRepo,
    private val imageCompressor: ImageCompressor
) : DataStateUseCase<Pair<Uri, String>, Unit>() {

    override suspend fun FlowCollector<DataState<Unit>>.execute(
        params: Pair<Uri, String>
    ) {
        emit(
            apiCall {
                val fileName = imageCompressor.getUriFilenameWithExtension(params.first)
                val compressedImage = imageCompressor.compressImage(
                    params.first,
                    200 * 1024L
                )
                incidentRepo.uploadImage(compressedImage ?: return@apiCall, fileName)
                incidentRepo.insertImagePaths(
                    IncidentImage(
                        incidentId = params.second,
                        path = fileName.toString()
                    )
                )

            }
        )
    }
}