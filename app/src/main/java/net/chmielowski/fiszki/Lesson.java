package net.chmielowski.fiszki;

import java.util.List;

final class Lesson {
    final List<String> basicWords;
    final List<Word> words;

    Lesson(List<String> basicWords, List<Word> words) {
        this.basicWords = basicWords;
        this.words = words;
    }
}
