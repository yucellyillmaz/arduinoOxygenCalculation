package com.yucellyillmaz.proje;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.Date;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "proje.db";
    private static final String TBL1 = "User_table";
    private static final String TB1_C1 = "Id";
    private static final String TB1_C2 = "Name";
    private static final String TB1_C3 = "Email";
    private static final String TB1_C4 = "Password";
    private static final String TBL2 = "Sensor_log";
    private static final String TB2_C1 = "Id";
    private static final String TB2_C2 = "oxygen";
    private static final String TB2_C3 = "remaining_time";
    private static final String TB2_C4 = "temperature";
    private static final String TB2_C5 = "humidity";
    private static final String TB2_C6 = "gas";
    private static final String TB2_C7 = "roomsize";

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + TBL1 + " ( Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT, Email TEXT, Password TEXT)");
        sqLiteDatabase.execSQL("create table " + TBL2 + " ( Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "oxygen TEXT, remaining_time TEXT, temperature TEXT, humidity TEXT, gas TEXT, roomsize TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL2);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData_User(String name, String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TB1_C2, name);
        values.put(TB1_C3, email);
        values.put(TB1_C4, password);
        long result = sqLiteDatabase.insert(TBL1, null, values);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertData_Sensor(String oxygen, String remaining_time, String temperature, String humidity,
                                     String gas, String roomsize){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TB2_C2, oxygen);
        values.put(TB2_C3, remaining_time);
        values.put(TB2_C4, temperature);
        values.put(TB2_C5, humidity);
        values.put(TB2_C6, gas);
        values.put(TB2_C7, roomsize);
        long result = sqLiteDatabase.insert(TBL2, null, values);
        if(result == -1)
            return false;
        else
            return true;
    }
    // Cursor = Sanal Tablo
    public Cursor getAllData_User(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("Select * From " + TBL1, null);
        return result;
    }

    public Cursor getAllData_Sensor()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("Select * From " + TBL2, null);
        return result;
    }
}
