package me.tbsten.tripletriad.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.translate(x: Float = 0f, y: Float = 0f) =
    this.graphicsLayer {
        translationX = x
        translationY = y
    }

fun Modifier.translate(x: Dp = 0.dp, y: Dp = 0.dp) =
    this.graphicsLayer {
        translationX = x.toPx()
        translationY = y.toPx()
    }
