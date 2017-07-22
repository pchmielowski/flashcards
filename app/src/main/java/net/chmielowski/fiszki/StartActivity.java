package net.chmielowski.fiszki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.greek)
                .setOnClickListener(view -> {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                });
    }
}
