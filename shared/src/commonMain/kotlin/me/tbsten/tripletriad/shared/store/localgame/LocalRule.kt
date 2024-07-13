package me.tbsten.tripletriad.shared.store.localgame

import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.domain.game.SquareOffset

interface LocalRule {

    // TODO other base rules
//    interface FirstHandsRule : LocalRule {
//        fun processFirstHands(state: LocalGameState): Hands
//    }

//    interface SelectCardRule : LocalRule {
//        fun selectableRule(state: LocalGameState): Selectables
//        interface Selectables {
//            fun isSelectable(state: LocalGameState, card: Card)
//            fun isSelectable(state: LocalGameState, squareOffset: SquareOffset)
//        }
//    }
}

interface CardPlaceRule : LocalRule {
    fun processGameFieldAfterPlaceCard(
        state: LocalGameState,
        player: GamePlayer,
        card: Card,
        squareOffset: SquareOffset,
    ): LocalGameState
}

//object SameRule : CardPlaceRule {
//    override fun processGameFieldAfterPlaceCard(
//        state: LocalGameState,
//        player: GamePlayer,
//        card: Card,
//        squareOffset: SquareOffset,
//    ): CardPlaceRule.Result? {
//        val cardNumberPairsEachDirection =
//            mapOf(
//                card.top to state.gameField.topCardOf(squareOffset)?.top,
//                card.bottom to state.gameField.bottomCardOf(squareOffset)?.bottom,
//                card.left to state.gameField.leftCardOf(squareOffset)?.left,
//                card.right to state.gameField.rightCardOf(squareOffset)?.right,
//            )
//        val sameNumberPairs = cardNumberPairsEachDirection
//            .filter { (cardNumber, nearCardNumber) ->
//                cardNumber == nearCardNumber
//            }
//        TODO("")
//    }
//}
