package com.srg.neighbourhoodwatchcompanion.data.repo.incidents

import com.srg.neighbourhoodwatchcompanion.data.model.IncidentInfo
import com.srg.neighbourhoodwatchcompanion.data.model.IncidentType
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject


interface IncidentRepo {
    suspend fun getIncidentType(): List<IncidentType>
    suspend fun saveIncidentForm(incidentData: IncidentInfo)
}

class IncidentRepoImpl @Inject constructor(
    val postgrest: Postgrest,
    val supabaseAuth: Auth
) : IncidentRepo {
    override suspend fun getIncidentType(): List<IncidentType> {
        return postgrest.from("incident_type").select().decodeList<IncidentType>()
    }

    override suspend fun saveIncidentForm(incidentData: IncidentInfo) {
        postgrest.from("incidents").insert(incidentData)

    }


}