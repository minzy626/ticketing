package com.example.minji.ticketing;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String search_url = "http://52.79.188.75/api/v1/tickets?per_page=";//
    String per_page="20";//보일 개수
    String page_no="1";
    String request;
    private List<HashMap<String,String>> concert_infoList=null;
    EditText et_search;
    public static String search;
    Button bt_search;
    Button bt_login;
    Button bt_logout;
    String selectedItem;


    ListView listView=null;
    MainlistviewAdapter adapter=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter= new MainlistviewAdapter();
        concert_infoList=new ArrayList<HashMap<String, String>>();//리스트생성
        listView =(ListView)findViewById(R.id.lv_mainlist);
        listView.setAdapter(adapter);
        request=search_url+per_page+"&page_no="+page_no;
        new JsonLoadingTask().execute();


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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                HashMap<String,String> hashmap =concert_infoList.get(position);
                Intent intent = new Intent(getApplication(),ConcertinfoActivity.class);
                intent.putExtra("id",hashmap.get("id"));
                startActivity(intent);
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
        bt_logout=(Button)nav_header.findViewById(R.id.bt_headlogout);
        bt_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt_logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                LoginActivity.Id="";
                LoginActivity.Phone="";
                LoginActivity.Name="";
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(LoginActivity.Id.isEmpty()){
            bt_login.setVisibility(View.VISIBLE);
            bt_logout.setVisibility(View.GONE);
        }else{
            bt_login.setVisibility(View.GONE);
            bt_logout.setVisibility(View.VISIBLE);
        }

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
            if(LoginActivity.Id.isEmpty()){
                Toast.makeText(MainActivity.this,"로그인 하셔야 이용가능 합니다.",Toast.LENGTH_LONG).show();
            }
            else{
            Intent intent1 = new Intent(this,MypageActivity.class);
            startActivity(intent1);
            }
        } else if (id == R.id.nav_ticketopen) {
            Intent intent2 = new Intent(this,TicketopenActivity.class);
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
    public class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText(request);
        } // doInBackground : 백그라운드 작업을 진행한다.
        @Override
        protected void onPostExecute(String result) {

            for(int i=0;i<concert_infoList.size();i++){
                HashMap<String,String> hashmap =concert_infoList.get(i);
                String title = hashmap.get("concert_name");
                String date = hashmap.get("open_date");
                String space = hashmap.get("source_site");
                adapter.addItem(title,date,space);
                adapter.notifyDataSetChanged();

            }
        } // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.
    } // JsonLoadingTask

    /**
     * 원격으로부터 JSON형태의 문서를 받아서
     * JSON 객체를 생성한 다음에 객체에서 필요한 데이터 추출
     */
    public String getJsonText(String url) {

        StringBuffer sb = new StringBuffer();
        try {

            //주어진 URL 문서의 내용을 문자열로 얻는다.
            String jsonPage = getStringFromUrl(url);

            //읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json = new JSONObject(jsonPage);

            String result_count =json.getString("result_count");//결과갯수
            Log.d("TEST","Test:"+result_count);

            JSONArray json_result = json.getJSONArray("result");//결과객체

            int results = Integer.parseInt(result_count);


                for(int i=0;i<json_result.length();i++){

                    json=json_result.getJSONObject(i);

                    String concert_name = json.getString("concert_name");//""안에는 변수명
                    Log.d("TEST","Test:"+concert_name );
                    String detail_info = json.getString("detail_info");
                    Log.d("TEST","Test:"+detail_info);
                    String site_url = json.getString("site_url");
                    Log.d("TEST","Test:"+site_url);
                    String open_date = json.getString("open_date");
                    Log.d("TEST","Test:"+open_date);
                    String image_url = json.getString("image_url");
                    Log.d("TEST","Test:"+image_url);
                    String source_site = json.getString("source_site");
                    String id = json.getString("id");

                    HashMap<String,String> concert_infomap= new HashMap<String,String>();//해시맵에저장

                    if(source_site.equals("1")){
                        source_site=source_site.replace("1","인터파크");
                    }else if(source_site.equals("2")){
                        source_site=source_site.replace("2","멜론티켓");
                    }else if(source_site.equals("3")){
                        source_site=source_site.replace("3","옥션티켓");
                    }else if(source_site.equals("4")){
                        source_site=source_site.replace("4","YES24");
                    }

                    concert_infomap.put("concert_name",concert_name);
                    concert_infomap.put("site_url",site_url);
                    concert_infomap.put("detail_info",detail_info);
                    concert_infomap.put("open_date",open_date);
                    concert_infomap.put("image_url",image_url);
                    concert_infomap.put("source_site",source_site);
                    concert_infomap.put("id",id);

                    concert_infoList.add(concert_infomap);//해시맵 리스트

                    Log.d("TEST","hashsize:"+concert_infomap.size());

                }


        } catch (Exception e) {
            // TODO: handle exception
        }

        return sb.toString();
    }//getJsonText()-----------

    // getStringFromUrl : 주어진 URL의 문서의 내용을 문자열로 반환
    public String getStringFromUrl(String pUrl){

        BufferedReader bufreader=null;
        HttpURLConnection urlConnection = null;

        StringBuffer page=new StringBuffer(); //읽어온 데이터를 저장할 StringBuffer객체 생성

        try {

            //[Type1]
            /*
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(pUrl));
            InputStream contentStream = response.getEntity().getContent();
            */

            //[Type2]
            URL url= new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();

            bufreader = new BufferedReader(new InputStreamReader(contentStream,"UTF-8"));
            String line = null;

            //버퍼의 웹문서 소스를 줄단위로 읽어(line), Page에 저장함
            while((line = bufreader.readLine())!=null){
                Log.d("line:",line);
                page.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //자원해제
            try {
                bufreader.close();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return page.toString();
    }// getStringFromUrl()------------------------




}
