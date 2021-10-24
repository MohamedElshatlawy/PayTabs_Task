package com.example.myapplication.domain.repository

import com.example.myapplication.data.dataSource.remote.models.TestResponseItem
import com.example.myapplication.domain.entities.common.Status
import com.example.myapplication.domain.repository.base.Repository
import kotlinx.coroutines.flow.Flow

abstract class TestRepository : Repository() {
    abstract suspend fun getRepos(): Flow<Status<List<TestResponseItem>>>

}