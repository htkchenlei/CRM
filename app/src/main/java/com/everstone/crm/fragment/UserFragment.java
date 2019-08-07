package com.everstone.crm.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.everstone.crm.R;
import com.everstone.crm.activity.UserInfoActivity;
import com.everstone.crm.database.MyOpenHelper;
import com.everstone.crm.database.UserDAO;
import com.everstone.crm.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFragment extends Fragment implements AdapterView.OnItemClickListener {

    private MyOpenHelper myOpenHelper;
    private List<User> userList;
    private List<Map<String, String>> dataList;
    private SimpleAdapter simpleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);
        Log.d("open", " CRM");
        return view;
    }

    /**
     * 查找全部user，并将其展示在userfragment上
     */
    private void initData(){
        myOpenHelper = new MyOpenHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = myOpenHelper.getReadableDatabase();
        UserDAO userDAO = new UserDAO(sqLiteDatabase);
        userList = userDAO.queryAll();
        sqLiteDatabase.close();
        dataList = new ArrayList<>();
        for(User user : userList){
            Map<String, String> map = new HashMap<>();
            map.put("username", user.getUsername());
            map.put("number", user.getNumber());
            map.put("address", user.getAddress());
            dataList.add(map);
        }
    }

    /**
     * c初始化View，展示在UserFragment上
     * @param view
     */
    private void initView(View view){
        ListView listView = (ListView)view.findViewById(R.id.userList);
        simpleAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.user_item, new String[]{"username", "number", "address"}, new int[]{R.id.item_username, R.id.item_number, R.id.item_address});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(this);
    }

    /**
     * 点击用户信息后，会自动跳转到UserInfoActivity去展示全面的用户信息
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("id", userList.get(i).getId());
        startActivity(intent);
    }
}
