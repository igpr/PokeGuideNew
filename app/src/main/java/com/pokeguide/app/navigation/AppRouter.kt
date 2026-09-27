package com.pokeguide.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pokeguide.app.screen.collections.CollectionDetailScreen
import com.pokeguide.app.screen.collections.CollectionsScreen
import com.pokeguide.app.screen.details.DetailsScreen
import com.pokeguide.app.screen.explore.ExploreScreen
import com.pokeguide.app.screen.history.HistoryScreen
import com.pokeguide.app.screen.profiles.ProfilesScreen

object Destination {
    const val EXPLORE = "explore"
    const val DETAILS = "details/{pokemonId}"
    const val PROFILES = "profiles"
    const val COLLECTIONS = "collections"
    const val COLLECTION_DETAIL = "collection/{collectionId}"
    const val HISTORY = "history"

    fun details(id: Int): String = "details/$id"
    fun collection(id: Long): String = "collection/$id"
}

@Composable
fun AppRouter(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destination.EXPLORE) {

        composable(Destination.EXPLORE) {
            ExploreScreen(
                onPokemonClick = { id -> navController.navigate(Destination.details(id)) },
                onOpenProfiles = { navController.navigate(Destination.PROFILES) },
                onOpenCollections = { navController.navigate(Destination.COLLECTIONS) },
                onOpenHistory = { navController.navigate(Destination.HISTORY) }
            )
        }

        composable(
            route = Destination.DETAILS,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) {
            DetailsScreen(onBack = { navController.popBackStack() })
        }

        composable(Destination.PROFILES) {
            ProfilesScreen(onBack = { navController.popBackStack() })
        }

        composable(Destination.COLLECTIONS) {
            CollectionsScreen(
                onBack = { navController.popBackStack() },
                onOpenCollection = { id -> navController.navigate(Destination.collection(id)) }
            )
        }

        composable(
            route = Destination.COLLECTION_DETAIL,
            arguments = listOf(navArgument("collectionId") { type = NavType.LongType })
        ) {
            CollectionDetailScreen(
                onBack = { navController.popBackStack() },
                onPokemonClick = { id -> navController.navigate(Destination.details(id)) }
            )
        }

        composable(Destination.HISTORY) {
            HistoryScreen(
                onBack = { navController.popBackStack() },
                onPokemonClick = { id -> navController.navigate(Destination.details(id)) }
            )
        }
    }
}