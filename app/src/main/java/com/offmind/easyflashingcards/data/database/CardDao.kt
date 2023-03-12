package com.offmind.easyflashingcards.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.offmind.easyflashingcards.data.datasource.entity.CardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM card_table")
    fun getAll(): List<CardEntity>

    /*@Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)
*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(card: CardEntity): Long

    @Query("SELECT * FROM card_table WHERE id == :cardId")
    fun getCardById(cardId: Int): CardEntity

    @Query("SELECT * FROM card_table WHERE deck_id == :deckId")
    fun loadCardsInDeck(deckId: Int): List<CardEntity>

    @Query("SELECT * FROM card_table WHERE deck_id == :deckId")
    fun loadCardsInDeckAsFlow(deckId: Int): Flow<List<CardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTempImportCards(cards: List<CardEntity>)

    @Query("delete from card_table where deck_id == -10")
    fun deleteTempCards()

    @Delete
    fun delete(card: CardEntity)
}
