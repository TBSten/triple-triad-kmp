package me.tbsten.tripletriad.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.domain.game.CardNumber
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.domain.game.Square
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState
import me.tbsten.tripletriad.ui.PlayerColors
import me.tbsten.tripletriad.ui.TripleTriadTheme

@Composable
fun SquareView(
    state: LocalGameState,
    square: Square,
    modifier: Modifier = Modifier,
    cardViewSize: CardViewSize = CardViewSize.Normal,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
) {
    SquareView(
        square = square,
        ownerColors = { TripleTriadTheme.playerColor(state, it) },
        modifier = modifier,
        cardViewSize = cardViewSize,
        clickable = clickable,
        onClick = onClick,
    )
}

@Composable
fun SquareView(
    square: Square,
    ownerColors: (owner: GamePlayer) -> PlayerColors,
    modifier: Modifier = Modifier,
    cardViewSize: CardViewSize = CardViewSize.Normal,
    clickable: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(12.dp)
    val borderColor by animateColorAsState(if (isSelected) Color(0x99FFFF00) else Color(0x99FFFFFF))

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(enabled = clickable, onClick = onClick)
            // TODO 破線に
            .border(2.dp, borderColor, shape = shape)
            .padding(6.dp)
    ) {
        AnimatedContent(
            targetState = square,
            transitionSpec = {
                scaleIn() togetherWith scaleOut()
            }
        ) { square ->
            when (square) {
                is Square.Open ->
                    CardViewPlaceholder(
                        size = cardViewSize,
                    )

                is Square.Owned ->
                    CardView(
                        size = cardViewSize,
                        card = square.card,
                        colors = ownerColors(square.owner),
                    )
            }

        }

    }
}

@Preview
@Composable
private fun OwnedCardSquareViewPreview() {
    MaterialTheme {
        SquareView(
            square = Square.Owned(
                owner = GamePlayer(
                    id = GamePlayer.Id("test-player"),
                    name = "test player",
                    hands = listOf(),
                ),
                card = Card(id = Card.Id("test-card-1"), CardNumber.Number(2)),
            ),
            ownerColors = { TripleTriadTheme.meColors },
        )
    }
}

@Preview
@Composable
private fun OpenCardSquareViewPreview() {
    MaterialTheme {
        SquareView(
            square = Square.Open(),
            ownerColors = { TripleTriadTheme.meColors },
        )
    }
}
