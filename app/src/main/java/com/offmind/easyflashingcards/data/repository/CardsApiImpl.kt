package com.offmind.easyflashingcards.data.repository

import com.offmind.easyflashingcards.data.datasource.entity.CardEntity
import com.offmind.easyflashingcards.data.datasource.entity.DeckEntity
import com.offmind.easyflashingcards.data.datasource.entity.toCard
import com.offmind.easyflashingcards.data.datasource.entity.toDeck
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.model.Deck
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CardsApiImpl(
    private val datasource: CardDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : CardsRepository {

    override suspend fun getAllCards(): List<Card> =
        withContext(ioDispatcher) {
            println("getAllCards: ${datasource.cardDao().getAll()}")
            datasource.cardDao().getAll().map { it.toCard() }
        }

    override suspend fun getAllDecks(): List<Deck> =
        withContext(ioDispatcher) {
            datasource.deckDao().getAll().map { it.toDeck() }
        }

    override suspend fun createNewDeck(deckName: String): Int =
        withContext(ioDispatcher) {
            val result = datasource.deckDao().insert(DeckEntity(deckName = deckName))
            println("createNewDeck $result")
            result.toInt()
        }

    override suspend fun writeCards(cards: List<CardEntity>) =
        withContext(ioDispatcher) {
            datasource.cardDao().insertTempImportCards(cards)
            println("writeCards $cards")
        }

    override suspend fun clearTempDeck() =
        withContext(ioDispatcher) {
            datasource.cardDao().deleteTempCards()
        }

    override suspend fun getCardsForDeck(deckId: Int): List<CardEntity> =
        withContext(ioDispatcher) {
            val result = datasource.cardDao().loadCardsInDeck(deckId)
            println("getCardsForDeck: $result")
            result
        }

    override suspend fun getDeck(deckId: Int): DeckEntity =
        withContext(ioDispatcher) {
            datasource.deckDao().getDeckById(deckId)
        }
}