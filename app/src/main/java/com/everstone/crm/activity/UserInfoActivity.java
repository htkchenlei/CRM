package com.everstone.crm.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.everstone.crm.R;
import com.everstone.crm.database.MyOpenHelper;
import com.everstone.crm.database.UserDAO;
import com.everstone.crm.entity.User;
import com.everstone.crm.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView username, number, address, sex, date ;
    private User user;
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Button edit, delete;
    private int EDITABLE_FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    public void initView(){
        myOpenHelper = new MyOpenHelper(this);
        sqLiteDatabase = myOpenHelper.getReadableDatabase();

        username = (TextView)findViewById(R.id.info_username);
        number = (TextView)findViewById(R.id.info_number);
        address = (TextView)findViewById(R.id.info_address);
        sex = (TextView)findViewById(R.id.info_sex);
        date = (TextView)findViewById(R.id.info_date);
        edit = (Button)findViewById(R.id.edit);
        delete = (Button)findViewById(R.id.delete);

        edit.setOnClickListener(this);
        delete.setOnClickListener(this);

        UserDAO userDAO = new UserDAO(sqLiteDatabase);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = String.valueOf( bundle.getInt("id"));
        user = userDAO.queryUser(id);
        username.setText(user.getUsername());
        number.setText(user.getNumber());
        address.setText(user.getAddress());
        sex.setText(user.getSex());
        date.setText(user.getDate());
    }

    /**
     * 通过设置flag来判断当前用户操作
     * 从而决定两个button显示的文字和作用
     * @param view
     */
    @Override
    public void onClick(View view) {
        //Log.d("flag = ", String.valueOf(EDITABLE_FLAG));

        switch (view.getId()){
            case R.id.edit:
                if(EDITABLE_FLAG == 0){
                    edit.setText(R.string.save);
                    delete.setText(R.string.reset);
                    EDITABLE_FLAG = 1;

                    username.setEnabled(true);
                    number.setEnabled(true);
                    number.setInputType(InputType.TYPE_CLASS_NUMBER);
                    address.setEnabled(true);
                    sex.setEnabled(true);
                    date.setEnabled(true);
                    break;
                }else if(EDITABLE_FLAG == 1){
                    edit.setText(R.string.edit);
                    delete.setText(R.string.delete);
                    EDITABLE_FLAG = 0;

                    user.setUsername(String.valueOf(username.getText()));
                    user.setNumber(String.valueOf(number.getText()));
                    user.setAddress(String.valueOf(address.getText()));
                    user.setSex(String.valueOf(sex.getText()));
                    UserDAO userDAO = new UserDAO(sqLiteDatabase);
                    boolean result = userDAO.updateUser(user);
                    if (result) {
                        username.setEnabled(false);
                        number.setEnabled(false);
                        address.setEnabled(false);
                        sex.setEnabled(false);
                        date.setEnabled(false);
                        ToastUtil.show(this, "更新成功", 3);
                    } else {
                        ToastUtil.show(this, "false", 3);
                    }
                    break;
                }
            case R.id.delete:
               if(EDITABLE_FLAG == 0){
                   AlertDialog.Builder builder = new AlertDialog.Builder(this);
                   builder.setTitle(user.getUsername());
                   builder.setMessage("确定要删除该用户吗？");
                   builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           UserDAO userDAO = new UserDAO(sqLiteDatabase);
                           if(userDAO.deleteUser(user)) {
                               ToastUtil.showShort(UserInfoActivity.this, "删除成功");
                               Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                               startActivity(intent);
                           }else{
                               ToastUtil.showShort(UserInfoActivity.this, "删除不成功");
                           }
                       }
                   });
                   builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           edit.setText(R.string.edit);
                           delete.setText(R.string.delete);
                           EDITABLE_FLAG = 0;
                           username.setEnabled(false);
                           number.setEnabled(false);
                           address.setEnabled(false);
                           sex.setEnabled(false);
                           date.setEnabled(false);
                       }
                   });
                   builder.show();
               }else if(EDITABLE_FLAG == 1){
                   edit.setText(R.string.save);
                   delete.setText(R.string.reset);

                   username.setText(user.getUsername());
                   number.setText(user.getNumber());
                   address.setText(user.getAddress());
                   sex.setText(user.getSex());
                   date.setText(user.getDate());
               }
               break;
        }
    }
}
