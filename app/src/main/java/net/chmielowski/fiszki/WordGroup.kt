package net.chmielowski.fiszki

import io.realm.RealmList
import io.realm.RealmObject

internal open class WordGroup : RealmObject {

    companion object {
        const val SCORE = "score"
    }

    constructor(words: RealmList<Word>) : super() {
        this.words = words
        this.base = words[0]
    }

    constructor() : super()

    lateinit var words: RealmList<Word>
    lateinit var base: Word
    var score = 0
}
