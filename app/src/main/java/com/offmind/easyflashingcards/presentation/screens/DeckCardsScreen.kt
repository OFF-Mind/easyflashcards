package com.offmind.easyflashingcards.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.offmind.easyflashingcards.R
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.presentation.NavigationRoutes
import com.offmind.easyflashingcards.presentation.ScreenSettings
import com.offmind.easyflashingcards.presentation.viewmodel.CardsListViewModel
import com.offmind.easyflashingcards.presentation.views.CardButton
import com.offmind.easyflashingcards.presentation.views.CardButtonSize
import com.offmind.easyflashingcards.presentation.views.SearchField
import com.offmind.easyflashingcards.presentation.views.WordListItem
import com.offmind.easyflashingcards.ui.theme.DividerColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeckCardsScreen(
    navController: NavController,
    appBarSettings: MutableState<ScreenSettings>,
    paddingValues: PaddingValues,
    viewModel: CardsListViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()
    appBarSettings.value = ScreenSettings(
        title = state.title,
        showHomeButton = true,
        fabButtonClick = {

        })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        when (state) {
            is CardsListViewModel.CardsListState.CardsList -> {
                DisplayCardsList(
                    cards = (state as CardsListViewModel.CardsListState.CardsList).cards,
                    queryString = (state as CardsListViewModel.CardsListState.CardsList).queryString,
                    onDeckAction = {
                        when(it) {
                            DeckAction.START -> {
                                navController.navigate(
                                    NavigationRoutes.CardFlashScreen(
                                        (state as CardsListViewModel.CardsListState.CardsList).deckId
                                    ).getParametrizedRoute()
                                )
                            }
                            else -> {

                            }
                        }
                    })
                {
                    viewModel.filterCards(it)
                }
            }

            else -> {}
        }
    }
}

@Composable
fun DisplayCardsList(
    cards: List<Card>,
    queryString: String,
    onDeckAction: (action: DeckAction) -> Unit,
    onFilterCards: (filterString: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CardButton(
                size = CardButtonSize.TINY,
                icon = R.drawable.start_icon,
                text = "Start deck"
            ) {
                onDeckAction.invoke(DeckAction.START)
            }
            CardButton(
                size = CardButtonSize.TINY,
                icon = R.drawable.statistics_icon,
                text = "Statistics"
            ) {
                //todo
            }
            CardButton(
                size = CardButtonSize.TINY,
                icon = R.drawable.settings_icon,
                text = "Settings"
            ) {
                //todo
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Divider(
            color = DividerColor,
            thickness = 0.5.dp,
            modifier = Modifier.padding(horizontal = 50.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        SearchField(
            modifier = Modifier
                .padding(10.dp)
                .height(50.dp),
            hint = "Search cards"
        ) { newSearchQuery ->
            onFilterCards.invoke(newSearchQuery)
        }

        if (cards.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No cards found",
                    textAlign = TextAlign.Center,
                    color = DividerColor,
                    fontSize = 20.sp
                )
            }
        } else {
            LazyColumn(content = {
                cards.forEach {
                    item {
                        WordListItem(card = it, highlitedText = queryString)
                    }
                }
            })
        }
    }
}

enum class DeckAction {
    START,
    STATS,
    SETTINGS
}


