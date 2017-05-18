package com.example.minji.ticketing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by minji on 2017-05-17.
 */

public class SplashActivity  extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent_intro = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent_intro);
                finish();
            }
        }, 3500);

    }
}