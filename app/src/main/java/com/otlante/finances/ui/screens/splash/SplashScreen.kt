package com.otlante.finances.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.otlante.finances.R

/**
 * Splash screen composable that displays a Lottie animation while the app is loading.
 *
 * @param onReady Callback triggered when the Lottie composition is ready to be displayed.
 * @param onAnimationFinished Callback triggered when the animation playback completes.
 */
@Composable
fun SplashScreen(
    onReady: () -> Unit,
    onAnimationFinished: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash_animation)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 2f
    )

    LaunchedEffect(composition) {
        composition?.let { onReady() }
    }

    LaunchedEffect(progress) {
        if (progress >= 1f) onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        composition?.let {
            LottieAnimation(
                composition = it,
                progress = { progress },
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }
    }
}
