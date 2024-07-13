package me.tbsten.tripletriad.shared.flux


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class FluxStore<State, Action>(
    val initialState: State,
) {
    private val _stateFlow = MutableStateFlow(initialState)
    val stateFlow = _stateFlow.asStateFlow()

    abstract fun reducer(prevState: State, action: Action): State
    open fun dispatch(action: Action) {
        _stateFlow.update { reducer(it, action) }
    }
}
