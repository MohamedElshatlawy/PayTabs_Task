package com.example.myapplication.data.dataSource.remote.models.core

import androidx.annotation.Keep
import com.squareup.moshi.Json
/// customizable based on how server error responses are, this is a simple example!!
@Keep
data class ErrorResponse(
    @Json(name = "status") val status: Any? = null, // false
    @Json(name = "msg") val msg: String? = null // Unauthorized
): IError