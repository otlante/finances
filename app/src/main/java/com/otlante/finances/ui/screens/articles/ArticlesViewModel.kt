package com.otlante.finances.ui.screens.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.otlante.finances.network.NetworkError
import com.otlante.finances.network.ResultState
import com.otlante.finances.domain.entity.Category
import com.otlante.finances.domain.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ArticlesUiState(
    val searchQuery: String = "",
    val allCategories: List<Category> = emptyList(),
    val filteredCategories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: NetworkError? = null
)

class ArticlesViewModel(
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticlesUiState())
    val uiState: StateFlow<ArticlesUiState> = _uiState

    init {
        fetchArticles(initial = true)
    }

    fun fetchArticles(initial: Boolean = false) {
        viewModelScope.launch {
            setLoadingState(initial)

            repository.getAllCategories().fold(
                onSuccess = { categories ->
                    _uiState.update {
                        val query = it.searchQuery
                        it.copy(
                            allCategories = categories,
                            filteredCategories = filter(categories, query),
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                    }
                },
                onError = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = error
                        )
                    }
                }
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredCategories = filter(it.allCategories, query)
            )
        }
    }

    private fun setLoadingState(initial: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = initial,
                isRefreshing = !initial,
                error = null
            )
        }
    }

    private fun filter(categories: List<Category>, query: String): List<Category> {
        return if (query.isBlank()) categories
        else categories.filter { it.name.contains(query, ignoreCase = true) }
    }
}

class ArticlesViewModelFactory(
    private val repository: ApiRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticlesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticlesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}