package com.example.minji.ticketing;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

public class DealboardActivity extends AppCompatActivity {
    String search_url = "https://ticketing-php-minzykim.c9users.io/deal_board.php";//
    String request;
    Button btn_dealadd;

    ListView listView=null;
    DealboardlistviewAdapter adapter=null;

    private List<HashMap<String,String>> board_List=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealboard);

        adapter= new DealboardlistviewAdapter();
        listView =(ListView)findViewById(R.id.list_deal);
        listView.setAdapter(adapter);
        board_List=new ArrayList<HashMap<String, String>>();//리스트생성
        btn_dealadd=(Button)findViewById(R.id.btn_dealadd) ;
        request=search_url;
        new JsonLoadingTask().execute();


        btn_dealadd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(LoginActivity.Id.isEmpty()){
                    Toast.makeText(DealboardActivity.this,"로그인 후 작성 가능합니다",Toast.LENGTH_LONG).show();
                }
                else{
                Intent intent = new Intent(getApplicationContext(),WriteActivity.class);
                startActivity(intent);}

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                HashMap<String,String> hashmap =board_List.get(position);
                Intent intent = new Intent(getApplication(),PostActivity.class);
                intent.putExtra("deal_idx",hashmap.get("deal_idx"));
                intent.putExtra("deal_title",hashmap.get("deal_title"));
                intent.putExtra("writer",hashmap.get("writer"));
                intent.putExtra("deal_contents",hashmap.get("deal_contents"));

                startActivity(intent);
            }
        });
    }

    public class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText(request);
        } // doInBackground : 백그라운드 작업을 진행한다.
        @Override
        protected void onPostExecute(String result) {

            for(int i=0;i<board_List.size();i++){
                HashMap<String,String> hashmap =board_List.get(i);
                String title = hashmap.get("deal_title");
                String date = hashmap.get("deal_datetime");
                date=date.substring(2,10);
                String writer = hashmap.get("writer");
                adapter.addItem(title,date,writer);
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
            JSONArray json_result = json.getJSONArray("deal_board");//결과객체

            for(int i=0;i<json_result.length();i++){

                json=json_result.getJSONObject(i);

                String deal_idx = json.getString("deal_idx");//""안에는 변수명
                String deal_title= json.getString("deal_title");
                String deal_contents = json.getString("deal_contents");
                String deal_datetime = json.getString("deal_datetime");
                String writer = json.getString("writer");

                HashMap<String,String> board_map= new HashMap<String,String>();//해시맵에저장


                board_map.put("deal_idx",deal_idx);
                board_map.put("deal_title",deal_title);
                board_map.put("deal_contents",deal_contents);
                board_map.put("deal_datetime",deal_datetime);
                board_map.put("writer",writer);

                board_List.add(board_map);//해시맵 리스트

                Log.d("TEST","hashsize:"+board_map.size());

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
        Log.d("testttttttttttttttttt","test"+pUrl);
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
