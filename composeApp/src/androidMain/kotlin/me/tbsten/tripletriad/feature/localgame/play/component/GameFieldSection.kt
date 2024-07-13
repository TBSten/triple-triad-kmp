package me.tbsten.tripletriad.feature.localgame.play.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import me.tbsten.tripletriad.component.GameFieldView
import me.tbsten.tripletriad.shared.domain.game.Square
import me.tbsten.tripletriad.shared.domain.game.SquareOffset
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState
import me.tbsten.tripletriad.shared.store.localgame.isTurnPlayer
import me.tbsten.tripletriad.ui.TripleTriadTheme

@Composable
fun GameFieldSection(
    state: LocalGameState,
    onSquareSelect: (SquareOffset, Square) -> Unit,
    selectedSquareOffset: SquareOffset?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        GameFieldView(
            gameField = state.gameField,
            ownerColors = { owner -> TripleTriadTheme.playerColor(state, owner) },
            modifier = Modifier.zIndex(1f),
            clickable = { state.me.isTurnPlayer(state) },
            selectedSquareOffset = selectedSquareOffset,
            onSquareClick = { offset, square ->
                onSquareSelect(offset, square)
            },
        )
    }
}
