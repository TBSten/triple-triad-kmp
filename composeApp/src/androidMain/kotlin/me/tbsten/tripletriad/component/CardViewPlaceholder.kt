package me.tbsten.tripletriad.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun CardViewPlaceholder(
    size: CardViewSize = CardViewSize.Normal,
    modifier: Modifier = Modifier,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .then(if(clickable) Modifier.clickable(onClick = onClick) else Modifier)
            .size(size.cardSize)
    )
}

@Preview
@Composable
private fun CardViewPlaceholderPreview() {
    MaterialTheme {
        CardViewPlaceholder()
    }
}