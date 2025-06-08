package com.srg.neighbourhoodwatchcompanion.domain.usecase.home

import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.data.dataStore.DataStoreRepo
import com.srg.neighbourhoodwatchcompanion.data.model.UserInfo
import com.srg.neighbourhoodwatchcompanion.data.repo.user.UserRepo
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepo: UserRepo,
    private val dataStoreRepo: DataStoreRepo,
) : DataStateUseCase<Unit, UserInfo>() {
    override suspend fun FlowCollector<DataState<UserInfo>>.execute(
        params: Unit
    ) {
        val userInfoResponse = apiCall {
            userRepo.getUserInfo()
        }
        if (userInfoResponse is DataState.Success<UserInfo>) {
            val result = userInfoResponse.result
            val signedUrl = userRepo.getProfileImageSignedUrl(result.imagePath.toString())
            dataStoreRepo.saveUserData(result)
            dataStoreRepo.saveProfileImage(signedUrl)
        }
        emit(userInfoResponse)

    }

}

