package com.example.minji.ticketing;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

/**
 * Created by JIYOUNG on 2017-05-23.
 */

public class CommentActivity extends AppCompatActivity {
    String search_url = "https://ticketing-php-minzykim.c9users.io/replylist.php?idx=";//
    String request;

    InputMethodManager imm;
    ImageView imageView2;
    EditText et_postcomment;
    ImageButton btn_input;
    String comment;

    private List<HashMap<String,String>> comment_List=null;
    ListView listView=null;
    DealboardlistviewAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        final Intent intent = getIntent();
        String deal_idx=intent.getExtras().getString("deal_idx");//전페이지에서 넘겨받은것 저장

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        btn_input=(ImageButton)findViewById(R.id.btn_input);
        adapter= new   DealboardlistviewAdapter();
        comment_List=new ArrayList<HashMap<String, String>>();//리스트생성
        listView =(ListView)findViewById(R.id.list_comment);
        listView.setAdapter(adapter);

        request=search_url+deal_idx;
        new JsonLoadingTask().execute();

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        et_postcomment = (EditText)findViewById(R.id.et_postcomment);
        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment=et_postcomment.getText().toString();

            }
        });

    }

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(et_postcomment.getWindowToken(), 0);
    }


    public class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText(request);
        } // doInBackground : 백그라운드 작업을 진행한다.
        @Override
        protected void onPostExecute(String result) {

            for(int i=0;i<comment_List.size();i++){
                HashMap<String,String> hashmap =comment_List.get(i);
                String contents = hashmap.get("com_contents");
                String date = hashmap.get("com_datetime");
                Log.d("testttttttttttttttttt","test"+date);
                date=date.substring(2,10);
                String writer = hashmap.get("mem_idx");
                adapter.addItem(contents,date,writer);
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
            JSONArray json_result = json.getJSONArray("com_list");//결과객체

            for(int i=0;i<json_result.length();i++){

                json=json_result.getJSONObject(i);

                String deal_idx = json.getString("deal_idx");//""안에는 변수명
                String mem_idx= json.getString("mem_idx");
                String com_contents = json.getString("com_contents");
                String com_datetime = json.getString("com_datetime");

                HashMap<String,String> com_map= new HashMap<String,String>();//해시맵에저장


                com_map.put("deal_idx",deal_idx);
                com_map.put("mem_idx",mem_idx);
                com_map.put("com_contents",com_contents);
                com_map.put("com_datetime",com_datetime);

                comment_List.add(com_map);//해시맵 리스트

                Log.d("TEST","hashsize:"+com_map.size());

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
