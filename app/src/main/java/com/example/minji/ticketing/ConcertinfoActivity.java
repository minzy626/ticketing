package com.example.minji.ticketing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

public class ConcertinfoActivity extends AppCompatActivity {
    String search_url = "http://52.79.188.75/api/v1/one_ticket?ticket_id=";//티켓하나정보
    String request;

    private Button gosite;
    private TextView coninfo;
    private ImageView conimg;
    private String ticket_id;
    private HashMap<String,String> concert_infomap=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concertinfo);

        gosite=(Button)findViewById(R.id.bt_gosite);
        conimg=(ImageView)findViewById(R.id.view_conimg);
        coninfo=(TextView)findViewById(R.id.tv_detailcon);

        final Intent intent = getIntent();
        ticket_id =intent.getExtras().getString("id");//전페이지에서 넘겨받으 id저장
        concert_infomap=new  HashMap<String,String> ();//해시맵

        Log.d("test"," detaiddddddddddddddddddddddddddddddddddddddddddddddddddl  "+ticket_id);

        request=search_url+ticket_id;//리퀘스트 초기화
        new JsonLoadingTask().execute();//실행



        gosite.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(concert_infomap.get("site_url"))));
            }
        });//버튼리스터
        coninfo.setMovementMethod(new ScrollingMovementMethod());//텍스트뷰 스크롤 활성화



    }
    public class JsonLoadingTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            return getJsonText(request);//getJsonText반환
        } // doInBackground : 백그라운드 작업을 진행한다.
        @Override
        protected void onPostExecute(String result) {
            String detail_info =concert_infomap.get("detail_info");//상세정보 string에저장
            detail_info=detail_info.replaceAll("-","\n");//-를 구분으로 개행처리
            coninfo.setText(detail_info);//setText
            new DownloadImageTask((ImageView) findViewById(R.id.view_conimg))
                    .execute(concert_infomap.get("image_url"));//url이미지 파싱 작업

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

                for(int i=0;i<json.length();i++){//객체크기만큼 반복

                    String concert_name = json.getString("concert_name");//""안에는 변수명
                    Log.d("TEST","Test:"+concert_name );//제대로 읽어오나 확인 위한 Log처리
                    String detail_info = json.getString("detail_info");
                    Log.d("TEST","Test:"+detail_info);
                    String site_url = json.getString("site_url");
                    Log.d("TEST","Test:"+site_url);
                    String open_date = json.getString("open_date");
                    Log.d("TEST","Test:"+open_date);
                    String image_url = json.getString("image_url");
                    Log.d("TEST","Test:"+image_url);
                    String source_site = json.getString("source_site");


                    if(source_site.equals("1")){//정수화해놓은 사이트명을 문자열로 교체
                        source_site=source_site.replace("1","인터파크");
                    }else if(source_site.equals("2")){
                        source_site=source_site.replace("2","멜론티켓");
                    }else if(source_site.equals("3")){
                        source_site=source_site.replace("3","옥션티켓");
                    }else if(source_site.equals("4")){
                        source_site=source_site.replace("4","YES24");
                    }
                    //각 변수명으로 추출한 객체를 해시맵에저장
                    concert_infomap.put("concert_name",concert_name);
                    concert_infomap.put("site_url",site_url);
                    concert_infomap.put("detail_info",detail_info);
                    concert_infomap.put("open_date",open_date);
                    concert_infomap.put("image_url",image_url);
                    concert_infomap.put("source_site",source_site);

                    Log.d("TEST","hashsize:"+concert_infomap.size());
                    //해시맵에 들어갔나 확인위한 로그처리
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
/*    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }*/
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
