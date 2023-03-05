package com.offmind.easyflashingcards.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.offmind.easyflashingcards.data.datasource.entity.toCard
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardsListViewModel(
    savedStateHandle: SavedStateHandle,
    private val cardsRepository: CardsRepository) : BaseViewModel() {

    private val _state: MutableStateFlow<CardsListState> = MutableStateFlow(CardsListState.Loading)
    val state: StateFlow<CardsListState> = _state

    init {
        val deckId: Int = checkNotNull(savedStateHandle["deckId"])
        println("deckId = ${deckId}")
        fetchCards(deckId)
    }

    fun fetchCards(deckId: Int) {
        viewModelScope.launch {
            _state.value = CardsListState.CardsList(
                cards = cardsRepository.getCardsForDeck(deckId).map { it.toCard() })
        }
    }

    sealed class CardsListState(override val title: String = "") : BaseViewModelState(title = title) {
        object Loading : CardsListState()
        object Empty : CardsListState()
        class CardsList(val cards: List<Card>) : CardsListState()
    }
}