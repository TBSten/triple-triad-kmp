package me.tbsten.tripletriad.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedText(
    text: String,
    stroke: Stroke,
    contentTextStyle: TextStyle,
    borderTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    overflow: TextOverflow = TextOverflow.Visible,
) {
    OutlinedText(
        annotatedString = buildAnnotatedString {
            append(text)
        },
        stroke = stroke,
        contentTextStyle = contentTextStyle,
        borderTextStyle = borderTextStyle,
        overflow = overflow,
        modifier = modifier,
    )
}

@Composable
fun OutlinedText(
    annotatedString: AnnotatedString,
    stroke: Stroke,
    contentTextStyle: TextStyle,
    borderTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    overflow: TextOverflow = TextOverflow.Visible,
) {
    val contentTextStyle = TextStyle(drawStyle = Fill)
        .merge(contentTextStyle)
    val borderTextStyle = contentTextStyle
        .merge(drawStyle = stroke)
        .merge(borderTextStyle)

    val textMeasurer = rememberTextMeasurer()

    Text(
        annotatedString,
        style = contentTextStyle,
        modifier = modifier
            .drawWithCache {
                onDrawBehind {
                    drawText(
                        textMeasurer,
                        text = annotatedString,
                        style = borderTextStyle,
                        overflow = overflow,
                    )
                }
            },
        overflow = overflow,
    )
}

@Preview
@Composable
private fun OutlinedTextPreview(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    OutlinedText(
        text = "OutlinedText",
        stroke = with(density) {
            Stroke(
                3.dp.toPx(),
                join = StrokeJoin.Round,
            )
        },
        contentTextStyle = TextStyle(color = Color.White),
        borderTextStyle = TextStyle(color = Color.Red),
    )
}
