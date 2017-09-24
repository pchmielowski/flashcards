package net.chmielowski.fiszki

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class StartActivity : AppCompatActivity() {

    private var numberOfWords = 3
    private val realmDelegate = RealmDelegate()
    lateinit var roomService: RoomService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realmDelegate.onCreate()
        setContentView(R.layout.activity_start)
        setUp(R.id.start_lesson, DictionaryUtils.Lang.ITALIAN)
        roomService.data()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    @SuppressLint("SimpleDateFormat")
                    val txt = it.map { lessonInfo -> lessonInfo.time }
                            .map { time -> SimpleDateFormat("yyyyy-mm-dd hh:mm:ss").format(Date(time)) }
                            .reduce { acc, s -> "$acc\n$s" }
                    findViewById<TextView>(R.id.last_lessons)
                            .setText(txt)
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        realmDelegate.onDestroy()
    }

    private fun setUp(button: Int, lang: DictionaryUtils.Lang) {
        findViewById<View>(button).setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra(MainActivity.LANGUAGE, lang)
            intent.putExtra(MainActivity.NUMBER_OF_WORDS, numberOfWords)
            startActivity(intent)
        }
        (findViewById<View>(R.id.numberOfWordsSeekBar) as SeekBar)
                .setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        numberOfWords = progress
                        (findViewById<View>(R.id.numberOfWordsText) as TextView).text = progress.toString()
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {

                    }
                })
    }

}
