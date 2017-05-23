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
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

public class PostActivity extends AppCompatActivity {
    ImageButton btn_postcomment;
    ImageView imageView;
    TextView tv_posttitle;
    TextView tv_postwriter;
    TextView tv_postcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btn_postcomment=(ImageButton)findViewById(R.id.btn_postcomment);
        imageView=(ImageView)findViewById(R.id.imageView);
        tv_posttitle=(TextView)findViewById(R.id.tv_posttitle);
        tv_postwriter=(TextView)findViewById(R.id.tv_postwriter);
        tv_postcontent=(TextView)findViewById(R.id.tv_postcontent);

        btn_postcomment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CommentActivity.class);
                startActivity(intent);
            }

        });

    }
}
