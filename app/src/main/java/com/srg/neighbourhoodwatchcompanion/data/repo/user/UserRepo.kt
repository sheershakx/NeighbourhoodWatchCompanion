package com.srg.neighbourhoodwatchcompanion.data.repo.user

import com.srg.neighbourhoodwatchcompanion.data.model.UserInfo
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

interface UserRepo {
    suspend fun getUserInfo(): UserInfo
}

class UserRepoImpl @Inject constructor(
    val postgrest: Postgrest,
    val supabaseAuth: Auth
) : UserRepo {
    override suspend fun getUserInfo(): UserInfo {
        return (postgrest.from("user_info").select {
            filter {
                UserInfo::userId eq supabaseAuth.currentUserOrNull()?.id
            }
        }.decodeSingle<UserInfo>())
    }


}