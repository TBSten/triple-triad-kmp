package me.tbsten.tripletriad.shared.flux

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

fun <State, Action, SelectedValue> selectedStateFlow(
    store: FluxStore<State, Action>,
    coroutineScope: CoroutineScope,
    sharingStarted: SharingStarted = SharingStarted.WhileSubscribed(),
    selector: (state: State) -> SelectedValue,
) =
    store.stateFlow
        .map(selector)
        .stateIn(
            scope = coroutineScope,
            started = sharingStarted,
            initialValue = selector(store.initialState),
        )

fun <State, Action, SelectedValue> selectedSharedFlow(
    store: FluxStore<State, Action>,
    coroutineScope: CoroutineScope,
    sharingStarted: SharingStarted = SharingStarted.WhileSubscribed(),
    selector: (state: State) -> SelectedValue,
) =
    store.stateFlow
        .map(selector)
        .shareIn(
            scope = coroutineScope,
            started = sharingStarted,
        )
