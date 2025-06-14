package com.otlante.finances.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.otlante.finances.R

enum class ListItemType { BASIC, SUMMARIZE }

fun isEmoji(content: String): Boolean {
    val codePoints = content.codePoints().toArray()
    return codePoints.all { codePoint ->
        Character.getType(codePoint).toByte() == Character.OTHER_SYMBOL
    }
}

@Composable
fun ScreenHeader(
    titleText: String,
    iconImageId: Int? = null,
    iconContentDescription: String = "",
    onIconClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSecondaryContainer)
            .padding(16.dp)
    ) {
        Text(
            text = titleText,
            style = typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
        iconImageId?.let {
            IconButton(
                onClick = onIconClick,
                modifier = Modifier.align(Alignment.CenterEnd).size(24.dp)
            ) {
                Icon(
                    painter = painterResource(iconImageId),
                    contentDescription = iconContentDescription,
                )
            }
        }
    }
}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    type: ListItemType = ListItemType.BASIC,
    emoji: String? = null,
    title: String,
    subtitle: String? = null,
    rightText: String? = null,
    showArrow: Boolean = false,
    showTrailing: Boolean = false,
    showSwitch: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    val listItemTypeModifier = when (type) {
        ListItemType.BASIC -> Modifier.height(72.dp).padding(horizontal = 16.dp)
        ListItemType.SUMMARIZE -> Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier)
            .then(listItemTypeModifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(emoji, type)

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = typography.bodyLarge
            )

            subtitle?.let {
                Text(
                    text = it,
                    style = typography.bodyMedium
                )
            }
        }

        rightText?.let {
            Text(
                text = it,
                style = typography.bodyLarge,
            )
            if (showArrow || showTrailing || showSwitch) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }

        if (showArrow) {
            Icon(
                painter = painterResource(R.drawable.ic_more),
                contentDescription = "Подробнее",
            )
        } else if (showTrailing) {
            Icon(
                painter = painterResource(R.drawable.ic_trailing_element),
                contentDescription = "Подробнее",
            )
        } else if (showSwitch) {
            var checked by remember { mutableStateOf(true) }
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onCheckedChange?.invoke(it)
                },
            )
        }
    }
}

@Composable
private fun Avatar(emoji: String?, type: ListItemType) {
    emoji?.let {
        val textSize = if (isEmoji(it)) 16.sp else 10.sp

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = when (type) {
                        ListItemType.BASIC -> MaterialTheme.colorScheme.secondaryContainer
                        ListItemType.SUMMARIZE -> Color.White
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = it, fontSize = textSize)
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}