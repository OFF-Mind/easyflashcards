package com.offmind.easyflashingcards.presentation

sealed class NavigationRoutes(val route: String) {
    object HomeScreenRoute: NavigationRoutes("home")
    object DecksScreenRoute: NavigationRoutes("decks")
    object ImportDecksScreenRoute: NavigationRoutes("importDecks")
    object CardsListScreen: NavigationRoutes("cardsList/{deckId}")
    object CardFlashScreen: NavigationRoutes("cardFlash")
}