package com.example.myapplication.data.repositoryImp

import com.example.myapplication.data.dataSource.remote.models.TestResponseItem
import com.example.myapplication.data.dataSource.remote.services.TestService
import com.example.myapplication.domain.entities.common.Status
import com.example.myapplication.domain.repository.TestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class TestRepositoryImp constructor (private val remoteDataService: TestService) : TestRepository() {
    override suspend fun getRepos(): Flow<Status<List<TestResponseItem>>> {
        return fetchRemoteData(
            response = {
                remoteDataService.getRepos()
            },
            onSuccess = {
                it
            }
        )
    }
}