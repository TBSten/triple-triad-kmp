package me.tbsten.tripletriad.shared.store.localgame.controllable

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import me.tbsten.tripletriad.shared.domain.game.GameField
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.domain.game.Square
import me.tbsten.tripletriad.shared.domain.game.SquareOffset
import me.tbsten.tripletriad.shared.store.localgame.LocalGameAction
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState
import me.tbsten.tripletriad.shared.store.localgame.LocalGameStore
import me.tbsten.tripletriad.shared.store.localgame.isTurnPlayer

interface Controller {
    suspend fun collectStore(store: LocalGameStore)
}

class RandomController(private val ownerPlayerId: GamePlayer.Id) : Controller {
    private fun startOwnerPlayerTurnFlow(store: LocalGameStore) =
        store.stateFlow
            .filterIsInstance<LocalGameState.WithTurnPlayer>()
            .filter { state ->
                state.getPlayer(ownerPlayerId).isTurnPlayer(state) &&
                        state.selectedCard == null && state.selectedSquareOffset == null &&
                        state.applyingRulesHistory == null
            }

    override suspend fun collectStore(store: LocalGameStore) {
        startOwnerPlayerTurnFlow(store).collect { state ->
            delay(2_000)
            store.dispatch(action = LocalGameAction.SelectCard(selectRandomHand(state)))
            delay(1_000)
            println("app-debug: auto-select-square")
            store.dispatch(action = LocalGameAction.SelectSquare(selectRandomSquare(state)))
        }
    }

    private fun selectRandomHand(state: LocalGameState) =
        state.getPlayer(ownerPlayerId).hands.random()

    private fun selectRandomSquare(state: LocalGameState): SquareOffset {
        val randomSquare =
            state.gameField.squares
                .filterIsInstance<Square.Open>()
                .random()
        return getSquareOffset(
            state.gameField,
            randomSquare,
        )
    }

    private fun getSquareOffset(gameField: GameField, square: Square): SquareOffset {
        val index = gameField.squares.indexOf(square)
        return SquareOffset(
            x = index % gameField.height,
            y = index / gameField.width,
        )
    }
}
