package net.chmielowski.fiszki;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Word extends RealmObject implements Serializable {
    @PrimaryKey
    String id = UUID.randomUUID().toString();
    String foreign;
    String english;
    @Ignore
    int score;

    public Word() {
    }

    Word(String english, String foreign) {
        this.english = english;
        this.foreign = foreign;
    }

    @Override
    public String toString() {
        return "Word{" +
                "english='" + english + '\'' +
                ", foreign='" + foreign + '\'' +
                ", score=" + score +
                '}';
    }
}
