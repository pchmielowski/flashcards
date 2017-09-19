package net.chmielowski.fiszki.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
class RoomWord {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var value: String = ""
    var score: Int = 0
}