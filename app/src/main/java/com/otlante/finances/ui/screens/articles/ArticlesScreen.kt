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
import com.otlante.finances.ui.composition.LocalViewModelFactory
import com.otlante.finances.ui.components.AppSearchBar
import com.otlante.finances.ui.components.ListItem

/**
 * Composable function displaying the articles screen, which allows
 * the user to view categories of articles with search functionality.
 * Supports pull-to-refresh, loading indicators, and error handling
 * via snackbar.
 *
 * @param snackBarHostState the [SnackbarHostState] used to show error messages and retry prompts
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(snackBarHostState: SnackbarHostState) {

    val factory = LocalViewModelFactory.current
    val viewModel: ArticlesViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.error?.let { errorMessage ->
        LaunchedEffect(uiState.error) {
            val result = snackBarHostState.showSnackbar(
                message = errorMessage.description,
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.fetchArticles(initial = true)
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.fetchArticles() },
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize()
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
