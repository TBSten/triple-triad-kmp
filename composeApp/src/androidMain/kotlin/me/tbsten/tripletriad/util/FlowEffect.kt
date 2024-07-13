package me.tbsten.tripletriad.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> Flow<T>.CollectAsEffect(effect: suspend (T) -> Unit) {
    LaunchedEffect(Unit) {
        collect(effect)
    }
}
