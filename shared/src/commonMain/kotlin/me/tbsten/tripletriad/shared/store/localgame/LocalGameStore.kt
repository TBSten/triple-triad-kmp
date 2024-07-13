package me.tbsten.tripletriad.shared.store.localgame

import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.domain.game.GameField
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.domain.game.Square
import me.tbsten.tripletriad.shared.domain.game.SquareOffset
import me.tbsten.tripletriad.shared.flux.FluxStore

class LocalGameStore(
    initialState: LocalGameState,
    localRules: List<LocalRule>,
) : FluxStore<LocalGameState, LocalGameAction>(initialState = initialState) {
    private val cardPlaceRules =
        listOf(BasicRule) + localRules.filterIsInstance<CardPlaceRule>()

    override fun reducer(prevState: LocalGameState, action: LocalGameAction): LocalGameState =
        when (action) {
            is LocalGameAction.StartTurn -> startTurn(prevState, action)
            is LocalGameAction.StartNextPlayerTurn -> startNextPlayerTurn(prevState)
            is LocalGameAction.SelectCard -> selectCard(prevState, action)
            is LocalGameAction.SelectSquare -> selectSquare(prevState, action)
            is LocalGameAction.PlacedCard -> placedCard(prevState, action)
            is LocalGameAction.ProcessedByLocalRule -> processedByLocalRule(prevState, action)
        }

    override fun dispatch(action: LocalGameAction) {
        super.dispatch(action)
        println("app-debug: dispatch-action: $action")
    }

    private fun startTurn(
        prevState: LocalGameState,
        action: LocalGameAction.StartTurn,
    ): LocalGameState {
        return when (prevState) {
            is LocalGameState.PreStart ->
                LocalGameState.WithTurnPlayer(
                    me = prevState.me,
                    enemy = prevState.enemy,
                    gameField = prevState.gameField,
                    turnPlayer = action.player,
                    selectedCard = null,
                    selectedSquareOffset = null,
                    applyingRulesHistory = null,
                )

            is LocalGameState.WithTurnPlayer ->
                prevState.copy(
                    turnPlayer = action.player,
                    selectedCard = null,
                    selectedSquareOffset = null,
                )

            else -> TODO("invalid state transition")
        }
    }

    private fun startNextPlayerTurn(
        prevState: LocalGameState,
    ): LocalGameState =
        if (prevState is LocalGameState.WithTurnPlayer) {
            val nextTurnPlayer =
                if (prevState.turnPlayer == prevState.me)
                    prevState.enemy
                else
                    prevState.me
            startTurn(prevState, LocalGameAction.StartTurn(nextTurnPlayer))
        } else {
            TODO("invalid state transition")
        }

    private fun selectCard(
        prevState: LocalGameState,
        action: LocalGameAction.SelectCard,
    ): LocalGameState {
        require(prevState is LocalGameState.WithTurnPlayer)
        return prevState.copy(
            selectedCard = action.selectedCard,
        )
    }

    private fun selectSquare(
        prevState: LocalGameState,
        action: LocalGameAction.SelectSquare,
    ): LocalGameState {
        require(prevState is LocalGameState.WithTurnPlayer)
        return prevState.copy(
            selectedSquareOffset = action.selectedSquareOffset,
        )
    }

    private fun placedCard(
        prevState: LocalGameState,
        action: LocalGameAction.PlacedCard,
    ): LocalGameState =
        if (prevState is LocalGameState.WithTurnPlayer) {
            // selectedCardをselectedSquareOffsetに配置し、手札から削除する。
            val selectedCard = action.selectedCard
            val selectedSquareOffset = action.selectedSquareOffset
            val newGameField = prevState.gameField
                .setCard(selectedSquareOffset, selectedCard, action.player)
            val newMeHands = prevState.me.hands
                .filter { it != selectedCard }
            val newEnemyHands = prevState.enemy.hands
                .filter { it != selectedCard }
            val placedCardGameState =
                prevState.copy(
                    me = prevState.me.updateHands(newMeHands),
                    enemy = prevState.enemy.updateHands(newEnemyHands),
                    gameField = newGameField,
                    selectedCard = null,
                    selectedSquareOffset = null,
                )
            // rulesの適用
            val updates = mutableListOf<LocalGameState>()
            val appliedRulesGameState =
                cardPlaceRules.fold<CardPlaceRule, LocalGameState>(placedCardGameState) { prevState, rule ->
                    rule.processGameFieldAfterPlaceCard(
                        state = prevState,
                        player = action.player,
                        card = action.selectedCard,
                        squareOffset = action.selectedSquareOffset,
                    ).also {
                        if (prevState != it) updates.add(it)
                    }
                }
            when {
                updates.isNotEmpty() -> {
                    // ルールによる更新があったので applyingRulesHistoryに更新情報を格納してアニメーションを発火する
                    // ルールによる更新はアニメーション後に適用するため、戻り値にはカードを配置した時点でのGameState (placedCardGameState) を適用する
                    placedCardGameState.copy(
                        applyingRulesHistory = updates,
                    )
                }

                else -> {
                    require(placedCardGameState == appliedRulesGameState)
                    finishTurn(appliedRulesGameState)
                }
            }
        } else {
            TODO("invalid state transition")
        }

    private fun processedByLocalRule(
        prevState: LocalGameState,
        action: LocalGameAction.ProcessedByLocalRule,
    ): LocalGameState {
        val newState = action.afterState
        require(newState is LocalGameState.WithTurnPlayer)
        return finishTurn(newState.copy(applyingRulesHistory = null))
    }

    private fun finishTurn(prevState: LocalGameState): LocalGameState {
        return if (prevState.gameField.isFill) {
            finish(prevState)
        } else {
            startNextPlayerTurn(prevState)
        }
    }

    private fun GameField.setCard(offset: SquareOffset, card: Card, owner: GamePlayer): GameField {
        val selectedIndex = squareOffsetToIndex(offset)
        return this.copy(
            squares = this.squares
                .mapIndexed { index, square ->
                    if (selectedIndex == index)
                        Square.Owned(
                            owner = owner,
                            card = card,
                        )
                    else
                        square
                }
        )
    }

    private fun finish(
        prevState: LocalGameState,
    ): LocalGameState {
        val mePoint = prevState.getPoint(prevState.me)
        val enemyPoint = prevState.getPoint(prevState.enemy)
        val winner = when {
            mePoint > enemyPoint -> prevState.me
            enemyPoint > mePoint -> prevState.me
            else -> prevState.me
        }
        return LocalGameState.Finished(
            me = prevState.me,
            enemy = prevState.enemy,
            gameField = prevState.gameField,
            winner = winner,
        )
    }
}


