package com.offmind.easyflashingcards.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.offmind.easyflashingcards.data.datasource.entity.DeckEntity

@Dao
interface DeckDao {
    @Query("SELECT * FROM deck_table")
    fun getAll(): List<DeckEntity>

    /*@Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deck: DeckEntity): Long

    @Delete
    fun delete(deck: DeckEntity)
}
