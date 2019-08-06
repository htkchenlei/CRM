package com.everstone.crm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyOpenHelper extends SQLiteOpenHelper {

    private static final String name = "crm.db";
    private static final int version = 1;
    private static final String sql = "CREATE TABLE IF NOT EXISTS " +
            "CRM(id INTEGER primary key autoincrement," +
            "username varchar(64), number varchar(64), address varchar(256), " +
            "sex varchar(64), date varchar(256))";

    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            Log.d("everstone", "please check database version");
        }
    }
}
