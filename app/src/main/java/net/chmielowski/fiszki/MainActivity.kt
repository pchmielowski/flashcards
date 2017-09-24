package net.chmielowski.fiszki

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.*


class MainActivity : AppCompatActivity() {
    private val myView = MyView(this)

    private lateinit var realmDelegate: RealmDelegate

    private lateinit var game: Game

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RoomService().saveData(applicationContext)
        setContentView(R.layout.activity_main)
        realmDelegate.onCreate()
        disposable = game.nextWordObservable()
                .subscribe({
                    myView.refreshView(it.english, it.foreign, it.score)
                }, {
                    throw RuntimeException(it)
                }, {
                    finish()
                })
        game.restoreOrCreateLesson(
                savedInstanceState?.getLessonId(),
                intent.language(),
                intent.numberOfWords())
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        findViewById<View>(R.id.show).setOnClickListener {
            vibrate(v)
            myView.showAnswer()
        }
        findViewById<View>(R.id.pass).setOnClickListener {
            vibrate(v)
            game.onIncorrectAnswer()
        }
        findViewById<View>(R.id.fail).setOnClickListener {
            vibrate(v)
            game.onCorrectAnswer()
        }
        findViewById<ProgressBar>(R.id.progress).max = game.maxProgress()
        findViewById<View>(R.id.play).setOnClickListener {
            vibrate(v)
            game.playCurrentWord(intent.language().shortcut)
        }
    }

    private fun Bundle.getLessonId() = this.getString(LESSON_ID)

    private fun Intent.numberOfWords() = this.getIntExtra(NUMBER_OF_WORDS, NUMBER_OF_REPETITIONS)

    private fun Intent.language() = this.getSerializableExtra(LANGUAGE) as DictionaryUtils.Lang

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString(LESSON_ID, game.lessonId())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        realmDelegate.onDestroy()
        disposable.dispose()
    }

    private fun vibrate(v: Vibrator) {
        @Suppress("DEPRECATION")
        v.vibrate(20)
    }

    private class MyView internal constructor(private val activity: MainActivity) {

        private fun disable(view: Int) {
            this.activity.findViewById<View>(view).alpha = .2f
            this.activity.findViewById<View>(view).isEnabled = false
        }

        private fun enable(view: Int) {
            this.activity.findViewById<View>(view).isEnabled = true
            this.activity.findViewById<View>(view).alpha = 1f
        }

        private fun updateProgressBar(score: Int) {
            this.activity.findViewById<ProgressBar>(R.id.progress).progress = score
        }

        internal fun refreshView(english: String, foreign: String, score: Int) {
            updateProgressBar(score)
            this.activity.findViewById<TextView>(R.id.english).text = english
            this.activity.findViewById<TextView>(R.id.foreign).text = foreign
            this.activity.findViewById<View>(R.id.foreign).visibility = View.INVISIBLE
            disable(R.id.pass)
            disable(R.id.fail)
            disable(R.id.play)
            enable(R.id.show)
        }

        internal fun showAnswer() {
            this.activity.findViewById<View>(R.id.foreign).visibility = View.VISIBLE
            enable(R.id.pass)
            enable(R.id.fail)
            enable(R.id.play)
            disable(R.id.show)
        }
    }

    companion object {
        val NUMBER_OF_REPETITIONS = 2
        val LANGUAGE = "language"
        val NUMBER_OF_WORDS = "number_of_words"
        private val LESSON_ID = "LESSON_ID"
    }
}

class Game internal constructor(val realm: RealmDelegate) {
    private fun persistedLesson(id: String) = realm.realm
            .where(Lesson::class.java)
            .equalTo("id", id)
            .findFirst()

    private fun newLesson(language: DictionaryUtils.Lang, numberOfWords: Int) =
            DictionaryUtils.getLesson(
                    language,
                    numberOfWords,
                    realm.realm)

    private lateinit var lesson: Lesson

    private lateinit var word: Word

    private fun incrementScoreOfCurrentWord() {
        realm.realm
                .executeTransaction { lesson.incrementScoreOf(word) }

    }

    private val random = Random()

    private val subject: PublishSubject<WordAndScore> = PublishSubject.create<WordAndScore>()

    private fun chooseNext() {
        val notPracticedYet = lesson.scores
                .filter { it.score < MainActivity.NUMBER_OF_REPETITIONS }
                .map { it.word }
                .toList()
        if (notPracticedYet.isEmpty()) {
            realm.realm
                    .executeTransaction { db ->
                        lesson.groups.forEach { it.score++ }
                        db.delete(Lesson::class.java)
                        db.delete(WordScore::class.java)
                    }
            subject.onComplete()
            return
        }
        word = notPracticedYet[random.nextInt(notPracticedYet.size)]
        subject.onNext(WordAndScore(word.english, word.foreign, lesson.score()))
    }

    internal fun restoreOrCreateLesson(savedId: String?, language: DictionaryUtils.Lang, numberOfWords: Int) {
        lesson = savedId
                ?.let {
                    persistedLesson(it)
                }
                ?: newLesson(language, numberOfWords)
        chooseNext()
    }

    internal fun onCorrectAnswer() {
        chooseNext()
    }

    internal fun onIncorrectAnswer() {
        incrementScoreOfCurrentWord()
        chooseNext()
    }

    internal fun nextWordObservable(): Observable<WordAndScore> = subject

    internal fun maxProgress() = lesson.numberOfAllWords() * MainActivity.NUMBER_OF_REPETITIONS

    internal fun lessonId() = lesson.id

    internal fun playCurrentWord(lang: String) {
        play(word.foreign, lang)
    }
}

data class WordAndScore(val english: String, val foreign: String, val score: Int)

fun play(word: String, language: String) {
    Thread {
        with(MediaPlayer()) {
            setDataSource(String.format(
                    "https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=%s&client=tw-ob",
                    word,
                    language
            ))
            prepareAsync()
            setOnPreparedListener { it.start() }
        }
    }.start()
}
