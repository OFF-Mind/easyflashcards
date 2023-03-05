package com.offmind.easyflashingcards.data.datasource.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.offmind.easyflashingcards.domain.model.Card

@Entity(tableName = "card_table")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "front_side") val frontSide: String = "",
    @ColumnInfo(name = "back_side") val backSide: String = "",
    @ColumnInfo(name = "deck_id") val deckId: Int = 0
)

fun CardEntity.toCard(): Card =
    Card(
        id = this.id,
        frontSide = this.frontSide,
        backSide = this.backSide
    )