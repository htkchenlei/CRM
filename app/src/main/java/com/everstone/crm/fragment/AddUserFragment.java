package com.everstone.crm.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.everstone.crm.R;
import com.everstone.crm.database.MyOpenHelper;
import com.everstone.crm.database.UserDAO;
import com.everstone.crm.entity.User;
import com.everstone.crm.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddUserFragment extends Fragment{

    private EditText editTextUsername, editTextNumber, editTextAddress, editTextSex, editTextDate;
    private Button buttonSubmit, buttonReset;
    private RadioGroup radioGroup;
    //private RadioButton radioButtonMale, radioButtonFemale;
    private String username, number, address, sex, date;
    private MyOpenHelper myOpenHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        editTextUsername = (EditText)view.findViewById(R.id.add_username);
        editTextNumber = (EditText)view.findViewById(R.id.add_number);
        editTextAddress = (EditText)view.findViewById(R.id.add_address);
        editTextDate = (EditText)view.findViewById(R.id.add_date);
        radioGroup = (RadioGroup) view.findViewById(R.id.add_sex);
        //radioButtonMale = (RadioButton)view.findViewById(R.id.male);
        //radioButtonFemale = (RadioButton)view.findViewById(R.id.female);
        buttonReset = (Button)view.findViewById(R.id.add_user_reset);
        buttonSubmit = (Button)view.findViewById(R.id.add_user_submit);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String dateString = sdf.format(new Date());
        editTextDate.setText(dateString);
        Log.d("initView", "init button listener");

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextUsername.setText("");
                editTextAddress.setText("");
                editTextNumber.setText("");
                editTextSex.setText("");
                editTextDate.setText(dateString);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.male:
                        sex = "男";
                        break;
                    case R.id.female:
                        sex = "女";
                        break;
                }
                username = editTextUsername.getText().toString();
                number = editTextNumber.getText().toString();
                address = editTextAddress.getText().toString();
                date = editTextDate.getText().toString();
                boolean userInfoFlag = checkUserInfo(username, number, address, sex, date);
                if(userInfoFlag) {
                    boolean userSaveFlag = saveUser();
                    if(userSaveFlag) {
                        ToastUtil.show(getActivity(), "新增用户完成", 3);
                    }
                }else{
                    ToastUtil.show(getActivity(), "用户信息有误", 3);
                }

            }
        });
    }

    private boolean saveUser(){
        User user = new User();
        myOpenHelper = new MyOpenHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = myOpenHelper.getReadableDatabase();
        UserDAO userDAO = new UserDAO(sqLiteDatabase);
        user.setUsername(username);
        user.setNumber(number);
        user.setAddress(address);
        user.setDate(date);
        user.setSex(sex);
        boolean flag = userDAO.addUser(user);
        return flag;
    }

    private boolean checkUserInfo(String username, String number, String address, String sex, String date){
        boolean userInfoFlag = true;
        if(username.isEmpty() || username.trim().equals("")){
            userInfoFlag = false;
        }
        if(number.isEmpty() || number.trim().equals("") || number.length() != 11){
            userInfoFlag = false;
        }
        if(address.isEmpty() || address.trim().equals("")){
            userInfoFlag = false;
        }
        if(sex.isEmpty() || sex.trim().equals("")){
            userInfoFlag = false;
        }
        if(date.isEmpty() || date.trim().equals("")){
            userInfoFlag = false;
        }
        return userInfoFlag;
    }
}
