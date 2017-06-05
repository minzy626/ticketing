package com.example.minji.ticketing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener{
    DialogInterface manageDialog = null;//다이얼로그메세지

    private static final String TAG = "Loginpage";

    static EditText mIDField;//아이디입력
    static EditText mPasswordField;//비밀번호입력
    Button bt_login;//로그인버튼
    Button bt_join;//회원가입버튼
    static String Id="";//입력받은 아이디
    static String Password="";//입력받은 비밀번호
    static String Name="";
    static String Phone="";

    static public boolean login_state=false;//로그인상태초기화 false

    CheckBox autoLogin;//자동로그인 체크박스
    static  public Boolean loginChecked=false;//체크

    final int code_chkid=1000;//아이디체크코드초기화
   //회원가입시 받을정보들
    static String tempId="",tempName="",tempPhone="";

    static public Context mContext;//상태
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        init();
    }
    void init(){
        mIDField = (EditText) findViewById(R.id.et_email);
        mPasswordField = (EditText) findViewById(R.id.et_pw);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_join = (Button) findViewById(R.id.bt_join);
      //  autoLogin = (CheckBox)findViewById(R.id.auto_login); //자동로그인체크박스
        bt_login.setOnClickListener(this);
        bt_join.setOnClickListener(this);
        mContext=this;

    }


    private boolean validateForm() {//아이디랑 비밀번호 입력되지 않았을시 에러메세지
        boolean valid = true;

        String id = mIDField.getText().toString();
        if (TextUtils.isEmpty(id)) {
            mIDField.setError("아이디를 입력해주세요");
            valid = false;
        } else {
            mIDField.setError(null);
        }
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("비밀번호를 입력해주세요");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:

                Id = mIDField.getText().toString().trim();
                Password = mPasswordField.getText().toString().trim();

                if(Id.isEmpty()||Password.isEmpty()) {
                    validateForm();

                }else
                    login_proc(login_state);


                break;

            case R.id.bt_join:
                Intent intent1 = new Intent(getApplicationContext(), JoinActivity.class);

                startActivity(intent1);
        }
    }
    void login_proc(boolean login){ //Login
        if(!login){
            String id=mIDField.getText().toString();
            String pw=mPasswordField.getText().toString();
            loginMysql idchk = new loginMysql(id,pw); //loginMysql.java
            loginMysql.active=true;
            idchk.start();
        }else{

        }
    }
    static public void result_login(String result,String pw,String name,String ph){
        loginMysql.active=false;
        if(result.equals("false"))
            Toast.makeText(mContext,"사용자 ID가 없습니다.",Toast.LENGTH_SHORT).show();
        else{
            if(pw.equals(Password)) {//받아온 비밀번호랑 입력한 비밀번호가같으면 로그인성공후 화면전환
                mIDField.setText("");
                mPasswordField.setText("");

                Toast.makeText(mContext, name + "님 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext,MainActivity.class);/* Ticketing 프로젝트의 MainActivity로 연결 */
                intent.putExtra("name",name);
                Id=result;
                Password=pw;
                Name=name;
                Phone=ph;
                mContext.startActivity(intent);

            }else//비밀번호 확인해주세욤
                Toast.makeText(mContext,"비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    protected void onResume(){//회원가입 완료 후 뜨는화면, 회원가입시 입력한 아아디 비밀번호 세팅
        super.onResume();
        mIDField = (EditText) findViewById(R.id.et_email);
        mPasswordField = (EditText) findViewById(R.id.et_pw);
        mIDField.setText(JoinActivity.st_id);
        mPasswordField.setText(JoinActivity.st_pw);
    }
    public void onBackPressed() {
        finish();
    }

}
