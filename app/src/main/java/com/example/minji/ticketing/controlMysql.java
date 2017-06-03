package com.example.minji.ticketing;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by minji on 2017-06-03.
 */

public class controlMysql extends Thread {
    public static boolean active=false;
    Handler mHandler=new Handler();
    String url=null;
    int gettype=0;

    public controlMysql(String id, String name, String mail) { //for update

        //String findingpw="http://answerofgod.honor.es/study/android/login/sql/findingpw.php?id=";
        String findingpw="http://192.168.0.237/study/android/login/sql/findingpw.php?id=";  //your server IP
        String userId=id;
        String nameuser="&name="+name;

        String mailuser="&mail="+mail;

        url=findingpw+userId+nameuser+mailuser;
        gettype=1;
    }

    public controlMysql(String pw, String mail) { //for update

        String findingpw="http://192.168.0.237/study/android/login/sql/sendingpw.php?msg=";     //your server IP
        //String findingpw="http://answerofgod.honor.es/study/android/login/sql/sendingpw.php?msg=";
        String msg=pw;
        String mailuser="&mail="+mail;

        url=findingpw+msg+mailuser;
        gettype=4;
    }

    public controlMysql(String id, String name, String age, String phone, String mail, String address){ //for update

        String userinfo_url="http://192.168.0.237/study/android/login/sql/updateUser.php?id=";      //your server IP
        //String userinfo_url="http://answerofgod.honor.es/study/android/login/sql/updateUser.php?id=";
        String userId=id;
        String nameuser="&name="+name;
        String ageuser="&age="+age;
        String phoneuser="&phone="+phone;
        String mailuser="&mail="+mail;
        String addressuser="&address="+address;
        url=userinfo_url+userId+nameuser+ageuser+phoneuser+mailuser+addressuser;
        gettype=0;
    }


    public controlMysql(String id, String name, String phone, String pw){ //for register
        //String insert_userinfo_url="http://answerofgod.honor.es/study/android/login/sql/insertUser.php?id=";
        String insert_userinfo_url="http://192.168.0.237/study/android/login/sql/insertUser.php?id=";       //your server IP
        String userId=id;
        String nameuser="&name="+name;
        String phoneuser="&phone="+phone;
        String pwuser="&pw="+pw;
        url=insert_userinfo_url+userId+nameuser+phoneuser+pwuser;
        gettype=3;
    }

    public controlMysql(String id, String no, int type){
        // String pwinfo_url="http://answerofgod.honor.es/study/android/login/sql/updatePw.php?id=";
        String pwinfo_url="http://192.168.0.237/study/android/login/sql/updatePw.php?id=";      //your server IP
        String userId=id;


        Log.e("active", active + "");

        switch(type){
            case 0:
                String userPw="&pw="+no;
                url=pwinfo_url+userId+userPw;
                gettype=2;
                break;

        }


    }


    public controlMysql(String id,int type){
        //String userinfo_url="http://answerofgod.honor.es/study/android/login/sql/getuserinfo.php?id=";
        //String idchk_url="http://answerofgod.honor.es/study/android/login/sql/chkid.php?id=";
        String userinfo_url="http://192.168.0.237/study/android/login/sql/getuserinfo.php?id=";     //your server IP
        String idchk_url="http://192.168.0.237/study/android/login/sql/chkid.php?id=";      //your server IP
        String userId=id;

        switch(type){
            case 0:
                url=userinfo_url+userId;
                gettype=6;
                break;

            case 2:
                url=idchk_url+userId;
                gettype=8;
                break;


        }


    }


    /**
     * Calls the <code>run()</code> method of the Runnable object the receiver
     * holds. If no Runnable is set, does nothing.
     *
     * @see Thread#start
     */
    @Override
    public void run() {
        super.run();
        if(active){
            Log.e("gettype",gettype+","+url);
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL phpUrl = new URL(url);

                HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while ( true ) {
                            String line = br.readLine();
                            if ( line == null )
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            show(jsonHtml.toString());
        }



    }

    void show(final String result){
        mHandler.post(new Runnable(){


            @Override
            public void run() {

                switch (gettype){

                 /*   case 1:
                        Login.result_findingpw(result);
                        break;
                    case 3:
                        Login.regist_result(result);
                        break;
                    case 4:
                        Login.result_sendingpw(result);
                        break;
*/
                    case 8:
                        chkIdActivity.chkidresult(result);
                        break;
                    case 14:
                /*        Login.regist_result(result);
                        break;
*/
                    default:
                        break;
                }
            }
        });

    }

}
