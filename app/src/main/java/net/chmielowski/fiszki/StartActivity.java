package net.chmielowski.fiszki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setLanguage(R.id.greek, DictionaryUtils.Lang.GREEK);
        setLanguage(R.id.italian, DictionaryUtils.Lang.ITALIAN);
    }

    private void setLanguage(int button, DictionaryUtils.Lang lang) {
        findViewById(button).setOnClickListener(view -> {
            final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(MainActivity.LANGUAGE, lang);
            startActivity(intent);
            finish();
        });
    }

}
