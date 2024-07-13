package me.tbsten.tripletriad.feature.localgame.play.component

import androidx.compose.runtime.Composable
import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState
import me.tbsten.tripletriad.shared.store.localgame.isTurnPlayer

@Composable
fun MePlayerSection(
    state: LocalGameState,
    onCardSelect: (Card) -> Unit,
) {
    PlayerSection(
        type = PlayerSectionType.Me,
        player = state.me,
        playerPoint = state.getPoint(state.me),
        enableSelectCard = state.me.isTurnPlayer(state),
        selectedCard = if (state is LocalGameState.WithTurnPlayer) state.selectedCard else null,
        onCardSelect = onCardSelect,
    )
}
