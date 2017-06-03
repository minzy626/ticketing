package com.example.minji.ticketing;

/**
 * Created by minji on 2017-06-03.
 */

public class sql_control {
    public static void pwUpdate(String id,String pw){ //changing pw
        controlMysql pwchange=new controlMysql(id,pw,0);
        controlMysql.active=true;
        pwchange.start();
    }
    public static void sendingPW(String pw,String mail){ //changing pw
        controlMysql sendingPW=new controlMysql(pw,mail);
        controlMysql.active=true;
        sendingPW.start();
    }
    public static void findingPW(String id,String Name,String Mail){ //changing pw
        controlMysql findingpw=new controlMysql(id,Name,Mail);
        controlMysql.active=true;
        findingpw.start();
    }

    static public void get_userInfo(String id,int type){ //getting information
        controlMysql getinfo=new controlMysql(id,type);
        controlMysql.active=true;
        getinfo.start();
    }

    static public void userUpdate(String id,String name,String age,String phone,String mail,String address){   //changing information
        controlMysql updateinfo=new controlMysql(id,name,age,phone,mail,address);
        controlMysql.active=true;
        updateinfo.start();
    }
    static public void userRegist(String id,String name,String phone,String pw){    //sign up
        controlMysql registinfo=new controlMysql(id,name,phone,pw);
        controlMysql.active=true;
        registinfo.start();
    }


    static public void idChk(String id){    //id check
        controlMysql idchk=new controlMysql(id,2);
        controlMysql.active=true;
        idchk.start();
    }
}
