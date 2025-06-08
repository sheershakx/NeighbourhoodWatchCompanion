package com.srg.neighbourhoodwatchcompanion.data.repo.user

import com.srg.neighbourhoodwatchcompanion.common.StringResources.USER_INFO_TABLE
import com.srg.neighbourhoodwatchcompanion.common.StringResources.USER_PROFILE_IMAGES_BUCKET
import com.srg.neighbourhoodwatchcompanion.data.model.UserInfo
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.uploadAsFlow
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

interface UserRepo {
    suspend fun getUserInfo(): UserInfo
    suspend fun updateUserInfo(userInfo: UserInfo)
    suspend fun uploadProfileImage(byteArray: ByteArray, fileName: String?)
    suspend fun getProfileImageSignedUrl(path: String): String
}

class UserRepoImpl @Inject constructor(
    val postgrest: Postgrest,
    val supabaseAuth: Auth,
    val supabaseStorage: Storage
) : UserRepo {
    override suspend fun getUserInfo(): UserInfo {
        return (postgrest.from("user_info").select {
            filter {
                UserInfo::userId eq supabaseAuth.currentUserOrNull()?.id
            }
        }.decodeSingle<UserInfo>())
    }

    override suspend fun updateUserInfo(userInfo: UserInfo) {
        postgrest.from("user_info").update(
            {
                UserInfo::firstName setTo userInfo.firstName
                UserInfo::lastName setTo userInfo.lastName
                UserInfo::mobile setTo userInfo.mobile
            }
        ) {
            filter {
                UserInfo::userId eq supabaseAuth.currentUserOrNull()?.id
            }
        }
    }

    override suspend fun uploadProfileImage(byteArray: ByteArray, fileName: String?) {
        val bucket = supabaseStorage.from(USER_PROFILE_IMAGES_BUCKET)
        bucket.uploadAsFlow(fileName ?: "empty_file_name.jpg", byteArray).collect {
            when (it) {
                is UploadStatus.Progress -> {
                    Timber.tag("UploadImage")
                        .d("Progress: ${it.totalBytesSend.toFloat() / it.contentLength * 100}%")
                }

                is UploadStatus.Success -> {
                    postgrest.from(USER_INFO_TABLE).update(
                        {
                            UserInfo::imagePath setTo fileName
                        }
                    ) {
                        filter {
                            UserInfo::userId eq supabaseAuth.currentUserOrNull()?.id
                        }
                    }
                }
            }
        }
    }

    override suspend fun getProfileImageSignedUrl(path: String): String {
        val bucket = supabaseStorage.from(USER_PROFILE_IMAGES_BUCKET)
        return bucket.createSignedUrl(path = path, expiresIn = 10.minutes)
    }

}