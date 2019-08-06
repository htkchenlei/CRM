package com.everstone.crm.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.everstone.crm.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAO {
    private SQLiteDatabase sqLiteDatabase;

    public UserDAO(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    public boolean addUser(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("number", user.getNumber());
        contentValues.put("address", user.getAddress());
        contentValues.put("sex", user.getSex());
        contentValues.put("date", user.getDate());
        long result = sqLiteDatabase.insert("crm", null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }

    /**
     * 查询全部用户，显示在主界面上
     * @return
     */
    public List<User> queryAll(){
        List<User> userList = new ArrayList<>();
        Cursor cursor =sqLiteDatabase.query("crm", null, null, null, null, null, "date desc");
        while (cursor.moveToNext()){
            User user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setNumber(cursor.getString(2));
            user.setAddress(cursor.getString(3));
            user.setSex(cursor.getString(4));
            user.setDate(cursor.getString(5));
            userList.add(user);
        }
        return userList;
    }

    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    public User queryUser(String id){
        User user = new User();
        String table = "CRM";
        String selection = "id=?" ;
        String[] selectionArgs = new  String[]{id};
        Cursor cursor = sqLiteDatabase.query(table, null, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()) {
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setNumber(cursor.getString(2));
            user.setAddress(cursor.getString(3));
            user.setSex(cursor.getString(4));
            user.setDate(cursor.getString(5));
        }
        return user;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public boolean updateUser(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("number", user.getNumber());
        contentValues.put("address", user.getAddress());
        contentValues.put("sex", user.getSex());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String dateString = sdf.format(new Date());
        contentValues.put("date", dateString);
        int result = sqLiteDatabase.update("crm", contentValues, "id=?", new String[]{user.getId() + ""});
        if(result == 0){
            return false;
        }
        return true;
    }

    /**
     * 根据用户输入，以username和number为条件进行模糊查询，返回userList
     * 如果没有查询到结果，则返回-1
     * @param query
     * @return
     */
    public List<User> fuzzyQuery(String query){
        List<User> userList = new ArrayList<>();
        String sql1 = "select * from CRM where username like '%" + query + "%'";
        String sql2 = "select * from CRM where number like '%" + query + "%'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql1, null);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setNumber(cursor.getString(2));
                user.setAddress(cursor.getString(3));
                user.setSex(cursor.getString(4));
                user.setDate(cursor.getString(5));
                userList.add(user);
            }
        }else{
            cursor = sqLiteDatabase.rawQuery(sql2, null);
            if(cursor.getCount() == 0){
                return null;
            }else{
                while (cursor.moveToNext()) {
                    User user = new User();
                    user.setId(cursor.getInt(0));
                    user.setUsername(cursor.getString(1));
                    user.setNumber(cursor.getString(2));
                    user.setAddress(cursor.getString(3));
                    user.setSex(cursor.getString(4));
                    user.setDate(cursor.getString(5));
                    userList.add(user);
                }
            }
        }
        return userList;
    }
}
