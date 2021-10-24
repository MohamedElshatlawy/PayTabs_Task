package com.example.myapplication.data.dataSource.remote.factory

import com.example.myapplication.data.dataSource.remote.models.core.ErrorResponse
import com.example.myapplication.data.dataSource.remote.models.core.IResponse
import com.example.myapplication.data.dataSource.remote.models.core.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
///this custom CallAdapter.Factory Class is made specific to work with our NetworkResponse Class
class NetworkResponseFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)

        // if the response type is not NetworkResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != NetworkResponse::class.java) {
            return null
        }


        // the response type is NetworkResponse and should be parameterized
        check(responseType is ParameterizedType) {
            "Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>"
        }

        val successBodyType = getParameterUpperBound(0, responseType)

        val errorBodyConverter = retrofit
            .nextResponseBodyConverter<ErrorResponse>(
                null,
                ErrorResponse::class.javaObjectType,
                annotations
            )

        return NetworkResponseAdapter<IResponse>(
            successBodyType,
            errorBodyConverter
        )

    }

}