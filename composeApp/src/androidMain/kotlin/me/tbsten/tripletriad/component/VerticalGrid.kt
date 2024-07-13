package me.tbsten.tripletriad.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VerticalGrid(
    size: Int,
    columns: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
) {
    Column(modifier = modifier) {
        var rows = (size / columns)
        if (size.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (index < size) {
                            content(index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T> VerticalGrid(
    items: List<T>,
    columns: Int,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    VerticalGrid(
        size = items.size,
        columns = columns,
        modifier = modifier,
    ) { index ->
        content(items[index])
    }
}
