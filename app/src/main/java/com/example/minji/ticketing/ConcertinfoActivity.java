package com.example.minji.ticketing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConcertinfoActivity extends AppCompatActivity {

    private Button gosite;
    private TextView coninfo;
    private View conimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concertinfo);

        gosite=(Button)findViewById(R.id.bt_gosite);
        conimg=(View)findViewById(R.id.view_conimg);
        coninfo=(TextView)findViewById(R.id.tv_detailcon);



    }
}
