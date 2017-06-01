package com.example.minji.ticketing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by minji on 2017-05-31.
 */

public class Cal_listAdapter extends BaseAdapter {
    public ArrayList< Cal_Listview_Item > callistitemList= new ArrayList< Cal_Listview_Item>() ;
    private Cal_listAdapter mApdapter =null;

    public Cal_listAdapter() {
    }
    public int getCount(){
        return callistitemList.size();
    }
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos =position;
        final Context context =parent.getContext();

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cal_listitem, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.tv_caltitle) ;
        TextView monthTextView = (TextView) convertView.findViewById(R.id.tv_calmonth) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Cal_Listview_Item listViewItem = callistitemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitle());
        monthTextView.setText(listViewItem.getMonth());



        return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return callistitemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String month, String title ) {

        Cal_Listview_Item item = new Cal_Listview_Item();

        item.setMonth(month);
        item.setTitle(title);


    callistitemList.add(item);
    }
    public void remove (){
        callistitemList.clear();

    }

}
