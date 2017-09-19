package net.chmielowski.fiszki.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface WordDao {
    @Query("SELECT * FROM word WHERE word.value = :value LIMIT 1")
    fun get(value: String): List<RoomWord>

    @Insert
    fun insert(word: RoomWord)
}
