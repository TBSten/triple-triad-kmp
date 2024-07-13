package me.tbsten.tripletriad.feature.localgame.play.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.tbsten.tripletriad.ui.PlayerColors
import me.tbsten.tripletriad.ui.cardFontFamily

@Composable
fun PlayerPoint(
    playerPoint: Int,
    colors: PlayerColors,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    // TODO AnimatedContentでアニメーションする
    Text(
        text = "$playerPoint",
        style = TextStyle(
            fontFamily = cardFontFamily,
            fontSize = 60.sp,
            color = Color(0xFFFFFFFF),
            shadow = Shadow(
                color = colors.pointShadow,
                blurRadius = with(density) { 8.dp.toPx() },
            ),
        ),
        modifier = modifier,
    )
}

