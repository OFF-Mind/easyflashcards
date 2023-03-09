package com.offmind.easyflashingcards.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.offmind.easyflashingcards.data.datasource.entity.toCard
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import com.offmind.easyflashingcards.presentation.NavigationRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardFlashViewModel(
    savedStateHandle: SavedStateHandle,
    private val cardsRepository: CardsRepository
) : BaseViewModel() {

    private val deckId: Int = savedStateHandle[NavigationRoutes.CardsListScreen.DECK_ID_KEY] ?: -1

    private val _state: MutableStateFlow<CardFlashState> = MutableStateFlow(CardFlashState.Loading)
    val state: StateFlow<CardFlashState> = _state

    private val possibleCards: MutableList<Card> = mutableListOf()

    init {
        viewModelScope.launch {
            val allCards =
                if (deckId == -1) cardsRepository.getAllCards() else cardsRepository.getCardsForDeck(
                    deckId
                ).map { it.toCard() }
            possibleCards.addAll(allCards.shuffled())
            onNextCard()
        }
    }

    fun onNextCard() {
        if(possibleCards.isNotEmpty()) {
            _state.value = CardFlashState.ShowCardClosed(possibleCards.removeFirst())
        } else {
            _state.value = CardFlashState.NoMoreWords
        }
    }

    fun onOpenCard() {
        if (_state.value is CardFlashState.ShowCardClosed) {
            _state.value =
                CardFlashState.ShowCardOpened((_state.value as CardFlashState.ShowCardClosed).card)
        } else {
            onNextCard()
        }
    }

    sealed class CardFlashState : BaseViewModelState(title = "Flashing") {
        object Loading : CardFlashState()
        class ShowCardClosed(val card: Card) : CardFlashState()
        class ShowCardOpened(val card: Card) : CardFlashState()
        object NoMoreWords: CardFlashState()
    }
}