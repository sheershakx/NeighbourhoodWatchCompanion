package com.srg.neighbourhoodwatchcompanion.data.repo.incidents

import com.srg.neighbourhoodwatchcompanion.common.StringResources.INCIDENTS_TABLE
import com.srg.neighbourhoodwatchcompanion.common.StringResources.INCIDENT_IMAGES_BUCKET
import com.srg.neighbourhoodwatchcompanion.common.StringResources.INCIDENT_IMAGES_TABLE
import com.srg.neighbourhoodwatchcompanion.common.StringResources.INCIDENT_LOCATION_INFO_TABLE
import com.srg.neighbourhoodwatchcompanion.common.StringResources.INCIDENT_TYPE_TABLE
import com.srg.neighbourhoodwatchcompanion.data.model.GetDetailedIncident
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentImage
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentInfo
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentLocationInformation
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentType
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.uploadAsFlow
import timber.log.Timber
import javax.inject.Inject


interface IncidentRepo {
    suspend fun getIncidentType(): List<IncidentType>
    suspend fun saveIncidentForm(incidentData: IncidentInfo): String
    suspend fun uploadImage(byteArray: ByteArray, fileName: String?)
    suspend fun insertImagePaths(incidentImage: IncidentImage)
    suspend fun insertLocationInformation(locationInformation: IncidentLocationInformation)
    suspend fun getDetailedIncidents(): List<GetDetailedIncident>
}

class IncidentRepoImpl @Inject constructor(
    val postgrest: Postgrest, val supabaseAuth: Auth, val supabaseStorage: Storage
) : IncidentRepo {
    override suspend fun getIncidentType(): List<IncidentType> {
        return postgrest.from(INCIDENT_TYPE_TABLE).select().decodeList<IncidentType>()
    }

    override suspend fun saveIncidentForm(incidentData: IncidentInfo): String {
        return postgrest.from(INCIDENTS_TABLE).insert(incidentData) {
            select()
        }.decodeSingle<IncidentInfo>().id ?: ""


    }

    override suspend fun uploadImage(byteArray: ByteArray, fileName: String?) {
        val bucket = supabaseStorage.from(INCIDENT_IMAGES_BUCKET)
        bucket.uploadAsFlow(fileName ?: "empty_file_name.jpg", byteArray).collect {
            when (it) {
                is UploadStatus.Progress -> {
                    Timber.tag("UploadImage")
                        .d("Progress: ${it.totalBytesSend.toFloat() / it.contentLength * 100}%")
                }

                is UploadStatus.Success -> Timber.tag("UploadImage").d("Success")
            }
        }
    }

    override suspend fun insertImagePaths(incidentImage: IncidentImage) {
        postgrest.from(INCIDENT_IMAGES_TABLE).insert(incidentImage)
    }

    override suspend fun insertLocationInformation(locationInformation: IncidentLocationInformation) {
        postgrest.from(INCIDENT_LOCATION_INFO_TABLE).insert(locationInformation)

    }

    override suspend fun getDetailedIncidents(): List<GetDetailedIncident> {
        return postgrest.rpc("get_detailed_incidents").decodeList<GetDetailedIncident>()
    }


}