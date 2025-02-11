package com.srg.framework.usecase

import com.srg.framework.network.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class DataStateUseCase<in Params, ReturnType> where ReturnType : Any {

    protected abstract suspend fun FlowCollector<DataState<ReturnType>>.execute(params: Params)

    operator fun invoke(params: Params) = flow {
        execute(params)
    }.flowOn(Dispatchers.IO)
}
