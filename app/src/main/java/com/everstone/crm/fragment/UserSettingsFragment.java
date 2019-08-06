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
    private ImageView delete;
    private ListView listView;
    MyOpenHelper myOpenHelper;
    List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        search = (EditText)view.findViewById(R.id.search);
        searchButton = (TextView)view.findViewById(R.id.search_button);
        delete = (ImageView)view.findViewById(R.id.delete);
        listView = (ListView)view.findViewById(R.id.listview);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
                delete.setVisibility(View.INVISIBLE);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    delete.setVisibility(View.INVISIBLE);
                }else{
                    delete.setVisibility(View.VISIBLE);
                    showListView(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(search.getText().length() == 0){
                    ToastUtil.show(getActivity(), "请输入要搜索的内容", 3);
                }
            }
        });
    }

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
