package com.offmind.easyflashingcards.domain.repository

import com.offmind.easyflashingcards.data.datasource.entity.CardEntity
import com.offmind.easyflashingcards.data.datasource.entity.DeckEntity
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.model.Deck

interface CardsRepository {

    suspend fun getCardsForDeck(deckId: Int): List<CardEntity>

    suspend fun getAllCards(): List<Card>

    suspend fun getAllDecks(): List<Deck>

    suspend fun createNewDeck(deckName: String): Int

    suspend fun getDeck(deckId: Int): DeckEntity

    suspend fun writeCards(cards: List<CardEntity>)

    suspend fun clearTempDeck()
}