package com.example.db_listview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    MySQLiteOpenHelper dbHelper;
    SQLiteDatabase mdb;
    Cursor mCursor;
    SimpleCursorAdapter cursorAdapter;

    static final int RQCODE_UPDATE = 1;
    static final int RQCODE_INSERT = 2;
    TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String strCol[] = {"country", "capital"};

        dbHelper = new MySQLiteOpenHelper(this,"mydb.db",null, 1);
        mdb = dbHelper.getWritableDatabase();
        mCursor = mdb.rawQuery("SELECT * FROM world", null);

        listView = (ListView) findViewById(R.id.list_view);
        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_2, mCursor, strCol, new int[]{android.R.id.text1, android.R.id.text2}, 1);



        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(listener_list);

    }

    AdapterView.OnItemClickListener listener_list = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            mCursor.moveToPosition(i);

            Intent intent_update = new Intent();
            intent_update.setClass(MainActivity.this, input_activity.class);

            int id = mCursor.getInt(0);
            String country = mCursor.getString(1);
            String capital = mCursor.getString(2);


            intent_update.putExtra("country", country);
            intent_update.putExtra("capital", capital);
            intent_update.putExtra("id", id);

            startActivityForResult(intent_update, RQCODE_UPDATE);
            
        }
    };



    public void on_Click_btn(View view) {

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, input_activity.class);
        startActivityForResult(intent, RQCODE_INSERT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            ContentValues values = new ContentValues();

            int id = data.getIntExtra("_id",-1);
            String country = data.getStringExtra("country");
            String capital = data.getStringExtra("capital");
            values.put("country",country);
            values.put("capital",capital);

            switch (requestCode){
                case RQCODE_INSERT:
                    mdb.insert("world",null, values);
                    break;
                    
                case RQCODE_UPDATE:     //requestCode가 업데이트 일때 변경
                    mdb.update("world",values, "_id =" + id, null);
                    break;
            }
        }

        mCursor.requery();


    }
}



















