package com.example.minji.ticketing;

/**
 * Created by minji on 2017-05-17.
 */

public class MainlistviewItem {
    private String titleStr;
    private String dateStr;
    private String spaceStr;



    public void setTitle(String title){
        titleStr = title;
    }

    public void setDate(String date){
        dateStr = date;
    }

    public void setSpace(String space) { spaceStr = space;   }

    public String getTitle(){
        return this.titleStr;
    }

    public String getDate() { return this.dateStr;  }

    public String getSpace(){
        return this.spaceStr;
    }
}
