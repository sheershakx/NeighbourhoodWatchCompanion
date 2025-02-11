package com.srg.neighbourhoodwatchcompanion.domain.usecase.home

import com.srg.framework.network.DataState
import com.srg.framework.network.apiCall
import com.srg.framework.usecase.DataStateUseCase
import com.srg.neighbourhoodwatchcompanion.data.model.UserInfo
import com.srg.neighbourhoodwatchcompanion.data.repo.user.UserRepo
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepo: UserRepo
) : DataStateUseCase<Unit, UserInfo>() {
    override suspend fun FlowCollector<DataState<UserInfo>>.execute(
        params: Unit
    ) {
        emit(
            apiCall {
                userRepo.getUserInfo()
            }
        )
    }

}

