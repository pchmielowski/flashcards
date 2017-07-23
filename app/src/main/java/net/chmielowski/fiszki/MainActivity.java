package net.chmielowski.fiszki;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String LANGUAGE = "language";
    private static final String WORDS = "words";
    private Random random = new Random();
    private Word word;
    private List<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Optional.ofNullable(savedInstanceState)
                .map(state -> state.getSerializable(WORDS))
                .executeIfPresent(w -> words = (List<Word>) w)
                .executeIfAbsent(() -> words = DictionaryUtils.shuffled(
                        (DictionaryUtils.Lang) getIntent().getSerializableExtra(LANGUAGE)));

        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        findViewById(R.id.show).setOnClickListener(view -> {
            vibrate(v);
            showAnswer();
        });
        findViewById(R.id.pass).setOnClickListener(view -> {
            vibrate(v);
            onPass();
        });
        findViewById(R.id.fail).setOnClickListener(view -> {
            vibrate(v);
            onFail();
        });
        ((ProgressBar) findViewById(R.id.progress)).setMax(words.size() * 3);
        nextWord();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(WORDS, (Serializable) words);
        super.onSaveInstanceState(outState);
    }

    private void vibrate(Vibrator v) {v.vibrate(20);}

    private void onFail() {
        nextWord();
    }

    private void onPass() {
        word.score += 1;
        nextWord();
    }

    private void showAnswer() {
        findViewById(R.id.foreign).setVisibility(View.VISIBLE);
        enable(R.id.pass);
        enable(R.id.fail);
        disable(R.id.show);
    }

    private Integer score() {
        return Stream.of(words).map(w -> w.score).reduce((a, b) -> a + b).get();
    }

    private void nextWord() {
        updateProgressBar();
        chooseNext();
        ((TextView) findViewById(R.id.english)).setText(word.english);
        ((TextView) findViewById(R.id.foreign)).setText(word.foreign);
        findViewById(R.id.foreign).setVisibility(View.INVISIBLE);
        disable(R.id.pass);
        disable(R.id.fail);
        enable(R.id.show);
    }

    private void chooseNext() {
        final List<Word> unknown = Stream.of(this.words)
                                         .filter(word -> word.score < 3)
                                         .toList();
        if (unknown.size() == 0) {
            startActivity(new Intent(getApplicationContext(), StartActivity.class));
            finish();
            return;
        }
        word = unknown.get(random.nextInt(unknown.size()));
    }

    private void updateProgressBar() {
        ((ProgressBar) findViewById(R.id.progress)).setProgress(score());
    }

    private void enable(int view) {
        findViewById(view).setEnabled(true);
        findViewById(view).setAlpha(1f);
    }

    private void disable(int view) {
        findViewById(view).setAlpha(.2f);
        findViewById(view).setEnabled(false);
    }

}
