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
    @ColumnInfo(name = "deck_id") val deckId: Int = 0,
    @ColumnInfo(name = "content_type") val contentType: Int = 0,
    @ColumnInfo(name = "enabled") val enabled: Int = 1
)

fun CardEntity.toCard(): Card =
    Card(
        id = this.id,
        frontSide = this.frontSide,
        backSide = this.backSide,
        deckId = this.deckId,
        contentType = ContentType.values().find { it.value == this.contentType }
            ?: ContentType.UNDEFINED,
        enabled = enabled == 1
    )

enum class ContentType(val value: Int, val label: String) {
    UNDEFINED(0, "Unspecified"),
    WORD(1, "Word"),
    SENTENCE(2, "Sentence")
}
