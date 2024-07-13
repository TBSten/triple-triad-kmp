package me.tbsten.tripletriad.feature.localgame.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.tbsten.tripletriad.feature.localgame.result.component.DrawSection
import me.tbsten.tripletriad.feature.localgame.result.component.LoseSection
import me.tbsten.tripletriad.feature.localgame.result.component.WinSection
import me.tbsten.tripletriad.navigation.DisableBack
import me.tbsten.tripletriad.ui.TripleTriadTheme

@Composable
fun LocalGameResultScreen(
    mePoint: Int,
    enemyPoint: Int,
    onRetry: () -> Unit,
) {
    DisableBack()

    Column {
        when {
            mePoint > enemyPoint -> WinSection()
            mePoint < enemyPoint -> LoseSection()
            else -> DrawSection()
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = TripleTriadTheme.meColors.background,
                contentColor = TripleTriadTheme.meColors.content,
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("もう一回 遊ぶ")
        }
    }
}
