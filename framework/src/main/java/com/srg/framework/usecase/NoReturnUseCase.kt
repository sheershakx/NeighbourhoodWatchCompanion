package com.srg.framework.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class NoReturnUseCase<in Params>  {

    protected abstract suspend fun execute(params: Params)

    suspend operator fun invoke(params: Params) = execute(params)

}