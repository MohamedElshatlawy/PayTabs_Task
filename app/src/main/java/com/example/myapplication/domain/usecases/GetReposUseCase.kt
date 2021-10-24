package com.example.myapplication.domain.usecases

import com.example.myapplication.data.dataSource.remote.factory.NetworkResponseFactory
import com.example.myapplication.data.dataSource.remote.models.core.IRequest
import com.example.myapplication.data.dataSource.remote.models.TestResponseItem
import com.example.myapplication.data.dataSource.remote.services.TestService
import com.example.myapplication.data.repositoryImp.TestRepositoryImp
import com.example.myapplication.domain.entities.common.Status
import com.example.myapplication.domain.usecases.core.UseCase
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GetReposUseCase : UseCase<IRequest, List<TestResponseItem>>()  {
    override suspend fun run(param: IRequest): Flow<Status<List<TestResponseItem>>> {
        val service=Retrofit.Builder()
            .baseUrl("https://api.github.com/orgs/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(NetworkResponseFactory())
            .build()
            .create(TestService::class.java)
        val repo=TestRepositoryImp(remoteDataService = service)
        return repo.getRepos()
    }
}
