package com.example.myapplication.data.dataSource.remote.factory



import com.example.myapplication.data.dataSource.remote.models.core.ErrorResponse
import com.example.myapplication.data.dataSource.remote.models.core.IResponse
import com.example.myapplication.data.dataSource.remote.models.core.NetworkResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

internal class NetworkResponseAdapter<R: IResponse>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, ErrorResponse>
) : CallAdapter<R, Call<NetworkResponse<R>>> {

    override fun adapt(call: Call<R>): Call<NetworkResponse<R>> {
        return NetworkResponseCall(
            call,
            errorBodyConverter
        )
    }

    override fun responseType(): Type = successType
}