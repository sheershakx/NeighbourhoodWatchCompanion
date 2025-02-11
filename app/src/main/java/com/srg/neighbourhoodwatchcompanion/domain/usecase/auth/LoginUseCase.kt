package com.srg.neighbourhoodwatchcompanion.domain.usecase.auth

import com.srg.framework.usecase.NoReturnUseCase
import com.srg.neighbourhoodwatchcompanion.data.model.AuthParams
import com.srg.neighbourhoodwatchcompanion.data.repo.auth.AuthRepo
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepo: AuthRepo
) : NoReturnUseCase<AuthParams>() {
    override suspend fun execute(params: AuthParams) {
        authRepo.loginUser(params.email, params.password)
    }


}




