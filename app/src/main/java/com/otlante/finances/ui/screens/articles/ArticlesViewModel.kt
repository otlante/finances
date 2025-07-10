package com.otlante.finances.ui.screens.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.otlante.finances.data.remote.NetworkError
import com.otlante.finances.domain.entity.Category
import com.otlante.finances.domain.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state holder for the Articles screen.
 *
 * @property searchQuery the current search text for filtering categories
 * @property allCategories the list of all categories loaded from the API
 * @property filteredCategories the categories filtered by [searchQuery]
 * @property isLoading true if the initial load is in progress
 * @property isRefreshing true if pull-to-refresh is in progress
 * @property error optional [NetworkError] describing any failure
 */
data class ArticlesUiState(
    val searchQuery: String = "",
    val allCategories: List<Category> = emptyList(),
    val filteredCategories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: NetworkError? = null
)

/**
 * ViewModel responsible for loading and managing the article categories,
 * including search filtering, error handling, and loading states.
 *
 * @property repository the [ApiRepository] used to load category data
 */
class ArticlesViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticlesUiState())
    val uiState: StateFlow<ArticlesUiState> = _uiState

    init {
        fetchArticles(initial = true)
    }

    /**
     * Loads categories from the API and updates the UI state.
     *
     * @param initial true if this is the initial load, false if called from pull-to-refresh
     */
    fun fetchArticles(initial: Boolean = false) {
        viewModelScope.launch {
            setLoadingState(initial)

            repository.getAllCategories().fold(
                onSuccess = ::fetchArticlesSuccess,
                onError = ::fetchArticlesError
            )
        }
    }

    private fun fetchArticlesError(error: NetworkError) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isRefreshing = false,
                error = error
            )
        }
    }

    private fun fetchArticlesSuccess(categories: List<Category>) {
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
    }

    /**
     * Handles updates to the search query, immediately applying filtering to the list.
     *
     * @param query the new search query text
     */
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

/**
 * Factory for creating an [ArticlesViewModel] with the required [ApiRepository] dependency.
 *
 * @property repository the repository to inject into the ViewModel
 */
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
