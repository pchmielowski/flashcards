package net.chmielowski.fiszki

internal class Lesson(groups: List<WordGroup>) {
    val allWords: List<Word> = groups.flatMap { it.words }

}
