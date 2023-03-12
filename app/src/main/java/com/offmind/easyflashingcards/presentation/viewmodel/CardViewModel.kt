package com.offmind.easyflashingcards.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.offmind.easyflashingcards.data.datasource.entity.toCard
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import com.offmind.easyflashingcards.presentation.NavigationRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardViewModel(
    savedStateHandle: SavedStateHandle,
    private val cardsRepository: CardsRepository
) : BaseViewModel() {

    private val cardId: Int = savedStateHandle[NavigationRoutes.CardScreen.CARD_ID_KEY] ?: -1
    private val deckId: Int = savedStateHandle[NavigationRoutes.CardScreen.DECK_ID_KEY] ?: -1

    private val _state: MutableStateFlow<CardScreenState> =
        MutableStateFlow(CardScreenState.Loading)
    val state: StateFlow<CardScreenState> = _state

    private lateinit var editingCard: Card
    private lateinit var startCard: Card

    init {
        viewModelScope.launch {
            //stupid workaround for compose navigation,
            // but this code starts working while previous screen still shown:
            delay(200)
            editingCard = if (cardId >= 0) {
                cardsRepository.getCard(cardId).toCard()
            } else {
                cardsRepository.getCard(cardsRepository.createNewCard(deckId)).toCard()
            }
            startCard = editingCard

            editingCard.let {
                _state.value =
                    CardScreenState.ShowCard(card = it)
            }
        }
    }

    fun doneEditing(): Boolean {
        return editingCard == startCard
    }

    fun deleteCard(onDeleted: () -> Unit) {
        viewModelScope.launch {
            cardsRepository.deleteCard(cardId = editingCard.id)
            onDeleted.invoke()
        }
    }

    fun saveCard(onSaved: () -> Unit) {
        editingCard.let {
            viewModelScope.launch {
                cardsRepository.saveCard(it)
                startCard = editingCard
                withContext(Dispatchers.Main) {
                    onSaved.invoke()
                }
            }
        }
    }

    fun updateCard(newCard: Card): Boolean {
        editingCard = newCard
        _state.value =
            CardScreenState.ShowCard(card = editingCard)
        return editingCard != startCard
    }

    sealed class CardScreenState : BaseViewModelState(title = "Edit Card") {
        object Loading : CardScreenState()
        class ShowCard(val card: Card) : CardScreenState()
    }
}