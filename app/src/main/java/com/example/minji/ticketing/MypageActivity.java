package com.example.minji.ticketing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class MypageActivity extends AppCompatActivity {
    private static final String TAG = "MyPage";

    Button btn_myinfo;
    Button btn_mysinger;
    Button btn_alarm;
    ImageView imageView2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        imageView2=(ImageView)findViewById(R.id.imageView2);
        btn_myinfo=(Button)findViewById(R.id.btn_myinfo);
        btn_mysinger=(Button)findViewById(R.id.btn_mysinger);
        btn_alarm=(Button)findViewById(R.id.btn_alarm);

        btn_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyinfoActivity.class);
                startActivity(intent);
            }
        });

        btn_mysinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MysingerActivity.class);
                startActivity(intent);
            }
        });

        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AlarmActivity.class);
                startActivity(intent);
            }
        });
    }


}



