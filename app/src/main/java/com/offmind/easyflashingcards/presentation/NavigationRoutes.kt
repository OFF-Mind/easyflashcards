package com.offmind.easyflashingcards.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.offmind.easyflashingcards.presentation.screens.CardScreen
import com.offmind.easyflashingcards.presentation.screens.DeckCardsScreen
import com.offmind.easyflashingcards.presentation.screens.DecksScreen
import com.offmind.easyflashingcards.presentation.screens.DisplayHomeScreen
import com.offmind.easyflashingcards.presentation.screens.FlashCardScreen
import com.offmind.easyflashingcards.presentation.screens.ImportCardsScreen

@Composable
fun NavigationComponent(
    navController: NavHostController,
    paddingValues: PaddingValues,
    appBarSettings: MutableState<ScreenSettings>
) {
    NavHost(navController = navController, startDestination = "home") {
        composable(NavigationRoutes.HomeScreenRoute.route) {
            DisplayHomeScreen(
                navController = navController,
                appBarSettings = appBarSettings
            )
        }
        composable(NavigationRoutes.DecksScreenRoute.route) {
            DecksScreen(
                paddingValues = paddingValues,
                navController = navController,
                appBarSettings = appBarSettings
            )
        }
        composable(NavigationRoutes.ImportDecksScreenRoute.route) {
            ImportCardsScreen(
                paddingValues = paddingValues,
                navController = navController,
                appBarSettings = appBarSettings)
        }
        composable(
            NavigationRoutes.CardsListScreen().route,
            arguments = listOf(
                navArgument(NavigationRoutes.CardsListScreen.DECK_ID_KEY) { type = NavType.IntType },
                navArgument(NavigationRoutes.CardsListScreen.DECK_NAME_KEY) { type = NavType.StringType })
        ) {
            DeckCardsScreen(
                navController = navController,
                appBarSettings = appBarSettings,
                paddingValues = paddingValues
            )
        }
        composable(
            NavigationRoutes.CardFlashScreen().route,
            arguments = listOf(
                navArgument(NavigationRoutes.CardFlashScreen.DECK_ID_KEY) {
                    type = NavType.IntType
                }
            )
        ) {
            FlashCardScreen(
                paddingValues = paddingValues,
                appBarSettings = appBarSettings,
                navController = navController
            )
        }
        composable(
            NavigationRoutes.CardScreen().route,
            arguments = listOf(
                navArgument(NavigationRoutes.CardScreen.CARD_ID_KEY) {
                    type = NavType.IntType
                },
                navArgument(NavigationRoutes.CardScreen.DECK_ID_KEY) {
                    type = NavType.IntType
                }
            )) {
            CardScreen(
                navController = navController,
                appBarSettings = appBarSettings,
                paddingValues = paddingValues
            )
        }
    }
}

sealed class NavigationRoutes(val route: String) {
    object HomeScreenRoute: NavigationRoutes("home")
    object DecksScreenRoute: NavigationRoutes("decks")
    object ImportDecksScreenRoute: NavigationRoutes("importDecks")
    class CardsListScreen : NavigationRoutes("cardsList/{${DECK_ID_KEY}}/{${DECK_NAME_KEY}}") {
        companion object {
            const val DECK_ID_KEY = "deckId"
            const val DECK_NAME_KEY = "deckName"
        }
    }
    class CardFlashScreen(val deckId: Int = -1) : NavigationRoutes("cardFlash/{${DECK_ID_KEY}}") {
        companion object {
            const val DECK_ID_KEY = "deckId"
        }

        fun getParametrizedRoute(): String {
            return "${route.split('/').first()}/$deckId"
        }
    }
    class CardScreen(
        val cardId: Int = -1,
        val deckId: Int = -1
    ) : NavigationRoutes("card/{$CARD_ID_KEY}/{$DECK_ID_KEY}") {
        companion object {
            const val CARD_ID_KEY = "cardId"
            const val DECK_ID_KEY = "deckId"
        }

        fun getParametrizedRoute(): String {
            return "${route.split('/').first()}/$cardId/$deckId"
        }
    }
}