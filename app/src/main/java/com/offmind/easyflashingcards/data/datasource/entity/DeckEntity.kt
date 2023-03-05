package com.offmind.easyflashingcards.data.datasource.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.offmind.easyflashingcards.domain.model.Card
import com.offmind.easyflashingcards.domain.model.Deck

@Entity(tableName = "deck_table")
data class DeckEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "deck_name") val deckName: String = ""
)

fun DeckEntity.toDeck(): Deck =
    Deck(
        id = this.id,
        displayName = deckName
    )
