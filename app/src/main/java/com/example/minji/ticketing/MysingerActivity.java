package com.example.minji.ticketing;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MysingerActivity extends AppCompatActivity {
    InputMethodManager imm;
    ImageView imageView3;
    EditText et_singer;
    Button btn_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysinger);

        imageView3=(ImageView) findViewById(R.id.imageView3);
        et_singer=(EditText)findViewById(R.id.et_singer);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        btn_reg=(Button)findViewById(R.id.btn_reg);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        et_singer = (EditText)findViewById(R.id.et_singer);
    }

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(et_singer.getWindowToken(), 0);
    }
}

