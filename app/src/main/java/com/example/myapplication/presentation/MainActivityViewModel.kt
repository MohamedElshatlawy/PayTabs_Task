package com.example.myapplication.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.entities.common.None
import com.example.myapplication.domain.usecases.GetReposUseCase
import kotlinx.coroutines.launch

class MainActivityViewModel:ViewModel() {
    private val useCase=GetReposUseCase()
    val statusRepos = useCase.data
    private fun fetchRepos() {
    viewModelScope.launch {
            useCase(param=None())
        }
    }
    init {
        fetchRepos()
    }
}