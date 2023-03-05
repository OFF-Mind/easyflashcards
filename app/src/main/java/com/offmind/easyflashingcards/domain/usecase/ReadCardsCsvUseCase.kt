package com.offmind.easyflashingcards.domain.usecase

import android.content.Context
import android.net.Uri
import com.offmind.easyflashingcards.data.datasource.entity.CardEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class ReadCardsCsvUseCase(private val context: Context) {

    suspend operator fun invoke(pathToFile: Uri): List<CardEntity> = coroutineScope {
        withContext(Dispatchers.IO) {
            val cards = mutableListOf<CardEntity>()
            try {
                context.contentResolver.openInputStream(pathToFile)!!.use { stream ->
                    val r = BufferedReader(InputStreamReader(stream))
                    var line: String?
                    while (r.readLine().also { line = it } != null) {
                        line?.let {
                            val values = it.split(',')
                            cards.add(CardEntity(frontSide = values[0], backSide = values[1]))
                        }
                    }
                }
                if (cards.size > 1) {
                    return@withContext cards.subList(1, cards.size)
                } else {
                    return@withContext emptyList<CardEntity>()
                }
            } catch (e: Exception) {
                return@withContext emptyList<CardEntity>()
            }
        }
    }
}