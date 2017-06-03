package com.example.minji.ticketing;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by minji on 2017-06-03.
 */

public class loginMysql extends Thread {
    public static boolean active=false;

    Handler mHandler;
    String userId=null,userPw=null,url=null;
    //String login_url="http://answerofgod.honor.es/study/android/login/sql/chkid.php?id=";
    String login_url="https://ticketing-php-minzykim.c9users.io/login.php?id=";

    public loginMysql(String id,String pw){
        mHandler=new Handler();
        userId=id;
        // userPw=pw;
        url=login_url+userId;

    }

    /**
     * Calls the <code>run()</code> method of the Runnable object the receiver
     * holds. If no Runnable is set, does nothing.
     *
     * @see Thread#start
     */
    @Override
    public void run() {
        BufferedReader bufreader=null;
        HttpURLConnection urlConnection = null;
        super.run();
        if(active){
            StringBuffer jsonHtml = new StringBuffer();
            try {
                URL phpUrl = new URL(url);
                urlConnection = (HttpURLConnection) phpUrl.openConnection();
                InputStream contentStream = urlConnection.getInputStream();

                //  HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection();
                bufreader = new BufferedReader(new InputStreamReader(contentStream,"UTF-8"));
                String line = null;
                StringBuilder sb = new StringBuilder();

                //버퍼의 웹문서 소스를 줄단위로 읽어(line), Page에 저장함
                while((line = bufreader.readLine())!=null){
                    Log.d("line:",line);
                    line.replaceAll("\n","");
                    jsonHtml.append(line);
                }

            } catch ( Exception e ) {
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
            show(jsonHtml.toString());
            Log.d("testHTML","test "+jsonHtml.toString());
        }
    }

    void show(final String result){
        mHandler.post(new Runnable(){

            @Override
            public void run() {
                String getid="";
                String getpw="";
                String getname="";

                try {

                    JSONObject jObject = new JSONObject(result);
                    JSONArray json_result=jObject.getJSONArray("member_info");
                    for(int i=0;i<json_result.length();i++) {

                        jObject=json_result.getJSONObject(i);

                        getid = jObject.getString("id");  //php에서 받아온 id, password, name 값을 저장
                        getpw = jObject.getString("password");
                        getname = jObject.getString("name");
                        Log.d("TESTtttttttttttttttttt", "Test:" + getid);

                    }

                    LoginActivity.result_login(getid, getpw, getname); //Login.java파일로 가서 수정
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoginActivity.result_login("false", "false", "false");
                }
            }
        });

    }

}
