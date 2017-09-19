package net.chmielowski.fiszki

import android.arch.persistence.room.Room
import android.content.Context
import net.chmielowski.fiszki.room.WordDatabase

internal class DataBaseService(context: Context) {
    val db: WordDatabase = Room.databaseBuilder(context, WordDatabase::class.java, "word")
            .build()

    fun save(words: List<String>) {
        words.forEach {
            with(db.dao()) {
                insert(get(it)[0].incrementScore())
            }
        }
    }
}
