package net.chmielowski.fiszki

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.TextView

class StartActivity : AppCompatActivity() {

    private var numberOfWords = 3
    private val realmDelegate = RealmDelegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realmDelegate.onCreate()
        setContentView(R.layout.activity_start)
        setLanguage(R.id.greek, DictionaryUtils.Lang.GREEK)
        setLanguage(R.id.italian, DictionaryUtils.Lang.ITALIAN)
    }

    override fun onDestroy() {
        super.onDestroy()
        realmDelegate.onDestroy()
    }

    private fun setLanguage(button: Int, lang: DictionaryUtils.Lang) {
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
