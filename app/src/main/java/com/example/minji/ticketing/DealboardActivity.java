package com.example.minji.ticketing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DealboardActivity extends AppCompatActivity {
    Button btn_dealadd;
    ListView listView=null;
    DealboardlistviewAdapter adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealboard);

        adapter= new DealboardlistviewAdapter();
        listView =(ListView)findViewById(R.id.list_deal);
        listView.setAdapter(adapter);
        btn_dealadd=(Button)findViewById(R.id.btn_dealadd) ;

        adapter.addItem("게시글제목","작성시간","작성자");
        adapter.addItem("게시글제목","작성시간","작성자");
        adapter.addItem("게시글제목","작성시간","작성자");
        adapter.addItem("게시글제목","작성시간","작성자");
        adapter.addItem("게시글제목","작성시간","작성자");
        adapter.addItem("게시글제목","작성시간","작성자");

        btn_dealadd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),WriteActivity.class);
                startActivity(intent);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                Intent intent = new Intent(getApplication(),PostActivity.class);
                startActivity(intent);
            }
        });



    }
}
