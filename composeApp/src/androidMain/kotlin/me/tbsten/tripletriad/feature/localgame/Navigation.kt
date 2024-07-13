package me.tbsten.tripletriad.feature.localgame

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import me.tbsten.tripletriad.feature.localgame.play.LocalGamePlayScreen
import me.tbsten.tripletriad.feature.localgame.play.LocalGameViewModel
import me.tbsten.tripletriad.feature.localgame.result.LocalGameResultScreen
import me.tbsten.tripletriad.navigation.NavGraph
import me.tbsten.tripletriad.navigation.Screen
import me.tbsten.tripletriad.navigation.composable
import me.tbsten.tripletriad.navigation.dialog
import me.tbsten.tripletriad.navigation.navigate
import me.tbsten.tripletriad.navigation.navigation
import me.tbsten.tripletriad.shared.domain.game.Card
import me.tbsten.tripletriad.shared.domain.game.CardNumber
import me.tbsten.tripletriad.shared.domain.game.GameField
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.domain.game.Square
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState.PreStart
import me.tbsten.tripletriad.shared.store.localgame.LocalGameStore

data object LocalGame : NavGraph {
    override val route = "localGame"

    override val start: Screen = Play

    data object Play : Screen {
        override val route = "${LocalGame.route}/play"
    }

    data object Result : Screen {
        override val route = "${LocalGame.route}/result?mePoint={mePoint}&enemyPoint={enemyPoint}"
        override val navArguments = listOf(
            navArgument("mePoint") { type = NavType.IntType },
            navArgument("enemyPoint") { type = NavType.IntType },
        )

        fun route(mePoint: Int, enemyPoint: Int) =
            route
                .replace("{mePoint}", "$mePoint")
                .replace("{enemyPoint}", "$enemyPoint")
    }
}

fun NavGraphBuilder.localGame(
    navController: NavController,
) {
    navigation(LocalGame) {
        composable(LocalGame.Play) {
            val localGameViewModel = localGameViewModel()

            LocalGamePlayScreen(
                localGameViewModel = localGameViewModel,
                onFinish = { mePoint, enemyPoint ->
                    navController.navigate(
                        LocalGame.Result.route(mePoint, enemyPoint)
                    )
                },
            )
        }

        dialog(
            screen = LocalGame.Result,
            dialogProperties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            ),
        ) {
            val mePoint = it.arguments?.getInt("mePoint")
                ?: throw IllegalArgumentException("invalid navArgument: myPoint")
            val enemyPoint = it.arguments?.getInt("enemyPoint")
                ?: throw IllegalArgumentException("invalid navArgument: enemyPoint")

            LocalGameResultScreen(
                mePoint = mePoint,
                enemyPoint = enemyPoint,
                onRetry = {
                    navController.navigate(
                        LocalGame,
                        navOptions = navOptions {
                            popUpTo(LocalGame.route) { inclusive = true }
                        },
                    )
                },
            )
        }
    }
}

@Composable
private fun localGameViewModel(): LocalGameViewModel =
    viewModel(
        factory = viewModelFactory {
            initializer {
                LocalGameViewModel(
                    // TODO DI
                    // ViewModel Scoped LocalGameStore
                    localGameStore = LocalGameStore(
                        initialState = initialLocalGameState(),
                        localRules = listOf(),
                    ),
                )
            }
        },
    )

private fun initialLocalGameState(): LocalGameState {
    // 仮初めの自分
    val sampleMe = GamePlayer(
        id = GamePlayer.Id("player-1"),
        name = "tbsten",
        hands = listOf(
            Card(
                id = Card.Id("player-card-1"),
                top = CardNumber.Number(2),
                right = CardNumber.Number(2),
                bottom = CardNumber.Number(5),
                left = CardNumber.Number(2),
            ),
            Card(
                id = Card.Id("player-card-2"),
                top = CardNumber.Number(1),
                right = CardNumber.Number(1),
                bottom = CardNumber.Number(1),
                left = CardNumber.Number(9),
            ),
            Card(
                id = Card.Id("player-card-3"),
                top = CardNumber.Number(5),
                right = CardNumber.Number(3),
                bottom = CardNumber.Number(2),
                left = CardNumber.Number(1),
            ),
            Card(
                id = Card.Id("player-card-4"),
                top = CardNumber.Number(6),
                right = CardNumber.Number(1),
                bottom = CardNumber.Number(1),
                left = CardNumber.Number(6),
            ),
            Card(
                id = Card.Id("player-card-5"),
                top = CardNumber.Number(1),
                right = CardNumber.Number(4),
                bottom = CardNumber.Number(3),
                left = CardNumber.Number(2),
            ),
        ),
    )

    // 本当の敵は自分自身
    val sampleEnemy = GamePlayer(
        id = GamePlayer.Id("player-2"),
        name = "enemy",
        hands = listOf(
            Card(
                id = Card.Id("enemy-card-1"),
                top = CardNumber.Number(1),
                right = CardNumber.Number(2),
                bottom = CardNumber.Number(3),
                left = CardNumber.Number(4),
            ),
            Card(
                id = Card.Id("enemy-card-2"),
                top = CardNumber.Number(5),
                right = CardNumber.Number(5),
                bottom = CardNumber.Number(2),
                left = CardNumber.Number(2),
            ),
            Card(
                id = Card.Id("enemy-card-3"),
                top = CardNumber.Number(4),
                right = CardNumber.Number(4),
                bottom = CardNumber.Number(1),
                left = CardNumber.Number(1),
            ),
            Card(
                id = Card.Id("enemy-card-4"),
                top = CardNumber.Number(9),
                right = CardNumber.Number(1),
                bottom = CardNumber.Number(1),
                left = CardNumber.Number(1),
            ),
            Card(
                id = Card.Id("enemy-card-5"),
                top = CardNumber.Number(3),
                right = CardNumber.Number(3),
                bottom = CardNumber.Number(3),
                left = CardNumber.Number(3),
            ),
        ),
    )

    val width = 3
    val height = 3
    val gameField = GameField(
        squares = List<Square>(width * height) { Square.Open() },
        width = width,
        height = height,
    )
    return PreStart(
        me = sampleMe,
        enemy = sampleEnemy,
        gameField = gameField,
    )
}