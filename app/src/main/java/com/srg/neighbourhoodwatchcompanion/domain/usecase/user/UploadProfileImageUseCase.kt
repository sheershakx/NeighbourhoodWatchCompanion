package com.srg.neighbourhoodwatchcompanion.domain.usecase.user

import android.net.Uri
import androidx.core.net.toFile
import androidx.datastore.dataStore
import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.common.ImageCompressor
import com.srg.neighbourhoodwatchcompanion.data.dataStore.DataStoreRepo
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentImage
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentInfo
import com.srg.neighbourhoodwatchcompanion.data.repo.incidents.IncidentRepo
import com.srg.neighbourhoodwatchcompanion.data.repo.user.UserRepo
import kotlinx.coroutines.flow.FlowCollector
import timber.log.Timber
import java.io.File
import java.net.URI
import javax.inject.Inject

class UploadProfileImageUseCase @Inject constructor(
    private val userRepo: UserRepo,
    private val imageCompressor: ImageCompressor,
    private val dataStoreRepo: DataStoreRepo
) : DataStateUseCase<Uri, Unit>() {

    override suspend fun FlowCollector<DataState<Unit>>.execute(
        params: Uri
    ) {
        val fileName = imageCompressor.getUriFilenameWithExtension(params)
        fileName?.let {
            emit(
                apiCall {
                    val compressedImage = imageCompressor.compressImage(
                        params,
                        200 * 1024L
                    )
                    userRepo.uploadProfileImage(compressedImage ?: return@apiCall, fileName)
                    val signedUrl = userRepo.getProfileImageSignedUrl(fileName)
                    Timber.d("SignedUrl= $signedUrl")
                    dataStoreRepo.saveProfileImage(signedUrl)

                }
            )
        }
    }
}