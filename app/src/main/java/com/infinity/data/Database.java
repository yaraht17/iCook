package com.infinity.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.infinity.model.DishItem;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "iCookDB";
    public static final String TABLE_NAME = "tbl_dish";
    public static final int verson = 1;
    public static final String TAG = "TienDH";

    public static final String CL_ID = "ID";
    public static final String CL_DISHNAME = "NAME";

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
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CL_DISHNAME + " TEXT);";

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
     * - them mot doi tuong Friend vao trong csdl
     */
    public void insertDish(DishItem dish) {

        ContentValues values = new ContentValues();
        values.put(CL_DISHNAME, dish.getName());

        if (mSQLitedb.insert(TABLE_NAME, null, values) == -1) {
            // Log.d("INSERT", friend.getName() + "- " + friend.getId());
            Toast.makeText(mContext, "Add New Error", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Log.d("INSERT", dish.getName());
        }

    }

    /*
     * - lay toan bo ban ghi trong bang co trong csdl
     */
    public ArrayList<DishItem> getList(long startId, long endId) {

        ArrayList<DishItem> list = new ArrayList<DishItem>();
        Cursor cursor = mSQLitedb.query(TABLE_NAME, null, CL_ID + ">= ? AND " + CL_ID + " <= ? "
                , new String[]{String.valueOf(startId), String.valueOf(endId)}, null,
                null, null);

        while (cursor.moveToNext()) {
            int numberId = cursor.getColumnIndex(CL_ID);
            int numberSender = cursor.getColumnIndex(CL_DISHNAME);

            int id = cursor.getInt(numberId);
            String dishName = cursor.getString(numberSender);

            DishItem item = new DishItem(id, dishName);
            list.add(item);
        }
        cursor.close();
        return list;
    }

    //lay mon an hom nay
    public ArrayList<DishItem> getListToday() {
        ArrayList<DishItem> list = new ArrayList<DishItem>();

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
