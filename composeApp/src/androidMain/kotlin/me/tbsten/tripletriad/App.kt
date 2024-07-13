import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import me.tbsten.tripletriad.feature.localgame.LocalGame
import me.tbsten.tripletriad.feature.localgame.localGame
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        AppNavigation()
    }
}

@Composable
private fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LocalGame.route) {
        localGame(
            navController = navController,
        )
    }
}
