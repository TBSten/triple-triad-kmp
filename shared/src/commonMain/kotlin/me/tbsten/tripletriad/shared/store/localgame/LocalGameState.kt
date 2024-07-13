package me.tbsten.tripletriad.shared.store.localgame

import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.domain.game.GameField
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.domain.game.Square
import me.tbsten.tripletriad.shared.domain.game.SquareOffset

sealed interface LocalGameState {
    val me: GamePlayer
    val enemy: GamePlayer
    val gameField: GameField

    val players: List<GamePlayer>
        get() = listOf(
            me,
            enemy,
        )

    fun getPlayer(id: GamePlayer.Id): GamePlayer =
        players.find { it.id == id } ?: TODO("invalid player id : $id")

    fun getPoint(player: GamePlayer) =
        gameField.squares.count { it is Square.Owned && it.owner == player } +
                player.hands.size

    data class PreStart(
        override val me: GamePlayer,
        override val enemy: GamePlayer,
        override val gameField: GameField,
    ) : LocalGameState

    data class WithTurnPlayer(
        override val me: GamePlayer,
        override val enemy: GamePlayer,
        override val gameField: GameField,
        val turnPlayer: GamePlayer,
        val selectedCard: Card?,
        val selectedSquareOffset: SquareOffset?,
        val applyingRulesHistory: List<LocalGameState>?,
    ) : LocalGameState

    data class Finished(
        override val me: GamePlayer,
        override val enemy: GamePlayer,
        override val gameField: GameField,
        val winner: GamePlayer?,
    ) : LocalGameState
}

fun GamePlayer.isTurnPlayer(state: LocalGameState) =
    state is LocalGameState.WithTurnPlayer && state.turnPlayer == this
