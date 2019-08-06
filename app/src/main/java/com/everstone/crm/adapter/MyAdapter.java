package com.everstone.crm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.everstone.crm.R;
import com.everstone.crm.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<User> userList;
    private LayoutInflater inflater;

    public MyAdapter(List<User> userList, Context context){
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(userList == null){
            return -1;
        }else {
            return userList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView = inflater.inflate(R.layout.user_item, null);
        User user = (User)getItem(i);
        TextView username = (TextView)myView.findViewById(R.id.item_username);
        TextView number = (TextView)myView.findViewById(R.id.item_number);
        if(getCount() == -1){
            username.setText("未找到！");
            number.setText("请检查输入！");
        }else {
            username.setText(user.getUsername());
            number.setText(user.getNumber());
        }
        return myView;
    }
}
