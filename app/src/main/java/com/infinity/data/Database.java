package com.infinity.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.infinity.clock.Entity;
import com.infinity.model.DishItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ICOOK";
    public static final String TABLE_NAME = "tbl_dishlog";
    public static final int verson = 2;
    public static final String TAG = "TienDH";

    public static final String CL_ID = "ID";
    public static final String CL_DISHNAME = "NAME";
    public static final String CL_DATE = "DATE";
    public SQLiteDatabase mSQLitedb;

    public Context mContext;


    public Database(Context context) {
        super(context, DATABASE_NAME, null, verson);
        mSQLitedb = getWritableDatabase();
        mContext = context;
    }

    /*
     * - Kiem tra da co bang ton tai trong csdl chua. - neu co tra ve la true
     */
    public boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                        + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + CL_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CL_DISHNAME + " TEXT, " + CL_DATE + " DATETIME );";

        if (!isTableExists(db, TABLE_NAME)) {
            Log.d(null, "Oncreate " + createTable);
            db.execSQL(createTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqldrop = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sqldrop);
        onCreate(db);
    }

    /*
     * -
     */
    public void insertDish(DishItem dishItem) {
        long timeCurrent = System.currentTimeMillis();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeCurrent));
        Log.d("TienDH", "date: " + date);

        ContentValues values = new ContentValues();
        values.put(CL_DISHNAME, dishItem.getName());
        values.put(CL_DATE, date);
        if (mSQLitedb.insert(TABLE_NAME, null, values) == -1) {
            Log.d("TienDH", "INSERT Err " + dishItem.getName());
        } else {
            Log.d("TienDH", "INSERT: " + dishItem.getName());
        }

    }

    public void insertDish(String dishName) {
        long timeCurrent = System.currentTimeMillis();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeCurrent));
        Log.d("TienDH", "date: " + date);
        ContentValues values = new ContentValues();
        values.put(CL_DISHNAME, dishName);
        values.put(CL_DATE, date);
        if (mSQLitedb.insert(TABLE_NAME, null, values) == -1) {
            Log.d("TienDH", "INSERT Err " + dishName);
        } else {
            Log.d("TienDH", "INSERT: " + dishName);
        }

    }

    //lay mon an hom nay
    public ArrayList<Entity> getListToday() {
        ArrayList<Entity> list = new ArrayList<Entity>();
//        Cursor cursor = mSQLitedb.query(TABLE_NAME, null, CL_DATE + " = date('now')", null, null, null, null);
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + CL_ID + " DESC LIMIT 7";
        Cursor cursor = mSQLitedb.rawQuery(sql, null);
        Log.d("TienDH", "sql: " + sql);
        while (cursor.moveToNext()) {
            int CL_name = cursor.getColumnIndex(CL_DISHNAME);
            String name = cursor.getString(CL_name);
            Entity entity = new Entity(name, false);
            list.add(entity);
        }
        cursor.close();
        return list;
    }

    public void dropTable() {
        String sqldrop = "DROP TABLE IF EXISTS " + TABLE_NAME;
        mSQLitedb.execSQL(sqldrop);
        onCreate(mSQLitedb);
    }

    /*
     * - dong database
     */
    public void closeDB() {
        if (mSQLitedb != null && mSQLitedb.isOpen())
            mSQLitedb.close();
    }

}
