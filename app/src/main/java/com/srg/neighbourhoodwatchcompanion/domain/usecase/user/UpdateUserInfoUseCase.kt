package com.srg.neighbourhoodwatchcompanion.domain.usecase.user

import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.data.dataStore.DataStoreRepo
import com.srg.neighbourhoodwatchcompanion.data.model.UserInfo
import com.srg.neighbourhoodwatchcompanion.data.repo.user.UserRepo
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject


class UpdateUserInfoUseCase @Inject constructor(
    private val userRepo: UserRepo,
    private val dataStoreRepo: DataStoreRepo
) :
    DataStateUseCase<UserInfo, Unit>() {
    override suspend fun FlowCollector<DataState<Unit>>.execute(
        params: UserInfo
    ) {

        emit(
            apiCall {
                userRepo.updateUserInfo(params).also {
                    dataStoreRepo.saveUserData(params)
                }
            }
        )


    }

}