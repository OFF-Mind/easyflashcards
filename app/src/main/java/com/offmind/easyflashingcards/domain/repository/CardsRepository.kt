package com.offmind.easyflashingcards.domain.repository

import com.offmind.easyflashingcards.data.datasource.entity.CardEntity
import com.offmind.easyflashingcards.data.datasource.entity.DeckEntity
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.model.Deck
import kotlinx.coroutines.flow.Flow

interface CardsRepository {

    suspend fun getCardsForDeck(deckId: Int): List<CardEntity>

    suspend fun getAllCards(): List<Card>

    suspend fun getAllDecks(): List<Deck>

    suspend fun createNewDeck(deckName: String): Int

    suspend fun getDeck(deckId: Int): DeckEntity

    suspend fun getCard(cardId: Int): CardEntity

    suspend fun writeCards(cards: List<CardEntity>)

    suspend fun saveCard(card: Card): Int

    suspend fun createNewCard(deckId: Int): Int

    suspend fun clearTempDeck()

    suspend fun deleteCard(cardId: Int)

    fun obtainCardsFlow(deckId: Int): Flow<List<CardEntity>>
}