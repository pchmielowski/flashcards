package net.chmielowski.fiszki;

import android.annotation.SuppressLint;

import java.io.Serializable;

import io.realm.RealmObject;

public class Word extends RealmObject implements Serializable {
    String foreign;
    String english;

    public Word() {
    }

    Word(String english, String foreign) {
        this.english = english;
        this.foreign = foreign;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("Word{english='%s', foreign='%s'}", english, foreign);
    }
}
