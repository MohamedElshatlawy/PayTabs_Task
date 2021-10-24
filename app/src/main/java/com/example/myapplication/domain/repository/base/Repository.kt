package com.example.myapplication.domain.repository.base


import com.example.myapplication.data.dataSource.remote.models.core.IResponse
import com.example.myapplication.data.dataSource.remote.models.core.NetworkResponse
import com.example.myapplication.domain.entities.common.Status
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

abstract class Repository {
    suspend fun <R: IResponse, T> fetchRemoteData(
        response: suspend () -> NetworkResponse<R>,
        onSuccess: suspend (R) -> T,
        context: CoroutineContext = IO
    ): Flow<Status<T>> = flow {

        val status = response().toStatus { response -> onSuccess(response) }

        emit(status)
    }
        .onStart { emit(Status.Loading) }
        .catch { throwable ->
            if (throwable !is CancellationException)
                emit(Status.Error.Exceptions(throwable))
        }
        .flowOn(context)

}