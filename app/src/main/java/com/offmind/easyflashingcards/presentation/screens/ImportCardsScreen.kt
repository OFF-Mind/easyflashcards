package com.offmind.easyflashingcards.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.offmind.easyflashingcards.presentation.ScreenSettings
import com.offmind.easyflashingcards.presentation.viewmodel.ImportCardsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportCardsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    appBarSettings: MutableState<ScreenSettings>,
    decksViewModel: ImportCardsViewModel = koinViewModel()
) {

    val state by decksViewModel.state.collectAsState()
    appBarSettings.value = appBarSettings.value.copy(
        title = state.title,
        showHomeButton = true
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        when (state) {
            is ImportCardsViewModel.ImportCardsScreenState.ShowDecks -> {
                Column(modifier = Modifier.padding(10.dp)) {
                    DecksList(
                        (state as ImportCardsViewModel.ImportCardsScreenState.ShowDecks).decks,
                        true, onDeckSelected = {
                            decksViewModel.insertTempCardsToDeck(it)
                            navController.navigate("decks")
                        },
                        onNewDeckSelected = {
                            decksViewModel.createNewDeck(it) { newDeckId ->
                                decksViewModel.insertTempCardsToDeck(newDeckId)
                                navController.navigate("decks")
                            }
                        })
                }
            }

            else -> {}
        }
    }
}

