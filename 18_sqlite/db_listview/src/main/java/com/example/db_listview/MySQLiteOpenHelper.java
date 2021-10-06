package com.example.db_listview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    Context context;
    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE world (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "country TEXT, capital TEXT);");
        sqLiteDatabase.execSQL("INSERT INTO world VALUES (null, '한국', '서울');");
        sqLiteDatabase.execSQL("INSERT INTO world VALUES (null, '중국', '베이징');");
        sqLiteDatabase.execSQL("INSERT INTO world VALUES (null, '일본', '도쿄');");



        Log.i("jiseong","table 생성");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS world");
    }
}
