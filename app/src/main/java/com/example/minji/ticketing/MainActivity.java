package com.example.minji.ticketing;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText et_search;
    static String search;
    private Button bt_search;
    private Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView;
        MainlistviewAdapter adapter;

        adapter= new MainlistviewAdapter();
        listView =(ListView)findViewById(R.id.mainlist);
        listView.setAdapter(adapter);

        adapter.addItem("2017 축가","2017.05.28","성시경콘서트","연세대학교");
        adapter.addItem("정준일 겨울콘서트 고백","2017.12.02","정준일 연말 콘서트","경희대학교");
        adapter.addItem("콘서트이름","날짜","콘서트내용","장소");
        adapter.addItem("콘서트이름","날짜","콘서트내용","장소");
        adapter.addItem("콘서트이름","날짜","콘서트내용","장소");
        adapter.addItem("콘서트이름","날짜","콘서트내용","장소");
        adapter.addItem("콘서트이름","날짜","콘서트내용","장소");


        bt_search=(Button)findViewById(R.id.bt_search);
        et_search = (EditText)findViewById(R.id.et_search);
        bt_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                search = et_search.getText().toString();

                if(search.length()==0){
                    Toast.makeText(getApplicationContext(),"검색어를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                    intent.putExtra("search",search);
                    startActivity(intent);
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_header=navigationView.getHeaderView(0);
        bt_login=(Button)nav_header.findViewById(R.id.bt_headlogin);
        bt_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mypage) {
            Intent intent1 = new Intent(this,MypageActivity.class);
            startActivity(intent1);
        } else if (id == R.id.nav_ticketopen) {
            Intent intent2 = new Intent(this,MainActivity.class);
            startActivity(intent2);
        } else if (id == R.id.nav_concert) {
            Intent intent3 = new Intent(this,CalenderActivity.class);
            startActivity(intent3);
        } else if (id == R.id.nav_servertime) {
            Intent intent4 = new Intent(this,ServertimeActivity.class);
            startActivity(intent4);
        } else if (id == R.id.nav_board) {
            Intent intent5 = new Intent(this,DealboardActivity.class);
            startActivity(intent5);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
