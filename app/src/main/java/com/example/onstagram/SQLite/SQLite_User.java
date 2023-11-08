package com.example.onstagram.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLite_User extends SQLiteOpenHelper {

    public static final String tableName = "User";

    public static final int VERSION = 1;


    public SQLite_User(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터 베이스가 생성될 때 호출 된다. 테이블 생성 등 초기화 작업을 수행할 수 있다.

        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터 베이스가 업데이트 될 때 호출되며, 이전 버전과 새 버전 간의 차이를 확인하여 업데이트 작업을 수행할 수 있다.

    }

    public void createTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName +
                "(id integer primary key, idx text)";

        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUser(SQLiteDatabase db, String idx)  {
        db.beginTransaction();

        try {
            String sql = "INSERT INTO " + tableName + "(idx)" + "VALUES('"+ idx + "')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public String getIdx(SQLiteDatabase db) {
        db.beginTransaction();

        String idx = "";

        String query = "SELECT idx FROM " + tableName;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {

                int columnIndex = cursor.getColumnIndexOrThrow("idx");

                idx = cursor.getString(columnIndex);

            } while (cursor.moveToNext());



        }

        db.endTransaction();


        return idx;
    }

}
