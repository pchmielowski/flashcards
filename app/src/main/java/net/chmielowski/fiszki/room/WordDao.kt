package net.chmielowski.fiszki.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface WordDao {
    @Query("SELECT * FROM word WHERE word.value = :arg0")
    fun get(value: String): List<RoomWord>

    @Insert
    fun insertAll(vararg users: RoomWord)
}
