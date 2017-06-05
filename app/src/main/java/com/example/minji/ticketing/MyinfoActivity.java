package com.example.minji.ticketing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyinfoActivity extends AppCompatActivity {

    private static final String TAG = "myinfopage";

    private TextView IDTEXT;
    private EditText PWTEXT;
    private EditText PWTEXT2;
    private TextView NAMETEXT;
    private EditText PHONETEXT;
    private Button pwcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        IDTEXT = (TextView) findViewById(R.id.tv_myinfoid);
        PWTEXT = (EditText) findViewById(R.id.et_myinfopw);
        PWTEXT2 = (EditText) findViewById(R.id.et_myinforepw);
        NAMETEXT = (TextView) findViewById(R.id.tv_myinfoname);
        PHONETEXT = (EditText) findViewById(R.id.et_myinfoph);

        IDTEXT.setText(LoginActivity.Id);
        NAMETEXT.setText(LoginActivity.Name);
        PHONETEXT.setText(LoginActivity.Phone);

        pwcon=(Button)findViewById(R.id.btn_myinfoch);
        pwcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String pw1;
                String pw2;
                String ph;


                pw1 = PWTEXT.getText().toString();
                pw2 = PWTEXT2.getText().toString();
                ph =PHONETEXT.getText().toString();

                if(pw1.equals(pw2)){

                    InsertData task = new InsertData();
                    task.execute(pw1,ph,LoginActivity.Id);

                }
                else{
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    class InsertData extends AsyncTask<String, Void, String> { //<onPre parameter, doInBack parameter, onPost parameter>
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() { //Asynctask 실행 했을 때 뜨는 화면
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MyinfoActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {//Asynctask 완료 후 뜨는 화면
            super.onPostExecute(result);

            progressDialog.dismiss();
            if (result.equals("SQL문 처리 성공")) {
                Toast.makeText(MyinfoActivity.this, "회원 정보 변경완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyinfoActivity.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) { //background

            String st_pw = (String) params[0];
            String st_phone = (String) params[1];
            String st_id =(String)params[2];


            String serverURL = "https://ticketing-php-minzykim.c9users.io/infoupdate.php";
            String postParameters = "pwd=" + st_pw +"&phone=" + st_phone+"&id="+st_id;
            Log.d(TAG, "POST response  - " + "pwd=" + st_pw +"&phone=" + st_phone +"&id="+st_id);


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

}







