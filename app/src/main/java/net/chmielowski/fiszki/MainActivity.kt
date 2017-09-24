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
import java.util.*


class MainActivity : AppCompatActivity() {
    private val myView = MyView(this)
    internal lateinit var realmDelegate: RealmDelegate
    internal lateinit var service: LessonService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RoomService().saveData(applicationContext)
        setContentView(R.layout.activity_main)
        realmDelegate.onCreate()
        service.restoreOrCreateLesson(
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
            onPass()
        }
        findViewById<View>(R.id.fail).setOnClickListener {
            vibrate(v)
            onFail()
        }
        findViewById<ProgressBar>(R.id.progress).max = service.lesson.numberOfAllWords() * NUMBER_OF_REPETITIONS
        nextWord()
        findViewById<View>(R.id.play).setOnClickListener {
            vibrate(v)
            play(service.word.foreign, intent.language().shortcut)
        }
    }

    private fun Bundle.getLessonId() = this.getString(LESSON_ID)

    private fun Intent.numberOfWords() = this.getIntExtra(NUMBER_OF_WORDS, NUMBER_OF_REPETITIONS)

    private fun Intent.language() = this.getSerializableExtra(LANGUAGE) as DictionaryUtils.Lang

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString(LESSON_ID, service.lesson.id)
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
        service.incrementScoreOfCurrentWord()
        nextWord()
    }

    private fun nextWord() {
        service.chooseNext(andThen = { myView.refreshView() },
                onFinished = { finish() })
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
            (this.activity.findViewById<View>(R.id.progress) as ProgressBar).progress = this.activity.service.lesson.score()
        }

        internal fun refreshView() {
            updateProgressBar()
            this.activity.findViewById<TextView>(R.id.english).text = this.activity.service.word.english
            this.activity.findViewById<TextView>(R.id.foreign).text = this.activity.service.word.foreign
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

class LessonService internal constructor(val realm: RealmDelegate) {
    internal fun persistedLesson(id: String) = realm.realm
            .where(Lesson::class.java)
            .equalTo("id", id)
            .findFirst()

    internal fun newLesson(language: DictionaryUtils.Lang, numberOfWords: Int) =
            DictionaryUtils.getLesson(
                    language,
                    numberOfWords,
                    realm.realm)

    internal lateinit var lesson: Lesson
    internal lateinit var word: Word

    fun incrementScoreOfCurrentWord() {
        realm.realm
                .executeTransaction { lesson.incrementScoreOf(word) }

    }

    internal fun restoreOrCreateLesson(savedId: String?, language: DictionaryUtils.Lang, numberOfWords: Int) {
        lesson = savedId
                ?.let {
                    persistedLesson(it)
                }
                ?: newLesson(language, numberOfWords)
    }

    private val random = Random()

    internal fun chooseNext(andThen: () -> Unit, onFinished: () -> Unit) {
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
            onFinished.invoke()
            return
        }
        word = notPracticedYet[random.nextInt(notPracticedYet.size)]
        andThen.invoke()
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
