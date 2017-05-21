package com.example.minji.ticketing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WriteActivity extends AppCompatActivity {
    private EditText et_title;
    private EditText et_content;
    private Button btn_dealreg;
    private Button btn_dealcancel;
    static String title;
    static String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        btn_dealcancel=(Button)findViewById(R.id.btn_dealcancel);
        btn_dealreg=(Button)findViewById(R.id.btn_dealreg);
        et_title = (EditText)findViewById(R.id.et_title);
        et_content = (EditText)findViewById(R.id.et_content);

        btn_dealreg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                title = et_title.getText().toString();
                content=et_content.getText().toString();

                if(title.length()==0||content.length()==0){
                    Toast.makeText(getApplicationContext(),"내용을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),DealboardActivity.class);
                    startActivity(intent);
                }

            }
        });

        btn_dealcancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });


    }


}
