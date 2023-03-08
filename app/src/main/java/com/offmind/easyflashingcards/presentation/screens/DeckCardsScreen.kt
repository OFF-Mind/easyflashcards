package com.offmind.easyflashingcards.presentation.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.offmind.easyflashingcards.R
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.presentation.AppBarSettings
import com.offmind.easyflashingcards.presentation.viewmodel.CardsListViewModel
import com.offmind.easyflashingcards.presentation.views.CardButton
import com.offmind.easyflashingcards.presentation.views.CardButtonSize
import com.offmind.easyflashingcards.presentation.views.SearchField
import com.offmind.easyflashingcards.ui.theme.DividerColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeckCardsScreen(
    navController: NavController,
    appBarSettings: MutableState<AppBarSettings>,
    paddingValues: PaddingValues,
    viewModel: CardsListViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()
    appBarSettings.value = appBarSettings.value.copy(title = state.title, showHomeButton = true)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        when (state) {
            is CardsListViewModel.CardsListState.CardsList -> {
                DisplayCardsList(cards = (state as CardsListViewModel.CardsListState.CardsList).cards)
                {
                    viewModel.filterCards(it)
                }

            }

            else -> {}
        }
    }
}

@Composable
fun DisplayCardsList(cards: List<Card>, onFilterCards: (filterString: String) -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CardButton(
                size = CardButtonSize.TINY,
                icon = R.drawable.start_icon,
                text = "Start deck"
            ) {
                //todo
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
        Divider(color = DividerColor, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 50.dp))
        Spacer(modifier = Modifier.height(20.dp))

        SearchField(
            modifier = Modifier
                .padding(10.dp)
                .height(50.dp)
        ) { newSearchQuery ->
            onFilterCards.invoke(newSearchQuery)
        }

        LazyColumn(content = {
            cards.forEach {
                item {
                    Surface(
                        shadowElevation = 5.dp,
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .padding(10.dp)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = it.frontSide,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = it.backSide,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .background(Color.LightGray),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        })
    }
}

