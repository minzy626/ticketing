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

public class TicketopenActivity extends AppCompatActivity {
    String search_url = "http://52.79.188.75/api/v1/tickets?per_page=";//
    String per_page="20";//보일 개수
    String page_no="1";
    String request;
    int page;
    ListView listView=null;
    MainlistviewAdapter adapter=null;
    Button btn_more;

    private List<HashMap<String,String>> concert_infoList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketopen);

        adapter= new MainlistviewAdapter();
        concert_infoList=new ArrayList<HashMap<String, String>>();//리스트생성

        listView =(ListView)findViewById(R.id.lv_openlist);
        listView.setAdapter(adapter);

        View footer = getLayoutInflater().inflate(R.layout.ticketopen_footer,null,false);
        listView.addFooterView(footer);

        btn_more =(Button)findViewById(R.id.btn_footer);
        request=search_url+per_page+"&page_no="+page_no;

        new JsonLoadingTask().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                HashMap<String,String> hashmap =concert_infoList.get(position);
                Intent intent = new Intent(getApplication(),ConcertinfoActivity.class);
                intent.putExtra("id",hashmap.get("id"));
                startActivity(intent);
            }
        });
        btn_more.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                page=1+Integer.parseInt(page_no);
                request=search_url+per_page+"&page_no"+Integer.toString(page);
                new JsonLoadingTask().execute();
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
