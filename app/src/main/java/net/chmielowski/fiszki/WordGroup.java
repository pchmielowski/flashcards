package net.chmielowski.fiszki;

import io.realm.RealmList;
import io.realm.RealmObject;


public class WordGroup extends RealmObject {
    static final String SCORE = "score";
    public RealmList<Word> words;
    public Word base;
    public Integer score;

    public WordGroup() {
    }

    WordGroup(RealmList<Word> words) {
        this.words = words;
        this.base = words.get(0);
        this.score = 0;
    }

}
