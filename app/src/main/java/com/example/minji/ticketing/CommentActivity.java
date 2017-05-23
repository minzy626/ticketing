package com.example.minji.ticketing;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by JIYOUNG on 2017-05-23.
 */

public class CommentActivity extends AppCompatActivity {

    InputMethodManager imm;
    ImageView imageView2;
    EditText et_postcomment;
    ImageButton btn_input;
    ImageButton btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        btn_input=(ImageButton)findViewById(R.id.btn_input);
        btn_cancel=(ImageButton)findViewById(R.id.btn_cancel);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        et_postcomment = (EditText)findViewById(R.id.et_postcomment);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PostActivity.class);
                startActivity(intent);
            }
        });

    }

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(et_postcomment.getWindowToken(), 0);
    }
}
