package com.offmind.easyflashingcards.presentation

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
}