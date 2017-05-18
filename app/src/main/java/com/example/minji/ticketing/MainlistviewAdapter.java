package com.example.minji.ticketing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MainlistviewAdapter extends BaseAdapter{

    public ArrayList<MainlistviewItem> listViewItemList = new ArrayList< MainlistviewItem >() ;
    private MainlistviewAdapter mApdapter =null;

    public MainlistviewAdapter(){
        //생성자
    };

    public int getCount(){
        return listViewItemList.size();
    }
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos =position;
        final Context context =parent.getContext();

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mainlistview_item, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.title) ;
        TextView dateTextView = (TextView) convertView.findViewById(R.id.date) ;
        TextView conTextView = (TextView) convertView.findViewById(R.id.contents) ;
        TextView spaceTextView = (TextView) convertView.findViewById(R.id.space) ;

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MainlistviewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitle());
        dateTextView.setText(listViewItem.getDate());
        conTextView.setText(listViewItem.getCon());
        spaceTextView.setText(listViewItem.getSpace());


        return convertView;
        }
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String date, String con, String space) {
        MainlistviewItem item = new MainlistviewItem();

        item.setTitle(title);
        item.setDate(date);
        item.setCon(con);
        item.setSpace(space);



        listViewItemList.add(item);
    }
}
