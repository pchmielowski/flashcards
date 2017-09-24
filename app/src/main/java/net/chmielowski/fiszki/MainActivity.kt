package net.chmielowski.fiszki

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import java.util.*


class MainActivity : AppCompatActivity() {
    private val NUMBER_OF_REPETITIONS = 2
    private val myView = MyView(this)
    private val random = Random()
    private lateinit var word: Word
    private val realmDelegate = RealmDelegate()
    private lateinit var lesson: Lesson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RoomService().saveData(applicationContext)
        setContentView(R.layout.activity_main)
        realmDelegate.onCreate()
        lesson = savedInstanceState
                ?.getString(LESSON_ID)
                ?.let {
                    realmDelegate.realm
                            .where(Lesson::class.java)
                            .equalTo("id", it)
                            .findFirst()
                }
                ?: DictionaryUtils.getLesson(
                language,
                numberOfWords,
                realmDelegate.realm)
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        findViewById<View>(R.id.show).setOnClickListener {
            vibrate(v)
            myView.showAnswer()
        }
        findViewById<View>(R.id.pass).setOnClickListener {
            vibrate(v)
            onPass()
        }
        findViewById<View>(R.id.fail).setOnClickListener {
            vibrate(v)
            onFail()
        }
        findViewById<ProgressBar>(R.id.progress).max = lesson.numberOfAllWords() * NUMBER_OF_REPETITIONS
        nextWord()
        findViewById<View>(R.id.play).setOnClickListener {
            vibrate(v)
            play(word.foreign, language.shortcut)
        }
    }


    private val numberOfWords: Int
        get() = intent.getIntExtra(NUMBER_OF_WORDS, NUMBER_OF_REPETITIONS)

    private val language: DictionaryUtils.Lang
        get() = intent.getSerializableExtra(LANGUAGE) as DictionaryUtils.Lang

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString(LESSON_ID, lesson.id)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        realmDelegate.onDestroy()
    }

    private fun vibrate(v: Vibrator) {
        @Suppress("DEPRECATION")
        v.vibrate(20)
    }

    private fun onFail() {
        nextWord()
    }

    private fun onPass() {
        realmDelegate.realm
                .executeTransaction { lesson.incrementScoreOf(word) }
        nextWord()
    }

    private fun score() = lesson.score()

    private fun nextWord() {
        chooseNext(andThen = Runnable { myView.refreshView() })
    }

    private fun chooseNext(andThen: Runnable) {
        val unknown = lesson.scores
                .filter { it.score < NUMBER_OF_REPETITIONS }
                .map { it.word }
                .toList()
        val finished = unknown.isEmpty()
        if (finished) {
            realmDelegate.realm
                    .executeTransaction { db ->
                        lesson.groups.forEach { it.score++ }
                        db.delete(Lesson::class.java)
                        db.delete(WordScore::class.java)
                    }
            finish()
            return
        }
        word = unknown[random.nextInt(unknown.size)]
        andThen.run()
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

        private fun updateProgressBar() {
            (this.activity.findViewById<View>(R.id.progress) as ProgressBar).progress = this.activity.score()
        }

        internal fun refreshView() {
            updateProgressBar()
            this.activity.findViewById<TextView>(R.id.english).text = this.activity.word.english
            this.activity.findViewById<TextView>(R.id.foreign).text = this.activity.word.foreign
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
        val LANGUAGE = "language"
        val NUMBER_OF_WORDS = "number_of_words"
        private val LESSON_ID = "LESSON_ID"
    }
}

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
