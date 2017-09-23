package net.chmielowski.fiszki

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

open internal class Lesson : RealmObject {
    lateinit var groups: RealmList<WordGroup>
    lateinit var scores: RealmList<WordScore>
    lateinit var id: String

    constructor(groups: List<WordGroup>) : super() {
        this.groups = RealmList()
        this.groups.addAll(groups)
        this.scores = RealmList()
        this.scores.addAll(groups.flatMap { it.words }.map { WordScore(it) })
        this.id = UUID.randomUUID().toString()
    }

    constructor()

    fun numberOfAllWords(): Int = groups.flatMap { it.words }.size

    fun score(): Int = scores.map { it.score }.sum()

    fun incrementScoreOf(word: Word) {
        scores.first { it.word == word }.score++
    }
}

open class WordScore() : RealmObject() {
    lateinit var word: Word
    var score: Int = 0

    constructor(word: Word) : this() {
        this.word = word
    }
}
