package com.everstone.crm.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.everstone.crm.R;
import com.everstone.crm.activity.MainActivity;
import com.everstone.crm.adapter.MyAdapter;
import com.everstone.crm.database.MyOpenHelper;
import com.everstone.crm.database.UserDAO;
import com.everstone.crm.entity.User;
import com.everstone.crm.util.ToastUtil;

import java.util.List;

public class UserSettingsFragment extends Fragment {

    private TextView searchButton;
    private EditText search;
    private ImageView clear;
    private ListView listView;
    MyOpenHelper myOpenHelper;
    List<User> userList;

    /**
     * 加载UserSettingFragment的View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化View，搜索框中输入文字后会实时进行模糊查询用户，并出现删除搜索文字的图片
     * @param view
     */
    private void initView(View view){
        search = (EditText)view.findViewById(R.id.search);
        searchButton = (TextView)view.findViewById(R.id.search_button);
        clear = (ImageView)view.findViewById(R.id.clear);
        listView = (ListView)view.findViewById(R.id.listview);

        /**
         * 点击clear图片之后清空搜索框文字，并设置图片不可见
         */
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
                clear.setVisibility(View.INVISIBLE);
            }
        });

        /**
         * 搜索框文字变动监听器
         */
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * 搜索框没有文字时，不出现clear图片
             * 有文字时，每当文字发生变动都更新ListView，展示搜索结果，并出现clear图片
             * @param charSequence
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    clear.setVisibility(View.INVISIBLE);
                }else{
                    clear.setVisibility(View.VISIBLE);
                    showListView(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /**
         * 对TextView设置监听器，当没有文字的时候出现提示信息
         */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(search.getText().length() == 0){
                    ToastUtil.show(getActivity(), "请输入要搜索的内容", 3);
                }
            }
        });
    }

    /**
     * 通过传入char sequence字符串
     * 使用fuzzyQuery方法进行模糊查询
     * 为搜索结果的listView设置Item监听器，用户点击后跳转到拨打电话的界面，直接拨打电话
     * @param charSequence
     */
    private void showListView(CharSequence charSequence){
        listView.setVisibility(View.VISIBLE);
        myOpenHelper = new MyOpenHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = myOpenHelper.getReadableDatabase();
        UserDAO userDAO = new UserDAO(sqLiteDatabase);
        userList = userDAO.fuzzyQuery(String.valueOf(charSequence));
        MyAdapter myAdapter = new MyAdapter(userList, getContext());
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:"+userList.get(i).getNumber());
                intent.setData(data);
                startActivity(intent);
            }
        });
    }
}
