package me.tbsten.tripletriad.feature.localgame.play

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import me.tbsten.tripletriad.R
import me.tbsten.tripletriad.feature.localgame.play.component.EnemyPlayerSection
import me.tbsten.tripletriad.feature.localgame.play.component.GameFieldSection
import me.tbsten.tripletriad.feature.localgame.play.component.MePlayerSection
import me.tbsten.tripletriad.navigation.DisableBack
import me.tbsten.tripletriad.shared.store.localgame.LocalGameAction
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState
import me.tbsten.tripletriad.util.CollectAsEffect

@Composable
internal fun LocalGamePlayScreen(
    localGameViewModel: LocalGameViewModel,
    onFinish: (mePoint: Int, enemyPoint: Int) -> Unit,
) {
    val state by localGameViewModel.state.collectAsState()

    DisableBack()

    localGameViewModel.finishFlow.CollectAsEffect {
        delay(1_000)
        onFinish(it.getPoint(it.me), it.getPoint(it.enemy))
    }

//    val context = LocalContext.current
//    localGameViewModel.applyingRulesHistoryFlow.CollectAsEffect {
//        Toast.makeText(context, "TODO: apply rules animation", Toast.LENGTH_SHORT).show()
//    }

    LocalGamePlayScreen(
        state = state,
        dispatch = localGameViewModel::dispatch,
    )
}

@Composable
private fun LocalGamePlayScreen(
    state: LocalGameState,
    dispatch: (LocalGameAction) -> Unit,
) {
    LocalGameScreenScaffold(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            EnemyPlayerSection(
                state = state,
                onCardSelect = {
                    dispatch(LocalGameAction.SelectCard(it))
                },
            )
            GameFieldSection(
                state = state,
                selectedSquareOffset = if (state is LocalGameState.WithTurnPlayer) state.selectedSquareOffset else null,
                onSquareSelect = { offset, _ ->
                    dispatch(LocalGameAction.SelectSquare(offset))
                },
                modifier = Modifier
                    .weight(1f)
//                    .background(
//                        when (state) {
//                            is LocalGameState.PreStart -> Color.Red
//                            is LocalGameState.WithTurnPlayer ->
//                                if (state.me.isTurnPlayer(state)) Color.Blue
//                                else Color.Yellow
//
//                            is LocalGameState.Finished -> Color.White
//                        }.copy(alpha = 0.3f)
//                    ),
            )
            MePlayerSection(
                state = state,
                onCardSelect = {
                    dispatch(LocalGameAction.SelectCard(it))
                },
            )
        }
    }
}

@Composable
private fun LocalGameScreenScaffold(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_game),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
        )
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.4f))
                .matchParentSize()
        )
        Box(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)) {
            content()
        }
    }
}
