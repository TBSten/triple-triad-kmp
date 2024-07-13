package me.tbsten.tripletriad.feature.localgame.play

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import me.tbsten.tripletriad.shared.store.localgame.LocalGameAction
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState
import me.tbsten.tripletriad.shared.store.localgame.LocalGameStore
import me.tbsten.tripletriad.shared.store.localgame.controllable.RandomController

class LocalGameViewModel(
    private val localGameStore: LocalGameStore,
) : ViewModel() {
    val state: StateFlow<LocalGameState> = localGameStore.stateFlow

    val finishFlow =
        localGameStore.stateFlow
            .filterIsInstance<LocalGameState.Finished>()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
            )

    val applyingRulesHistoryFlow =
        localGameStore.stateFlow
            .filterIsInstance<LocalGameState.WithTurnPlayer>()
            .filter {
                it.applyingRulesHistory != null
            }
            .map {
                it.applyingRulesHistory
            }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
            )

    private val enemyControl = RandomController(state.value.enemy.id)

    init {
        viewModelScope.launch {
            enemyControl.collectStore(localGameStore)
        }
        viewModelScope.launch {
            state.collect {
//                if (it is LocalGameState.WithTurnPlayer) {
//                    Log.d(
//                        "state-chage",
//                        "WithTurnPlayer: selectedSquareOffset:${it.selectedSquareOffset}"
//                    )
//                }
//                println("app-debug: change-state: $it")
                if (it is LocalGameState.WithTurnPlayer)
                    println("app-debug: selected-square: ${it.selectedSquareOffset}")
            }
        }
        // TODO mock animations, must delete
        // 先行プレイヤーを決めるアニメーションのモック
        viewModelScope.launch {
            localGameStore.stateFlow
                .filterIsInstance<LocalGameState.PreStart>()
                .collect {
                    delay(2_000)
                    dispatch(LocalGameAction.StartTurn(state.value.me))
                }
        }
        // カードが置かれた時のアニメーションのモック
        viewModelScope.launch {
            localGameStore.stateFlow
                .filterIsInstance<LocalGameState.WithTurnPlayer>()
                .collect {
                    val selectedCard = it.selectedCard
                    val selectedSquareOffset = it.selectedSquareOffset
                    if (selectedCard != null && selectedSquareOffset != null) {
                        delay(1_000)
                        dispatch(
                            LocalGameAction.PlacedCard(
                                player = it.turnPlayer,
                                selectedCard = selectedCard,
                                selectedSquareOffset = selectedSquareOffset,
                            ),
                        )
                    }
                }
        }
        // applyingRulesHistoryのアニメーションのモック
        viewModelScope.launch {
            localGameStore.stateFlow
                .filterIsInstance<LocalGameState.WithTurnPlayer>()
                .collect {
                    val selectedCard = it.selectedCard
                    val selectedSquareOffset = it.selectedSquareOffset
                    val applyingRulesHistory = it.applyingRulesHistory
                    if (
                        selectedCard == null && selectedSquareOffset == null &&
                        applyingRulesHistory != null
                    ) {
                        Log.d(
                            "applyingRulesHistory-animation-mock",
                            "applyingRulesHistory:$applyingRulesHistory",
                        )
                        delay(2_000)
                        dispatch(
                            LocalGameAction.ProcessedByLocalRule(
                                afterState = applyingRulesHistory.last(),
                            )
                        )
                    }
                }
        }
    }

    fun dispatch(action: LocalGameAction) =
        localGameStore.dispatch(action)
}
