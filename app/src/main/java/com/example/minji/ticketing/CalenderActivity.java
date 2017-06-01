package com.example.minji.ticketing;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalenderActivity extends AppCompatActivity implements View.OnClickListener{
    String search_url = "http://52.79.188.75/api/v1/search_by_name?query=";//
    String query;
    String request;
    private List<HashMap<String,String>> concert_infoList=null;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12;
    ListView listView=null;
    Cal_listAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        adapter= new Cal_listAdapter();
        concert_infoList=new ArrayList<HashMap<String, String>>();//리스트생성

        listView =(ListView)findViewById(R.id.list_cal);
        listView.setAdapter(adapter);

        request=search_url+query;

        new JsonLoadingTask().execute();

        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_10).setOnClickListener(this);
        findViewById(R.id.btn_11).setOnClickListener(this);
        findViewById(R.id.btn_12).setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                HashMap<String,String> hashmap =concert_infoList.get(position);
                Intent intent = new Intent(getApplication(),ConcertinfoActivity.class);
                intent.putExtra("id",hashmap.get("id"));
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
            for(int i=0;i<concert_infoList.size();i++){
                HashMap<String,String> hashmap =concert_infoList.get(i);
                String title = hashmap.get("concert_name");
                String month= hashmap.get("월");
                adapter.addItem(month,title);
                adapter.notifyDataSetChanged();//결과물 리스트뷰로 출력

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

            int results = Integer.parseInt(result_count);//결과 개수확인


            if(results==0){//결과개수가 없다면

            }else{//결과값이 있다면 해쉬맵에 저장
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
                    }//소스 정수로 구분된것 문자로 변환

                    concert_infomap.put("concert_name",concert_name);
                    concert_infomap.put("site_url",site_url);
                    concert_infomap.put("detail_info",detail_info);
                    concert_infomap.put("open_date",open_date);
                    concert_infomap.put("image_url",image_url);
                    concert_infomap.put("source_site",source_site);
                    concert_infomap.put("id",id);
                    concert_infomap.put("월",query.substring(6,8));

                    concert_infoList.add(concert_infomap);//해시맵 리스트에 추가

                    Log.d("TEST","hashsize:"+concert_infomap.size());

                }
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

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_1) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 1월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_2) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 2월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_3) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 3월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_4) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 4월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_5) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 5월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_6) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 6월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_7) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 7월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_8) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 8월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_9) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 9월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_10) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 10월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_11) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 11월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_12) {
            concert_infoList.clear();
            adapter.remove();
            adapter.notifyDataSetChanged();
            query="2017년 12월";
            request=search_url+query;
            new JsonLoadingTask().execute();
        }
    }



}
