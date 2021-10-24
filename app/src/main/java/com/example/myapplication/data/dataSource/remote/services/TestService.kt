package com.example.myapplication.data.dataSource.remote.services

import com.example.myapplication.data.dataSource.remote.models.core.NetworkResponse
import com.example.myapplication.data.dataSource.remote.models.TestResponse
import retrofit2.http.GET

interface TestService {

    @GET("octokit/repos")
    suspend fun getRepos(): NetworkResponse<TestResponse>
}