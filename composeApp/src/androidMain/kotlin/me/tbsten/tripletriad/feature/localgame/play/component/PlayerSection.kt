package me.tbsten.tripletriad.feature.localgame.play.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.tbsten.tripletriad.component.CardView
import me.tbsten.tripletriad.component.CardViewPlaceholder
import me.tbsten.tripletriad.component.CardViewSize
import me.tbsten.tripletriad.component.HorizontalDivider
import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.domain.game.CardNumber
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.ui.PlayerColors
import me.tbsten.tripletriad.ui.TripleTriadTheme
import me.tbsten.tripletriad.ui.cardFontFamily

@Composable
fun PlayerSection(
    type: PlayerSectionType,
    player: GamePlayer,
    playerPoint: Int,
    enableSelectCard: Boolean,
    onCardSelect: (Card) -> Unit,
    selectedCard: Card?,
    modifier: Modifier = Modifier,
    colors: PlayerColors = type.playerColors,
) {
    Column(modifier) {
        when (type) {
            PlayerSectionType.Me -> {
                MenuBar(
                    type = type,
                    name = player.name,
                    playerPoint = playerPoint,
                    colors = colors,
                )
                Hands(
                    hands = player.hands,
                    colors = colors,
                    enableSelectCard = enableSelectCard,
                    onCardSelect = onCardSelect,
                    selectedCard = selectedCard,
                    reverse = false,
                )
            }

            PlayerSectionType.Enemy -> {
                Hands(
                    hands = player.hands,
                    colors = colors,
                    enableSelectCard = enableSelectCard,
                    onCardSelect = onCardSelect,
                    selectedCard = selectedCard,
                    reverse = true,
                )
                MenuBar(
                    type = type,
                    name = player.name,
                    playerPoint = playerPoint,
                    colors = colors,
                )
            }
        }
    }
}

@Composable
private fun MenuBar(
    type: PlayerSectionType,
    name: String,
    playerPoint: Int,
    colors: PlayerColors,
    modifier: Modifier = Modifier,
) {
    val border = remember {
        movableContentOf {
            HorizontalDivider(
                brush = Brush.horizontalGradient(
                    colors.sectionBorder,
                ),
                thickness = 2.dp,
            )
        }
    }
    Column(modifier.padding(start = 12.dp)) {
        if (type == PlayerSectionType.Enemy) {
            border()
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = name,
                style = TextStyle(
                    fontFamily = cardFontFamily,
                    fontSize = 16.sp,
                    color = Color(0xFFFFFFFF),
                ),
                modifier = Modifier
                    .align(if (type == PlayerSectionType.Me) Alignment.Bottom else Alignment.Top)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
            PlayerPoint(playerPoint = playerPoint, colors = colors)
        }
        if (type == PlayerSectionType.Me) {
            border()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Hands(
    hands: List<Card>,
    colors: PlayerColors,
    enableSelectCard: Boolean,
    onCardSelect: (Card) -> Unit,
    selectedCard: Card?,
    reverse: Boolean,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
    ) {
        items(hands) { card ->
            val cardScale by animateFloatAsState(
                if (selectedCard == card) 1.25f else 1.00f
            )
            CardView(
                card = card,
                colors = colors,
                size = CardViewSize.Small,
                clickable = enableSelectCard,
                onClick = {
                    onCardSelect(card)
                },
                modifier = Modifier
                    .animateItemPlacement()
                    .scale(cardScale)
                    .rotate(if (reverse) 180f else 0f),
            )
        }
        if (hands.isEmpty()) {
            item {
                CardViewPlaceholder(
                    size = CardViewSize.Small,
                )
            }
        }
    }
}

enum class PlayerSectionType(val playerColors: PlayerColors) {
    Me(TripleTriadTheme.meColors),
    Enemy(TripleTriadTheme.enemyColors),
}

@Preview
@Composable
private fun MePlayerSectionPreview() {
    val player = GamePlayer(
        id = GamePlayer.Id("test-player"),
        name = "テスト",
        hands = listOf(
            Card(
                id = Card.Id("test-card-1"),
                CardNumber.Number(1),
            ),
            Card(
                id = Card.Id("test-card-2"),
                CardNumber.Number(2),
            ),
            Card(
                id = Card.Id("test-card-3"),
                CardNumber.Ace,
            ),
        ),
    )

    MaterialTheme {
        PlayerSection(
            type = PlayerSectionType.Me,
            player = player,
            playerPoint = 4,
            enableSelectCard = false,
            onCardSelect = {},
            selectedCard = player.hands[2],
        )
    }
}

@Preview
@Composable
private fun EnemyPlayerSectionPreview() {
    val player = GamePlayer(
        id = GamePlayer.Id("test-player"),
        name = "テスト",
        hands = listOf(
            Card(
                id = Card.Id("test-card-1"),
                CardNumber.Number(1),
            ),
            Card(
                id = Card.Id("test-card-2"),
                CardNumber.Number(2),
            ),
            Card(
                id = Card.Id("test-card-3"),
                CardNumber.Ace,
            ),
        ),
    )

    MaterialTheme {
        PlayerSection(
            type = PlayerSectionType.Enemy,
            player = player,
            playerPoint = 4,
            enableSelectCard = false,
            onCardSelect = {},
            selectedCard = player.hands[2],
        )
    }
}
