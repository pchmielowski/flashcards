package net.chmielowski.fiszki;

class Word {
    final String english;
    final String foreign;
    int score = 0;

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
