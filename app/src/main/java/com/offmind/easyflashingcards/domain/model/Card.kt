package com.offmind.easyflashingcards.domain.model

import com.offmind.easyflashingcards.data.datasource.entity.ContentType

data class Card(
    val id: Int,
    val frontSide: String,
    val backSide: String,
    val deckId: Int,
    val contentType: ContentType,
    val enabled: Boolean
)

