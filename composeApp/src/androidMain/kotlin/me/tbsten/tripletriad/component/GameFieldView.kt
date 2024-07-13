package me.tbsten.tripletriad.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.tbsten.tripletriad.shared.domain.game.GameField
import me.tbsten.tripletriad.shared.domain.game.GamePlayer
import me.tbsten.tripletriad.shared.domain.game.Square
import me.tbsten.tripletriad.shared.domain.game.SquareOffset
import me.tbsten.tripletriad.ui.PlayerColors
import kotlin.math.max

@Composable
fun GameFieldView(
    gameField: GameField,
    ownerColors: (owner: GamePlayer) -> PlayerColors,
    modifier: Modifier = Modifier,
    clickable: (Square) -> Boolean = { true },
    selectedSquareOffset: SquareOffset?,
    onSquareClick: (SquareOffset, Square) -> Unit = { _, _ -> },
) {
    GameFieldLayout(
        gameField = gameField,
        gap = 2.dp,
        modifier = modifier,
    ) { offset, square ->
        val isSelected = selectedSquareOffset == offset

        SquareView(
            square = square,
            clickable = clickable(square),
            isSelected = isSelected,
            ownerColors = ownerColors,
            onClick = { onSquareClick(offset, square) },
        )
    }
}

@Composable
private fun GameFieldLayout(
    gameField: GameField,
    gap: Dp, // TODO できればArrangementで指定できると綺麗
    modifier: Modifier = Modifier,
    content: @Composable GameField.(SquareOffset, Square) -> Unit,
) {
    Layout(
        measurePolicy = { measurables, constraints ->
            val cellWidths = MutableList(gameField.width) { 0 }
            val cellHeights = MutableList(gameField.width) { 0 }

            val placeables = measurables.chunked(gameField.width).mapIndexed { rowIndex, row ->
                row.mapIndexed { columnIndex, cell ->
                    cell.measure(
                        constraints.copy(
                            minWidth = 0,
                            maxWidth = constraints.maxWidth / gameField.width,
                        )
                    ).also {
                        cellWidths[columnIndex] = max(cellWidths[columnIndex], it.width)
                        cellHeights[rowIndex] = max(cellHeights[rowIndex], it.height)
                    }
                }
            }

            val gapPx = gap.roundToPx()

            val width = cellWidths.sum() + (cellWidths.size - 1) * gapPx
            val height = cellHeights.sum() + (cellHeights.size - 1) * gapPx

            var y = 0
            layout(width, height) {
                placeables.forEachIndexed { rowIndex, row ->
                    var x = 0
                    row.forEachIndexed { columnIndex, cell ->
                        cell.place(x, y)
                        x += cellWidths[columnIndex]
                        x += gapPx
                    }
                    y += cellHeights[rowIndex]
                    y += gapPx
                }
            }
        },
        content = {
            gameField.squares.forEachIndexed { index, square ->
                val x = index % gameField.height
                val y = index / gameField.width
                content(gameField, SquareOffset(x, y), square)
            }
        },
        modifier = modifier,
    )
}
