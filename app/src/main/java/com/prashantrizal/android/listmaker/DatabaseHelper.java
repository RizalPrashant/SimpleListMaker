package com.prashantrizal.android.listmaker;

/**
 * Created by RizalPrashant on 10/11/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Shopping.db";
    public static final String TABLE_NAME = "Shopping_table";
  //  public static final String COL_1 = "ID";
    public static final String COL_2 = "Food";
    //public static final String COL_3 = "Food";
    //public static final String COL_4 = "Restaurant";
//      cut this below code and paste it into insertData method.
//    SQLiteDatabase db = this.getWritableDatabase();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (FOOD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String Food){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Food);
        //contentValues.put(COL_3, Food);
        //contentValues.put(COL_4, Restaurant);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("Select * from " + TABLE_NAME, null);
        return result;
    }
    public Integer deleteData(String Food){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, " Food = ?" , new String[] {Food});
    }
}
