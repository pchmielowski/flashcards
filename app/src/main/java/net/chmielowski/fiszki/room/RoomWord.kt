package net.chmielowski.fiszki.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "word")
class RoomWord {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var value: String = ""
    var score: Int = 0

    constructor(value: String) {
        this.value = value
    }

    fun incrementScore(): RoomWord {
        score++
        return this
    }
}