package com.srg.neighbourhoodwatchcompanion.domain.usecase.auth

import com.srg.framework.usecase.NoReturnUseCase
import com.srg.neighbourhoodwatchcompanion.data.dataStore.DataStoreRepo
import com.srg.neighbourhoodwatchcompanion.data.repo.auth.AuthRepo
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepo: AuthRepo,
    private val dataStoreRepo: DataStoreRepo,
) : NoReturnUseCase<Unit>() {
    override suspend fun execute(params: Unit) {
        //perform remote signOut of user
        authRepo.signOutUser()

        //clear dataStore data
        dataStoreRepo.clearAllData()

        //trigger navigation

    }


}




