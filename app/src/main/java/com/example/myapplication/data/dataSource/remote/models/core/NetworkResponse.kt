package com.example.myapplication.data.dataSource.remote.models.core



import com.example.myapplication.domain.entities.common.Status
import com.example.myapplication.domain.entities.exceptions.UnauthorizedExceptions
/// a representation of the different network call responses that we need tto handle
sealed class NetworkResponse<out T : IResponse> {
    // a success response
    data class Success<out T : IResponse>(val response: T) : NetworkResponse<T>()
    // an error response
    data class Error(val code: Int, val error: ErrorResponse) : NetworkResponse<Nothing>() {
        fun isExpiredToken(): Boolean {
            return code == 401
        }
    }
    /// an exception  response that happened for some reason
    data class Exception(val ex: Throwable) : NetworkResponse<Nothing>()

    suspend fun <R> toStatus(
        onSuccess: suspend (response: T) -> R
    ): Status<R> {
        return when (this) {
            is Success -> Status.Success(onSuccess(this.response))
            is Error -> {
                if (this.isExpiredToken()) {
                    Status.Error.Exceptions(UnauthorizedExceptions())
                } else
                    Status.Error.Message(this.error.msg)
            }
            is Exception -> Status.Error.Exceptions(this.ex)
        }
    }

//    suspend fun <Key : Any, Value : Any> toLoadState(
//        onSuccess: suspend (T) -> List<Value>,
//        nextKey: (T) -> Key?
//    ): PagingSource.LoadResult<Key, Value> {
//
//        return when (this) {
//            is Success -> PagingSource.LoadResult.Page(
//                data = onSuccess(this.response),
//                nextKey = nextKey(this.response),
//                prevKey = null
//            )
//            is Exception -> PagingSource.LoadResult.Error(throwable = this.ex)
//            is Error -> PagingSource.LoadResult.Error(throwable = MessageExceptions(this.error.msg))
//        }
//    }


}