package com.example.a18_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MySQLiteOpenHelper dbHelper;
    SQLiteDatabase mdb;
    Cursor mCursor;
    TextView tv;
    Button btn_add, btn_read, btn_update, btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //있으면 mydb불러오기 없으면 mydb 생성
        dbHelper = new MySQLiteOpenHelper(this,"mydb.db",null,1);
        mdb = dbHelper.getWritableDatabase();

        tv = (TextView) findViewById(R.id.tv);
        btn_add = (Button) findViewById(R.id.btn1);
        btn_read = (Button) findViewById(R.id.btn2);
        btn_update = (Button) findViewById(R.id.btn3);
        btn_delete = (Button) findViewById(R.id.btn4);





    }

    public void on_Click_btn_insert(View view) {

        mdb.execSQL("INSERT INTO world VALUES (null, '북한', '평양');");
        /*ContentValues values = new ContentValues();
        values.put("country", "korea");
        values.put("country", "seoul");

        long result = mdb.insert("world", null, values);
        Log.i("jiseong","insert record id:" + result);*/
    }

    public void on_Click_btn_read(View view) {
        mCursor = mdb.query("world", null,null,null,null,null,null);

        String str = "";
        while(mCursor.moveToNext()){
            String country = mCursor.getString(1);
            String capital = mCursor.getString(2);
            str += (country + "- " + capital + "\n");

            if(str.length() > 0){
                tv.setText(str);
            }else{
                Log.i("jiseong","읽어올 데이터가 없습니다.");
                tv.setText("");
            }

        }
    }

    public void on_Click_btn_update(View view) {

        mCursor.moveToFirst();
        int id = mCursor.getInt(0);

        ContentValues values = new ContentValues();
        values.put("capital","서울");
        mdb.update("world", values, "_id = "+id,null);

        /*mCursor.moveToFirst();
        int id = mCursor.getInt(0);
        mdb.execSQL("UPDATE world SET capital='부산' WHERE _id=" + id + ";");*/
    }

    public void on_Click_btn_delete(View view) {

        mCursor.moveToFirst();
        int id = mCursor.getInt(0);

        mdb.delete("world", "_id="+id, null);

        /*mCursor.moveToFirst();
        int id = mCursor.getInt(0);
        mdb.execSQL("delete from world where _id= " +id + ";");*/

    }
}