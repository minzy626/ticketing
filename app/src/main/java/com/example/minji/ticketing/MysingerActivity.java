package com.example.minji.ticketing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MysingerActivity extends AppCompatActivity {
    InputMethodManager imm;
    ImageView imageView3;
    EditText et_singer;
    Button btn_reg;
    static String singer;
    ListView listView;
    MysingerlistviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysinger);


        adapter= new MysingerlistviewAdapter();
        listView =(ListView)findViewById(R.id.list_singer);
        listView.setAdapter(adapter);

        imageView3=(ImageView) findViewById(R.id.imageView3);
        et_singer=(EditText)findViewById(R.id.et_singer);
        btn_reg=(Button)findViewById(R.id.btn_singeradd);


        btn_reg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                singer = et_singer.getText().toString();

                if(singer.length()==0){
                    Toast.makeText(getApplicationContext(),"가수를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    adapter.addItem(singer);
                    adapter.notifyDataSetChanged();
                    et_singer.setText("");
                }

            }
        });

    }

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(et_singer.getWindowToken(), 0);
    }
}

