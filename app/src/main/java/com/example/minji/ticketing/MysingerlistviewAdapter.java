package com.example.minji.ticketing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by minji on 2017-05-21.
 */

public class MysingerlistviewAdapter  extends BaseAdapter {

    public ArrayList<MysingerlistviewItem> singerlistViewItemList= new ArrayList< MysingerlistviewItem >() ;
    private MysingerlistviewAdapter mApdapter =null;
    public MysingerlistviewAdapter() {
    }
    public int getCount(){
        return singerlistViewItemList.size();
    }
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos =position;
        final Context context =parent.getContext();

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mysinger_item, parent, false);
        }

        TextView singerTextView = (TextView) convertView.findViewById(R.id.tv_singer) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MysingerlistviewItem listViewItem = singerlistViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        singerTextView.setText(listViewItem.getSingerStr());



        return convertView;
    }
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return singerlistViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String singer) {

        MysingerlistviewItem item = new MysingerlistviewItem();

        item.setSingerStr(singer);



        singerlistViewItemList.add(item);
    }

}
