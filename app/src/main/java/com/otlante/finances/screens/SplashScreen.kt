package com.otlante.finances.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.otlante.finances.R
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import com.otlante.finances.BottomNavItem
import kotlinx.coroutines.launch

object SplashScreen {
    const val ROUTE = "splash"
}

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val isDone by viewModel.isAnimationFinished

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_animation))
    val progress by animateLottieCompositionAsState(composition, iterations = 1)

    LaunchedEffect(isDone) {
        if (isDone) {
            navController.navigate(BottomNavItem.Expenses.route) {
                popUpTo(SplashScreen.ROUTE) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(250.dp)
        )
    }
}

class SplashViewModel : ViewModel() {
    private val _isAnimationFinished = mutableStateOf(false)
    val isAnimationFinished: State<Boolean> = _isAnimationFinished

    init {
        viewModelScope.launch {
            delay(2500)
            _isAnimationFinished.value = true
        }
    }
}