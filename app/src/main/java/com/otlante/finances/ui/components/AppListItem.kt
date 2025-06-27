package com.otlante.finances.ui.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.otlante.finances.R

/**
 * Composable function that displays a customizable list item with optional
 * emoji avatar, title, subtitle, trailing texts, icons, and switches.
 *
 * Supports two styles defined by [ListItemType]: BASIC and SUMMARIZE.
 *
 * @param modifier modifier to be applied to the root layout
 * @param type style type of the list item, affecting layout and background
 * @param emoji optional emoji string displayed as an avatar on the left
 * @param title main text content of the list item
 * @param subtitle optional secondary text below the title
 * @param rightText optional text aligned to the right side
 * @param rightComment optional smaller text below [rightText]
 * @param showArrow whether to show a right arrow icon
 * @param showTrailing whether to show a trailing icon different from arrow
 * @param showSwitch whether to show a switch toggle on the right side
 * @param onCheckedChange optional callback for switch toggle changes
 * @param onClick optional click listener for the whole item
 */
@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    type: ListItemType = ListItemType.BASIC,
    emoji: String? = null,
    title: String,
    subtitle: String? = null,
    rightText: String? = null,
    rightComment: String? = null,
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
        ListItemType.BASIC -> Modifier
            .height(72.dp)
            .padding(horizontal = 16.dp)

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

        BodyColumn(title, subtitle, Modifier.weight(1f))

        RightInfoColumn(
            rightText,
            rightComment,
            showArrow,
            showTrailing,
            showSwitch,
            modifier = Modifier.weight(1f)
        )

        TrailingElement(showArrow, showTrailing, showSwitch, onCheckedChange)
    }
}

@Composable
private fun BodyColumn(
    title: String,
    subtitle: String?,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = typography.bodyLarge
        )

        subtitle?.let {
            if (it.isNotEmpty()) {
                Text(
                    text = it,
                    style = typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun RightInfoColumn(
    rightText: String?,
    rightComment: String?,
    showArrow: Boolean,
    showTrailing: Boolean,
    showSwitch: Boolean,
    modifier: Modifier
) {
    rightText?.let {
        Column(
            modifier = modifier,
        ) {
            Text(
                text = it,
                style = typography.bodyLarge,
                modifier = Modifier.align(Alignment.End)
            )

            rightComment?.let { comment ->
                if (comment.isNotEmpty()) {
                    Text(
                        text = comment,
                        style = typography.bodyMedium,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
        if (showArrow || showTrailing || showSwitch) {
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
private fun TrailingElement(
    showArrow: Boolean,
    showTrailing: Boolean,
    showSwitch: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?
) {
    when {
        showArrow -> Icon(
            painter = painterResource(R.drawable.ic_more),
            contentDescription = stringResource(R.string.show_more),
        )

        showTrailing -> Icon(
            painter = painterResource(R.drawable.ic_trailing_element),
            contentDescription = stringResource(R.string.show_more),
        )

        showSwitch -> {
            var checked by remember { mutableStateOf(false) }
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

/**
 * Private Composable that displays an emoji avatar with
 * a circular background whose color depends on [type].
 *
 * @param emoji the emoji string to display
 * @param type the list item type to determine styling
 */
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

/**
 * Enum representing types of list items for styling and layout purposes.
 */
enum class ListItemType { BASIC, SUMMARIZE }

/**
 * Checks if the given [content] string consists only of emoji characters.
 *
 * @param content the string to check
 * @return true if all characters in the string are emojis, false otherwise
 */
private fun isEmoji(content: String): Boolean {
    val codePoints = content.codePoints().toArray()
    return codePoints.all { codePoint ->
        Character.getType(codePoint).toByte() == Character.OTHER_SYMBOL
    }
}
