package me.tbsten.tripletriad.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun DisableBack() {
    BackHandler { }
}
