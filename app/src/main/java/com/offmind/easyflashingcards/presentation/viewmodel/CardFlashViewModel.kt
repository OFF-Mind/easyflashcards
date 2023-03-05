package com.offmind.easyflashingcards.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardFlashViewModel(private val cardsRepository: CardsRepository) : BaseViewModel() {

    private val _state: MutableStateFlow<CardFlashState> = MutableStateFlow(CardFlashState.Loading)
    val state: StateFlow<CardFlashState> = _state

    private val possibleCards: MutableList<Card> = mutableListOf()

    init {
        viewModelScope.launch {
            possibleCards.addAll(cardsRepository.getAllCards().shuffled())
            onNextCard()
        }
    }

    fun onNextCard() {
        _state.value = CardFlashState.ShowCardClosed(possibleCards.removeFirst())
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
    }
}