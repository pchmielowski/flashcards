package net.chmielowski.fiszki;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String LANGUAGE = "language";
    public static final String NUMBER_OF_WORDS = "number_of_words";
    private static final String LESSON_ID = "LESSON_ID";
    private final int NUMBER_OF_REPETITIONS = 3;
    private Random random = new Random();
    private Word word;
    private RealmDelegate realmDelegate = new RealmDelegate();
    private Lesson lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realmDelegate.onCreate();
        Optional.ofNullable(savedInstanceState)
                .map(state -> state.getSerializable(LESSON_ID))
                .executeIfPresent(lessonId -> {
                    // get lesson from DB and save to MainActivity.this.lesson
                })
                .executeIfAbsent(() -> {
                    lesson = DictionaryUtils.getLesson(
                            getLanguage(), getNumberOfWords(), realmDelegate.getRealm());
//                    words = Collections.unmodifiableList(lesson.getGroups());
                });

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
        ((ProgressBar) findViewById(R.id.progress)).setMax(lesson.numberOfAllWords() * NUMBER_OF_REPETITIONS);
        nextWord();
        findViewById(R.id.play).setOnClickListener(view -> {
            vibrate(v);
            new Thread(() -> {
                try {
                    final MediaPlayer player = new MediaPlayer();
                    player.setDataSource(String.format(
                            "https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=%s&client=tw-ob",
                            word.foreign,
                            getLanguage().shortcut
                    ));
                    player.prepareAsync();
                    player.setOnPreparedListener(MediaPlayer::start);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });
    }

    private int getNumberOfWords() {
        return getIntent().getIntExtra(NUMBER_OF_WORDS, NUMBER_OF_REPETITIONS);
    }

    private DictionaryUtils.Lang getLanguage() {
        return (DictionaryUtils.Lang) getIntent().getSerializableExtra(LANGUAGE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LESSON_ID, lesson.id);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmDelegate.onDestroy();
    }

    private void vibrate(Vibrator v) {v.vibrate(20);}

    private void onFail() {
        nextWord();
    }

    private void onPass() {
        realmDelegate.getRealm()
                     .executeTransaction(db -> lesson.incrementScoreOf(word)
                     );
        nextWord();
    }

    private void showAnswer() {
        findViewById(R.id.foreign).setVisibility(View.VISIBLE);
        enable(R.id.pass);
        enable(R.id.fail);
        enable(R.id.play);
        disable(R.id.show);
    }

    private Integer score() {
        return lesson.score();
    }

    private void nextWord() {
        updateProgressBar();
        chooseNext();
        ((TextView) findViewById(R.id.english)).setText(word.english);
        ((TextView) findViewById(R.id.foreign)).setText(word.foreign);
        findViewById(R.id.foreign).setVisibility(View.INVISIBLE);
        disable(R.id.pass);
        disable(R.id.fail);
        disable(R.id.play);
        enable(R.id.show);
    }

    private void chooseNext() {
        final List<Word> unknown = Stream.of(lesson.scores)
                                         .filter(score -> score.getScore() < NUMBER_OF_REPETITIONS)
                                         .map(score -> score.word)
                                         .toList();
        final boolean finished = unknown.size() == 0;
        if (finished) {
            realmDelegate.getRealm()
                         .executeTransaction(db -> Stream.of(lesson.groups)
                                                         .forEach(w -> w.score++));
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
