package me.tbsten.tripletriad.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.domain.game.CardNumber
import me.tbsten.tripletriad.ui.PlayerColors
import me.tbsten.tripletriad.ui.TripleTriadTheme
import me.tbsten.tripletriad.ui.cardFontFamily

@Composable
fun CardView(
    card: Card,
    colors: PlayerColors,
    modifier: Modifier = Modifier,
    size: CardViewSize = CardViewSize.Normal,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
) {
    val cardShape = RoundedCornerShape(6.dp)
    val baseTextStyle =
        TextStyle(
            textAlign = TextAlign.Center,
            fontFamily = cardFontFamily,
        )

    val numberTextStyle =
        baseTextStyle.merge(
            size.numberTextStyle,
        ).merge(
            color = colors.content,
        )

    val numberBorderTextStyle =
        numberTextStyle
            .merge(
                color = colors.contentBorder,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both,
                ),
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            )

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .clip(cardShape)
            .then(if (clickable) Modifier.clickable(onClick = onClick) else Modifier)
            .size(size.cardSize)
            .aspectRatio(size.cardSize.width / size.cardSize.height, true),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .border(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFFD2C38B),
                            Color(0xFFA28C3B),
                        )
                    ),
                    width = size.borderWidth,
                    shape = cardShape,
                )
                .background(colors.background, shape = cardShape),
        )

        OutlinedText(
            annotatedString = buildAnnotatedString {
                append(card.top.displayText)
                append("\n")
                append(card.left.displayText)
                append(" ")
                append(card.right.displayText)
                append("\n")
                append(card.bottom.displayText)
            },
            stroke = with(density) {
                Stroke(
                    3.dp.toPx(),
                    join = StrokeJoin.Round,
                )
            },
            contentTextStyle = numberTextStyle,
            borderTextStyle = numberBorderTextStyle,
            modifier = Modifier.padding(3.dp),
        )
    }
}

enum class CardViewSize(
    internal val cardSize: DpSize,
    internal val borderWidth: Dp,
    internal val numberTextStyle: TextStyle,
) {
    Small(
        cardSize = DpSize(59.dp, 76.dp),
        borderWidth = 3.dp,
        numberTextStyle = TextStyle(
            fontSize = 22.sp,
            lineHeight = 18.sp,
        ),
    ),
    Normal(
        cardSize = DpSize(70.dp, 90.dp),
        borderWidth = 4.dp,
        numberTextStyle = TextStyle(
            fontSize = 26.sp,
            lineHeight = 20.sp,
        ),
    ),
}

@Preview
@Composable
private fun NormalSizeCardViewPreview() {
    MaterialTheme {
        CardView(
            card = Card(
                id = Card.Id("test-card-1"),
                top = CardNumber.Number(1),
                bottom = CardNumber.Number(2),
                left = CardNumber.Number(3),
                right = CardNumber.Ace,
            ),
            colors = TripleTriadTheme.meColors,
            size = CardViewSize.Normal,
        )
    }
}


@Preview
@Composable
private fun SmallSizeCardViewPreview() {
    MaterialTheme {
        CardView(
            card = Card(
                id = Card.Id("test-card-1"),
                top = CardNumber.Number(1),
                bottom = CardNumber.Number(2),
                left = CardNumber.Number(3),
                right = CardNumber.Ace,
            ),
            colors = TripleTriadTheme.enemyColors,
            size = CardViewSize.Small,
        )
    }
}
