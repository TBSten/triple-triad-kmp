package me.tbsten.tripletriad.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.store.localgame.LocalGameState

object TripleTriadTheme {
    val meColors =
        PlayerColors(
            background = Color(0xFF618AB9),
            contentBorder = Color(0xFF000000),
            content = Color(0xFFFFFFFF),
            sectionBorder = listOf(
                Color(0x00020251),
                Color(0xFF0E0EC7),
            ),
            pointShadow = Color(0xFF0000FF),
        )
    val enemyColors =
        PlayerColors(
            background = Color(0xFFB97777),
            contentBorder = Color(0xFF000000),
            content = Color(0xFFFFFFFF),
            sectionBorder = listOf(
                Color(0x00270000),
                Color(0xFF8D0000),
            ),
            pointShadow = Color(0xFFFF0000),
        )

    fun playerColor(state: LocalGameState, player: GamePlayer): PlayerColors =
        when (player) {
            state.me -> meColors
            state.enemy -> enemyColors
            else -> TODO("invalid player")
        }
}

@Stable
data class PlayerColors(
    val background: Color,
    val content: Color,
    val contentBorder: Color,
    val sectionBorder: List<Color>,
    val pointShadow: Color,
)
