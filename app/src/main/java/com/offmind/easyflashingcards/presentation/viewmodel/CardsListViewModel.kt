package com.offmind.easyflashingcards.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.offmind.easyflashingcards.data.datasource.entity.CardEntity
import com.offmind.easyflashingcards.data.datasource.entity.toCard
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import com.offmind.easyflashingcards.presentation.NavigationRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale

class CardsListViewModel(
    savedStateHandle: SavedStateHandle,
    private val cardsRepository: CardsRepository
) : BaseViewModel() {

    private var deckName: String =
        checkNotNull(savedStateHandle[NavigationRoutes.CardsListScreen.DECK_NAME_KEY])
    private val deckId: Int =
        checkNotNull(savedStateHandle[NavigationRoutes.CardsListScreen.DECK_ID_KEY])

    private val _state: MutableStateFlow<CardsListState> =
        MutableStateFlow(CardsListState.Loading(deckName = deckName, deckId = deckId))
    val state: StateFlow<CardsListState> = _state

    private var allDeckCards: List<CardEntity> = emptyList()

    init {
        fetchCards(deckId)
    }

    fun filterCards(filter: String) {
        val displayCards = if (filter.isNotEmpty()) {
            allDeckCards.filter { item ->
                item.backSide.lowercase(Locale.getDefault())
                    .contains(filter) || item.frontSide.lowercase(Locale.getDefault()).contains(
                    filter.lowercase(Locale.getDefault())
                )
            }.map { it.toCard() }
        } else {
            allDeckCards.map { it.toCard() }
        }

        _state.value = CardsListState.CardsList(
            cards = displayCards,
            deckId = deckId,
            deckName = deckName,
            queryString = filter
        )
    }

    private fun fetchCards(deckId: Int) {
        viewModelScope.launch {
            cardsRepository.obtainCardsFlow(deckId).onEach {
                allDeckCards = it
                filterCards("")
            }.collect()
        }
    }

    sealed class CardsListState(val deckId: Int, override val title: String = "") :
        BaseViewModelState(title = title) {
        class Loading(deckName: String, deckId: Int) : CardsListState(title = deckName, deckId = deckId)
        object Empty : CardsListState(deckId = -1)
        class CardsList(
            deckId: Int,
            deckName: String,
            val cards: List<Card>,
            val queryString: String = ""
        ) : CardsListState(title = deckName, deckId = deckId)
    }
}