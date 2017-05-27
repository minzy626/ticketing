package com.example.minji.ticketing;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class ServertimeActivity extends AppCompatActivity implements View.OnClickListener{
    String search_url = "http://52.79.188.75/api/v1/get_servertime?query=";
    String query ;
    String request;
    TextView tv_Url;
    TextView tv_Date;
    TextView tv_Time;
    EditText et_url;
    Button bt_Searchtime;
    Button bt_melon;
    Button bt_yes24;
    Button bt_auction;
    Button bt_interpark;
    TimerTask second;
    int curHour,curMinute,curSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_servertime);
            tv_Date=(TextView)findViewById(R.id.tv_serverDate);
            tv_Time=(TextView)findViewById(R.id.tv_serverTime);
            tv_Url=(TextView)findViewById(R.id.tv_url);
            et_url=(EditText)findViewById(R.id.et_url);
            bt_Searchtime=(Button)findViewById(R.id.bt_searchtime);
            bt_auction=(Button)findViewById(R.id.btn_auction);
            bt_melon=(Button)findViewById(R.id.btn_melon);
            bt_yes24=(Button)findViewById(R.id.btn_yes24);
            bt_interpark=(Button)findViewById(R.id.btn_interpark);



        bt_interpark.setOnClickListener(this);
        bt_Searchtime.setOnClickListener(this);
        bt_auction.setOnClickListener(this);
        bt_yes24.setOnClickListener(this);
        bt_melon.setOnClickListener(this);

    }
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_searchtime){
            query=et_url.getText().toString();
            if(query.isEmpty()){
                Toast.makeText(getApplicationContext(),"url을 입력해주세요",Toast.LENGTH_SHORT).show();
            }else{
                request=search_url+"http://"+query;
                tv_Url.setText("http://"+et_url.getText().toString());
                new JsonLoadingTask().execute();
            }
        }else if (i == R.id.btn_auction) {
            request=search_url+"http://ticket.auction.co.kr/";
            tv_Url.setText("http://ticket.auction.co.kr/");
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_melon) {
            request=search_url+"http://ticket.melon.com/";
            tv_Url.setText("http://ticket.melon.com/");
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_yes24) {
            request=search_url+"http://ticket.yes24.com/";
            tv_Url.setText("http://ticket.yes24.com/");
            new JsonLoadingTask().execute();
        }else if (i == R.id.btn_interpark) {
            request=search_url+"http://ticket.interpark.com/";
            tv_Url.setText("http://ticket.interpark.com/");
            new JsonLoadingTask().execute();
        }
    }



    public class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText(request);
        } // doInBackground : 백그라운드 작업을 진행한다.
        @Override
        protected void onPostExecute(String result) {

           String datetime=result;
            String [] DTvalue =datetime.split(" ");//요일0,날짜1,월2,년3,시간4,gmt5
            for(int i=0;i<12;i++){
                if(DTvalue[2].equals("May")){
                    DTvalue[2]=DTvalue[2].replace("May","5");
                }
            }
            String time =DTvalue[4];
            String [] Tvalue=time.split(":");//시0,분1,초2
            int hour = Integer.parseInt(Tvalue[0]);
            int minute= Integer.parseInt(Tvalue[1]);
            int second= Integer.parseInt(Tvalue[2]);

            tv_Date.setText(DTvalue[3]+" 년 "+DTvalue[2]+" 월 "+DTvalue[1]+" 일 ");
            tv_Time.setText((hour+9)+" : "+minute+" : "+second);

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

            String result = json.getString("result");//변수 뽑아내기

            sb.append(result);

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
