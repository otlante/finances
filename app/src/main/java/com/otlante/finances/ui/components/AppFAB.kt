package com.otlante.finances.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.otlante.finances.R

/**
 * Composable function that displays a Floating Action Button (FAB)
 * with a plus icon, typically used for adding new items.
 *
 * @param onClick callback invoked when the FAB is clicked
 */
@Composable
fun AppFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.fab_description)
        )
    }
}
