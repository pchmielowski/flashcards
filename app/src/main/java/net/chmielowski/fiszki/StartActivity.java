package net.chmielowski.fiszki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private int numberOfWords = 3;
    private RealmDelegate realmDelegate = new RealmDelegate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realmDelegate.onCreate();
        setContentView(R.layout.activity_start);
        setLanguage(R.id.greek, DictionaryUtils.Lang.GREEK);
        setLanguage(R.id.italian, DictionaryUtils.Lang.ITALIAN);
        showMyWords(findViewById(R.id.words), this);
    }

    private void showMyWords(RecyclerView view, StartActivity context) {
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(new WordsAdapter(realmDelegate.getRealm()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmDelegate.onDestroy();
    }

    private void setLanguage(int button, DictionaryUtils.Lang lang) {
        findViewById(button).setOnClickListener(view -> {
            final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(MainActivity.LANGUAGE, lang);
            intent.putExtra(MainActivity.NUMBER_OF_WORDS, numberOfWords);
            startActivity(intent);
        });
        ((SeekBar) findViewById(R.id.numberOfWordsSeekBar))
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        numberOfWords = progress;
                        ((TextView) findViewById(R.id.numberOfWordsText))
                                .setText(String.valueOf(progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
    }

}
