package com.example.minji.ticketing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

                   InsertData task = new InsertData();
                    task.execute(title, content, LoginActivity.Index);

                }

            }
        });

        btn_dealcancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });


    }



    class InsertData extends AsyncTask<String, Void, String> { //<onPre parameter, doInBack parameter, onPost parameter>
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() { //Asynctask 실행 했을 때 뜨는 화면
            super.onPreExecute();

            progressDialog = ProgressDialog.show(WriteActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {//Asynctask 완료 후 뜨는 화면
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result.equals("SQL문 처리 성공")) {


                Toast.makeText(WriteActivity.this, "게시글 작성이 완료되었습니다", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), DealboardActivity.class);
                startActivity(intent1);
            } else {
                Toast.makeText(WriteActivity.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
            Log.d("Test", "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) { //background

            String title = (String) params[0];
            String content = (String) params[1];
            String writer = (String) params[2];


            String serverURL = "https://ticketing-php-minzykim.c9users.io/write.php";
            String postParameters = "title=" + title + "&content=" + content + "&writer=" +writer ;
            Log.d("write", "POST response  - " + postParameters);

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
                Log.d("test", "POST response code - " + responseStatusCode);

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

                Log.d("TEst", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }



    public void onBackPressed() {
        finish();
    }


}
