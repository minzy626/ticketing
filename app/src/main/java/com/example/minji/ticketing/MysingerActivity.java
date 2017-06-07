package com.example.minji.ticketing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MysingerActivity extends AppCompatActivity {
    String search_url = "https://ticketing-php-minzykim.c9users.io/singerlist.php?idx=";//
    String request;

    InputMethodManager imm;
    ImageView imageView3;
    EditText et_singer;
    Button btn_reg;
    static String singer;
    ListView listView;
    MysingerlistviewAdapter adapter;
    ArrayList<String> singers ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysinger);


        adapter= new MysingerlistviewAdapter();
        listView =(ListView)findViewById(R.id.list_singer);
        listView.setAdapter(adapter);

        request=search_url+LoginActivity.Index;
        new JsonLoadingTask().execute();
        singers =new ArrayList<String>();

        et_singer=(EditText)findViewById(R.id.et_singer);
        btn_reg=(Button)findViewById(R.id.btn_singeradd);


        btn_reg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                singer = et_singer.getText().toString();

                if(singer.length()==0){
                    Toast.makeText(getApplicationContext(),"가수를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{

                    InsertData task = new InsertData();
                    task.execute( LoginActivity.Index,singer);
                }

            }
        });

    }
    class InsertData extends AsyncTask<String, Void, String> { //<onPre parameter, doInBack parameter, onPost parameter>
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() { //Asynctask 실행 했을 때 뜨는 화면
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MysingerActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {//Asynctask 완료 후 뜨는 화면
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result.equals("SQL문 처리 성공")) {
                Toast.makeText(MysingerActivity.this, "등록되었습니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MysingerActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MysingerActivity.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
            Log.d("Test", "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) { //background

            String idx = (String) params[0];
            String keyword = (String) params[1];


            String serverURL = "https://ticketing-php-minzykim.c9users.io/add_singer.php";
            String postParameters = "idx=" + idx + "&keyword=" + keyword ;
            Log.d("write", "POST response  - " + postParameters);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("test", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d("TEst", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }


    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(et_singer.getWindowToken(), 0);
    }
    public class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText(request);
        } // doInBackground : 백그라운드 작업을 진행한다.
        @Override
        protected void onPostExecute(String result) {

            for(int i=0;i<singers.size();i++){
                String keyword = singers.get(i);
                adapter.addItem(keyword);
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
            JSONArray json_result = json.getJSONArray("singer_list");//결과객체

            for(int i=0;i<json_result.length();i++){

                json=json_result.getJSONObject(i);

                String singer = json.getString("singer");
                singers.add(singer);

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

