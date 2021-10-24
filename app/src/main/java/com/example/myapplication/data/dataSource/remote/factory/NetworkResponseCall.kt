package com.example.myapplication.data.dataSource.remote.factory

import com.example.myapplication.data.dataSource.remote.models.core.ErrorResponse

import com.example.myapplication.data.dataSource.remote.models.core.NetworkResponse
import com.example.myapplication.domain.entities.exceptions.RemoteResponseEmpty
import com.example.myapplication.domain.entities.exceptions.UnauthorizedExceptions
import com.example.myapplication.data.dataSource.remote.models.core.IResponse
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
///class to wrap our NetworkResponse<IResponse> in a Call
internal class NetworkResponseCall<R : IResponse>(
    private val delegate: Call<R>,
    private val errorConverter: Converter<ResponseBody, ErrorResponse>
) : Call<NetworkResponse<R>> {

    override fun enqueue(callback: Callback<NetworkResponse<R>>) {
        return delegate.enqueue(object : Callback<R> {

            override fun onResponse(call: Call<R>, response: Response<R>) {
                val body = response.body()
                val error = response.errorBody()

                if (response.isSuccessful) {

                    body?.let {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Success(it))
                        )
                    } ?: callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(NetworkResponse.Exception(RemoteResponseEmpty()))
                    )

                } else {

                    if (response.code() == 401) {
                        Response.success(NetworkResponse.Exception(UnauthorizedExceptions()))
                    }

                    val errorBody = when {
                        error == null || error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }

                    errorBody?.let {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(NetworkResponse.Error(response.code(), it))
                        )
                    } ?: callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(NetworkResponse.Exception(RemoteResponseEmpty()))
                    )
                }
            }

            override fun onFailure(call: Call<R>, t: Throwable) {
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(NetworkResponse.Exception(t))
                )
            }

        })
    }


    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun clone() =
        NetworkResponseCall(
            delegate.clone(),
            errorConverter
        )

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<R>> {
        throw UnsupportedOperationException("Network Response Call doesn't support execute.")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

}