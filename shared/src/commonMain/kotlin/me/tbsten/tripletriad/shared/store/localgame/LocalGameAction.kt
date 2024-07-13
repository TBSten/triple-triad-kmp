package me.tbsten.tripletriad.shared.store.localgame

import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.domain.game.SquareOffset

sealed interface LocalGameAction {

    data class StartTurn(
        val player: GamePlayer,
    ) : LocalGameAction

    data object StartNextPlayerTurn : LocalGameAction

    data class SelectCard(
        val selectedCard: Card,
    ) : LocalGameAction

    data class SelectSquare(
        val selectedSquareOffset: SquareOffset,
    ) : LocalGameAction

    data class PlacedCard(
        val player: GamePlayer,
        val selectedCard: Card,
        val selectedSquareOffset: SquareOffset,
    ) : LocalGameAction

    data class ProcessedByLocalRule(
        val afterState: LocalGameState,
    ) : LocalGameAction
}
