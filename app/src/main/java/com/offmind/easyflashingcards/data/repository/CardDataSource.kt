package com.offmind.easyflashingcards.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.offmind.easyflashingcards.data.database.CardDao
import com.offmind.easyflashingcards.data.database.DeckDao
import com.offmind.easyflashingcards.data.datasource.entity.CardEntity
import com.offmind.easyflashingcards.data.datasource.entity.DeckEntity
import com.offmind.easyflashingcards.data.repository.CardDataSource.Companion.CURRENT_VERSION

@Database(entities = [CardEntity::class, DeckEntity::class], version = CURRENT_VERSION)
abstract class CardDataSource : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun deckDao(): DeckDao

    companion object {
        private const val NAME = "cards.db"
        const val CURRENT_VERSION = 1

        fun create(context: Context): CardDataSource {
            return Room.databaseBuilder(context, CardDataSource::class.java, NAME)
                .addCallback(object : Callback() {
                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                    }

                    override fun onCreate(db: SupportSQLiteDatabase) {

                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {

                    }
                }).build()
        }
    }

}