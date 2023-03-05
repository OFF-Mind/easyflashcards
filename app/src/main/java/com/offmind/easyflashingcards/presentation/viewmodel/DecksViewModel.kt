package com.offmind.easyflashingcards.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.offmind.easyflashingcards.domain.model.Deck
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DecksViewModel(
    private val cardsRepository: CardsRepository) : BaseViewModel() {

    private val _state: MutableStateFlow<DeckScreenState> = MutableStateFlow(DeckScreenState.Empty)
    val state: StateFlow<DeckScreenState> = _state

    init {
        loadDecks()
    }

    fun loadDecks() {
        viewModelScope.launch {
            println("loadDecks, viewmodel = ${hashCode()}")
            _state.value = DeckScreenState.ShowDecks(cardsRepository.getAllDecks())

            repeat(100) {
                delay(500)
                println("Pizda: ${hashCode()}")
            }
        }
    }

    fun createNewDeck(newDeckName: String) {
        viewModelScope.launch {
            cardsRepository.createNewDeck(newDeckName)
            loadDecks()
        }
    }

    sealed class DeckScreenState : BaseViewModelState(title = "Decks") {
        object Empty : DeckScreenState()
        class ShowDecks(val decks: List<Deck>) : DeckScreenState()
    }
}