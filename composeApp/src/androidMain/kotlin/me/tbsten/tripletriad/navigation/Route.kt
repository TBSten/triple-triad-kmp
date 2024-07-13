package me.tbsten.tripletriad.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation

interface Screen {
    val route: String
    val navArguments: List<NamedNavArgument>
        get() = listOf()
}

interface NavGraph {
    val route: String
    val navArguments: List<NamedNavArgument>
        get() = listOf()
    val start: Screen
}

fun NavGraphBuilder.composable(
    screen: Screen,
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(
    route = screen.route,
    arguments = screen.navArguments,
    deepLinks = deepLinks,
    enterTransition = enterTransition,
    exitTransition = exitTransition,
    popEnterTransition = popEnterTransition,
    popExitTransition = popExitTransition,
    content = content,
)

fun NavGraphBuilder.dialog(
    screen: Screen,
    deepLinks: List<NavDeepLink> = emptyList(),
    dialogProperties: DialogProperties = DialogProperties(),
    content: @Composable (NavBackStackEntry) -> Unit,
) = dialog(
    route = screen.route,
    arguments = screen.navArguments,
    deepLinks = deepLinks,
    dialogProperties = dialogProperties,
    content = content,
)

fun NavGraphBuilder.navigation(
    navGraph: NavGraph,
    deepLinks: List<NavDeepLink> = emptyList(),
    builder: NavGraphBuilder.() -> Unit
) = navigation(
    route = navGraph.route,
    arguments = navGraph.navArguments,
    startDestination = navGraph.start.route,
    deepLinks = deepLinks,
    builder = builder,
)

fun NavController.navigate(
    screen: Screen,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    navigate(
        route = screen.route,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )
}

fun NavController.navigate(
    navGraph: NavGraph,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    navigate(
        route = navGraph.route,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )
}
