package com.example.minji.ticketing;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.select.Evaluator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class JoinActivity extends AppCompatActivity {
    EditText mid;
    EditText mpwd;
    EditText mname;
    EditText mphone;
    Button joinOk;
    Button joinNo;
    Button idchk;

    static boolean pw_ok = false, id_ok = false;//비밀번호,아이디상태초기화
    final int code_chkid = 1000;//아이디체크코드초기화
    DialogInterface manageDialog = null;//다이얼로그메세지

    public static String st_id = "", st_pw = "", st_name = "", st_phone = "";

    private TextView sql_result;
    private static final String TAG = "Join in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        mid = (EditText) findViewById(R.id.et_joinid);
        mpwd = (EditText) findViewById(R.id.et_joinpw);
        mname = (EditText) findViewById(R.id.et_joinname);
        mphone = (EditText) findViewById(R.id.et_joinph);
        joinOk = (Button) findViewById(R.id.bt_joinok);
        joinNo = (Button) findViewById(R.id.bt_joinno);
        idchk=(Button)findViewById(R.id.bt_idchk);

        sql_result = (TextView) findViewById(R.id.join_result);
        sql_result.setVisibility(View.GONE);

        joinOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st_id = mid.getText().toString();
                st_pw = mpwd.getText().toString();
                st_name = mname.getText().toString();
                st_phone = mphone.getText().toString();

                if (st_id.isEmpty() || st_pw.isEmpty() || st_name.isEmpty() || st_phone.isEmpty()) {
                    validateForm();
                } else {

                    InsertData task = new InsertData();
                    task.execute(st_id, st_pw, st_name, st_phone);

                }
            }
        });
        joinNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        idchk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), chkIdActivity.class);
                startActivityForResult(intent1, code_chkid);
            }
        });


    }

    private boolean validateForm() {//아이디랑 비밀번호 입력되지 않았을시 에러메세지
        boolean valid = true;

        String id = mid.getText().toString();
        if (TextUtils.isEmpty(id)) {
            mid.setError("아이디를 입력해주세요");
            valid = false;
        } else {
            mid.setError(null);
        }
        String password = mpwd.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mpwd.setError("비밀번호를 입력해주세요");
            valid = false;
        } else {
            mpwd.setError(null);
        }
        String name = mname.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mname.setError("이름을 입력해주세요");
            valid = false;
        } else {
            mname.setError(null);
        }
        String phone = mphone.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mphone.setError("핸드폰번호를 입력해주세요");
            valid = false;
        } else {
            mphone.setError(null);
        }

        return valid;
    }


    class InsertData extends AsyncTask<String, Void, String> { //<onPre parameter, doInBack parameter, onPost parameter>
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() { //Asynctask 실행 했을 때 뜨는 화면
            super.onPreExecute();

            progressDialog = ProgressDialog.show(JoinActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {//Asynctask 완료 후 뜨는 화면
            super.onPostExecute(result);

            progressDialog.dismiss();
            sql_result.setText(result); //php의 result값으로 초기화
            if (result.equals("SQL문 처리 성공")) {

                mid.setText(""); //초기화
                mpwd.setText("");
                mname.setText("");
                mphone.setText("");

                Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent1);
            } else {
                Toast.makeText(JoinActivity.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) { //background

            String st_id = (String) params[0];
            String st_pw = (String) params[1];
            String st_name = (String) params[2];
            String st_phone = (String) params[3];


            String serverURL = "https://ticketing-php-minzykim.c9users.io/dead_membersave_2.php";
            String postParameters = "id=" + st_id + "&pwd=" + st_pw + "&name=" + st_name + "&phone=" + st_phone;
            Log.d(TAG, "POST response  - " + postParameters);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case code_chkid:
                    if (data.getExtras().getBoolean("ok")) {
                        st_id=data.getExtras().getString("id");
                        mid.setText(st_id);
                        id_ok = true;
                    }
                    break;
            }
        }

    }
    public void onBackPressed() {
        finish();
    }

}
