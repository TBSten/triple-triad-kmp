package me.tbsten.tripletriad.feature.localgame.result.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.tbsten.tripletriad.ui.TripleTriadTheme
import me.tbsten.tripletriad.ui.cardFontFamily

@Composable
fun WinSection(modifier: Modifier = Modifier) {
    val colors = TripleTriadTheme.meColors
    val density = LocalDensity.current

    Text(
        text = "WIN !!",
        style = TextStyle(
            fontFamily = cardFontFamily,
            fontSize = 80.sp,
            color = Color(0xFFFFFFFF),
            shadow = Shadow(
                color = colors.pointShadow,
                blurRadius = with(density) { 8.dp.toPx() },
            ),
            textDecoration = TextDecoration.Underline,
        ),
        modifier = modifier,
    )
}

