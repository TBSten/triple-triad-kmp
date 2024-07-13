package me.tbsten.tripletriad.shared

import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.domain.game.CardNumber
import me.tbsten.tripletriad.shared.domain.game.GameField
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.domain.game.Square
import me.tbsten.tripletriad.shared.domain.game.SquareOffset
import kotlin.test.Test
import kotlin.test.assertEquals

class GameFieldTest {
    @Test
    fun squareOffsetTest() {
        val player = GamePlayer(
            id = GamePlayer.Id("test-1"),
            name = "test-1",
            hands = listOf(),
        )
        val gameField = GameField(
            squares = listOf(
                testOwnedSquare(player, 0),
                testOwnedSquare(player, 1),
                testOwnedSquare(player, 2),
                testOwnedSquare(player, 3),
                testOwnedSquare(player, 4),
                testOwnedSquare(player, 5),
                testOwnedSquare(player, 6),
                testOwnedSquare(player, 7),
                testOwnedSquare(player, 8),
            ),
            width = 3,
            height = 3,
        )
        assertEquals(
            gameField.squares[1].card,
            gameField.topCardOf(SquareOffset(1, 1)),
        )
        assertEquals(
            gameField.squares[7].card,
            gameField.bottomCardOf(SquareOffset(1, 1)),
        )
        assertEquals(
            gameField.squares[3].card,
            gameField.leftCardOf(SquareOffset(1, 1)),
        )
        assertEquals(
            gameField.squares[5].card,
            gameField.rightCardOf(SquareOffset(1, 1)),
        )

        assertEquals(
            gameField.squares[2].card,
            gameField.topCardOf(SquareOffset(2, 1)),
        )
        assertEquals(
            gameField.squares[8].card,
            gameField.bottomCardOf(SquareOffset(2, 1)),
        )
        assertEquals(
            gameField.squares[4].card,
            gameField.leftCardOf(SquareOffset(2, 1)),
        )
        assertEquals(
            null,
            gameField.rightCardOf(SquareOffset(2, 1)),
        )

    }

    private fun testOwnedSquare(player: GamePlayer, number: Int) =
        Square.Owned(
            owner = player,
            card = Card(
                id = Card.Id("test-card-$number"),
                CardNumber.Number(number),
            ),
        )
}
