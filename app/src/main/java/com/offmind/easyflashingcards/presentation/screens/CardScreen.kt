package com.offmind.easyflashingcards.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.presentation.ScreenSettings
import com.offmind.easyflashingcards.presentation.viewmodel.CardViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CardScreen(
    navController: NavController,
    appBarSettings: MutableState<ScreenSettings>,
    paddingValues: PaddingValues,
    viewModel: CardViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()
    appBarSettings.value = ScreenSettings(
        title = state.title,
        showHomeButton = true)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        when (state) {
            is CardViewModel.CardScreenState.ShowCard -> {

            }

            else -> {}
        }
    }
}

@Composable
fun DisplayCardView(card: Card) {
    
}

