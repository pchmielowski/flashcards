package net.chmielowski.fiszki.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(RoomWord::class), version = 1)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}

