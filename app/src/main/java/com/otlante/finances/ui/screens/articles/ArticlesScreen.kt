package com.otlante.finances.ui.screens.articles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.components.AppSearchBar
import com.otlante.finances.ui.components.ListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(snackBarHostState: SnackbarHostState, repository: ApiRepository) {
    val viewModel: ArticlesViewModel = viewModel(
        factory = ArticlesViewModelFactory(repository)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.fetchArticles() },
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.error != null) {
            LaunchedEffect(uiState.error) {
                uiState.error?.let { errorMessage ->
                    val result = snackBarHostState.showSnackbar(
                        message = errorMessage.description,
                        actionLabel = "Retry"
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.fetchArticles(initial = true)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                stickyHeader {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                        AppSearchBar(
                            query = uiState.searchQuery,
                            onQueryChange = { viewModel.onSearchQueryChanged(it) },
                            placeholder = "Найти статью"
                        )
                    }
                    HorizontalDivider()
                }
                items(
                    items = uiState.filteredCategories,
                    key = { it.id }
                ) { category ->
                    ListItem(
                        emoji = category.emoji,
                        title = category.name,
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
