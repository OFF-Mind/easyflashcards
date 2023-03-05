package com.offmind.easyflashingcards.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.offmind.easyflashingcards.domain.model.Deck
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ImportCardsViewModel(
    private val cardsRepository: CardsRepository
) : BaseViewModel() {

    private val _state: MutableStateFlow<ImportCardsScreenState> =
        MutableStateFlow(ImportCardsScreenState.Empty)
    val state: StateFlow<ImportCardsScreenState> = _state

    init {
        loadDecks()
    }

    fun loadDecks() {
        viewModelScope.launch {
            _state.value = ImportCardsScreenState.ShowDecks(cardsRepository.getAllDecks())
        }
    }

    fun insertTempCardsToDeck(toDeckId: Int) {
        viewModelScope.launch {
            cardsRepository.writeCards(
                cardsRepository.getCardsForDeck(-10).map { it.copy(deckId = toDeckId) }
            )
            cardsRepository.clearTempDeck()
        }
    }

    fun createNewDeck(newDeckName: String, onCompelete: (newDeckId: Int) -> Unit) {
        viewModelScope.launch {
            val newDeckId = cardsRepository.createNewDeck(newDeckName)
            withContext(Dispatchers.Main) {
                onCompelete.invoke(newDeckId)
            }
        }
    }

    sealed class ImportCardsScreenState : BaseViewModelState(title = "Import Cards") {
        object Empty : ImportCardsScreenState()
        class ShowDecks(val decks: List<Deck>) : ImportCardsScreenState()
    }
}