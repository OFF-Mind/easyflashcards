package com.offmind.easyflashingcards.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.offmind.easyflashingcards.data.datasource.entity.toCard
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import com.offmind.easyflashingcards.presentation.NavigationRoutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel(
    savedStateHandle: SavedStateHandle,
    private val cardsRepository: CardsRepository
) : BaseViewModel() {

    private val cardId: Int = savedStateHandle[NavigationRoutes.CardScreen.CARD_ID_KEY] ?: -1
    private val deckId: Int = savedStateHandle[NavigationRoutes.CardScreen.DECK_ID_KEY] ?: -1

    private val _state: MutableStateFlow<CardScreenState> = MutableStateFlow(CardScreenState.Loading)
    val state: StateFlow<CardScreenState> = _state

    init {
        viewModelScope.launch {
            //stupid workaround for compose navigation,
            // but this code starts working while previous screen still shown:
            delay(200)
            val card = if (cardId >= 0) {
                cardsRepository.getCard(cardId).toCard()
            } else {
                cardsRepository.getCard(cardsRepository.createNewCard(deckId)).toCard()
            }
            _state.value =
                CardScreenState.ShowCard(card = card)
        }
    }

    sealed class CardScreenState : BaseViewModelState(title = "Edit Card") {
        object Loading : CardScreenState()
        class ShowCard(val card: Card) : CardScreenState()
    }
}