private object BasicRule : CardPlaceRule {
    override fun processGameFieldAfterPlaceCard(
        state: LocalGameState,
        player: GamePlayer,
        card: Card,
        squareOffset: SquareOffset
    ): LocalGameState {
        require(state is LocalGameState.WithTurnPlayer)
        val gameField = state.gameField
        val newSquares = gameField.squares.toMutableList()

        gameField.topOf(squareOffset)?.let { topOffset ->
            val topCard = gameField.getOrNull(topOffset)?.card ?: return@let
            if (card.top > topCard.bottom) {
                newSquares[gameField.squareOffsetToIndex(topOffset)] = Square.Owned(
                    owner = player,
                    card = topCard,
                )
            }
        }
        gameField.bottomOf(squareOffset)?.let { bottomOffset ->
            val bottomCard = gameField.getOrNull(bottomOffset)?.card ?: return@let
            if (card.bottom > bottomCard.top) {
                newSquares[gameField.squareOffsetToIndex(bottomOffset)] = Square.Owned(
                    owner = player,
                    card = bottomCard,
                )
            }
        }
        gameField.leftOf(squareOffset)?.let { leftOffset ->
            val leftCard = gameField.getOrNull(leftOffset)?.card ?: return@let
            if (card.left > leftCard.right) {
                newSquares[gameField.squareOffsetToIndex(leftOffset)] = Square.Owned(
                    owner = player,
                    card = leftCard,
                )
            }
        }
        gameField.rightOf(squareOffset)?.let { rightOffset ->
            val rightCard = gameField.getOrNull(rightOffset)?.card ?: return@let
            if (card.right > rightCard.left) {
                newSquares[gameField.squareOffsetToIndex(rightOffset)] = Square.Owned(
                    owner = player,
                    card = rightCard,
                )
            }
        }
        return state.copy(
            gameField = gameField.copy(
                squares = newSquares.toList(),
            )
        )
    }
}
