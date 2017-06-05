package com.example.minji.ticketing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class chkIdActivity extends AppCompatActivity implements View.OnClickListener  {
    EditText id;
    static TextView description;
    Button chkId,useId;
    static boolean chkok=false;
    String chkid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk_id);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        init();
    }
    void init(){
        id=(EditText)findViewById(R.id.id);
        description=(TextView)findViewById(R.id.description);
        chkId=(Button)findViewById(R.id.chkId);
        useId=(Button)findViewById(R.id.useId);
        chkId.setOnClickListener(this);
        useId.setOnClickListener(this);

    }public void onClick(View v) {
        switch (v.getId()){
            case R.id.chkId:
                chkid=id.getText().toString();
                Log.e("id", chkid);
                if(chkid.length()!=0){
                    sql_control.idChk(chkid);
                }else{
                    Toast.makeText(getApplication(),"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.useId:
                if(chkok){
                    Intent intent = new Intent();
                    intent.putExtra("ok",true);
                    intent.putExtra("id",chkid);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    setResult(RESULT_CANCELED);
                }

                break;
        }
    }


    static public void chkidresult(String result){  //id check
        Log.e("chkidresult",result);
        if(result.contains("1")){
            Log.e("chkidresult","false");
            description.setText("사용중인 아이디입니다.");
            chkok=false;
        }else if(result.contains("0")){
            Log.e("chkidresult","true");
            description.setText("사용가능한 아이디입니다.");
            chkok=true;
        }else{
            description.setText("다시 확인해주세요");
        }

    }
}
