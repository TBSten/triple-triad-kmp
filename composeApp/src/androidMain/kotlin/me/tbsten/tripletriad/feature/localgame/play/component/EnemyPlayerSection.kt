package me.tbsten.tripletriad.feature.localgame.play.component

import androidx.compose.runtime.Composable
import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState

@Composable
fun EnemyPlayerSection(
    state: LocalGameState,
    onCardSelect: (Card) -> Unit,
) {
    PlayerSection(
        type = PlayerSectionType.Enemy,
        player = state.enemy,
        playerPoint = state.getPoint(state.enemy),
//        enableSelectCard = state.enemy.isTurnPlayer(state),
        enableSelectCard = false,
        selectedCard = if (state is LocalGameState.WithTurnPlayer) state.selectedCard else null,
        onCardSelect = onCardSelect,
    )
}
