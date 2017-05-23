package com.example.minji.ticketing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyinfoActivity extends AppCompatActivity {

    private static final String TAG = "myinfopage";

    private TextView IDTEXT;
    private EditText PWTEXT;
    private EditText PWTEXT2;
    private EditText PHONETEXT;
    private Button pwcon;
    private String ph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        IDTEXT = (TextView) findViewById(R.id.tv_myinfoid);
        PWTEXT = (EditText) findViewById(R.id.et_myinfopw);
        PWTEXT2 = (EditText) findViewById(R.id.et_myinforepw);
        PHONETEXT = (EditText) findViewById(R.id.et_myinfopw);


        pwcon=(Button)findViewById(R.id.btn_myinfoch);
        pwcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String pw1;
                String pw2;

                pw1 = PWTEXT.getText().toString();
                pw2 = PWTEXT2.getText().toString();
                ph =PHONETEXT.getText().toString();

                if(pw1.equals(pw2)){
                    Intent intent = new Intent(getApplicationContext(),MypageActivity.class);
                    Toast.makeText(getApplicationContext(),"정보가 수정되었습니다.",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();

                    }

            }
        });


    }

}







