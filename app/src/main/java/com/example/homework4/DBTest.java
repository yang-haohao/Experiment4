package com.example.homework4;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class DBTest extends AppCompatActivity {
    SQLiteDatabase db;
    Button bn = null;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        db=SQLiteDatabase.openOrCreateDatabase(
                this.getFilesDir().toString()
                +"/my.db3",null);
        listView=(ListView)findViewById(R.id.show);
        bn=(Button)findViewById(R.id.ok);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=((EditText)findViewById(R.id.title)).getText().toString();
                String content=((EditText)findViewById(R.id.content)).getText().toString();
                try {
                    insertData(db,title,content);
                    Cursor cursor = db.rawQuery("select * from news_inf",null);
                    inflateList(cursor);
                }
                catch (SQLiteException se){
                    db.execSQL("create table news_inf(_id integer primary key autoincrement,"
                            +"news_title varchar(50),"
                            +"news_content varchar(255))");
                    insertData(db,title,content);
                    Cursor cursor = db.rawQuery("select * from news_inf",null);
                    inflateList(cursor);
                }
            }
        });
    }
    private void insertData(SQLiteDatabase db,String title,String content){
        db.execSQL("insert into news_inf values(null , ? , ?)"
            ,new String[]{title,content});
    }

    private void inflateList(Cursor cursor)
    {
        ArrayList<HashMap<String,String>>arrayList = new ArrayList<HashMap<String,String>>();
        arrayList=cursorToHashMap(cursor);
        SimpleAdapter adapter = new SimpleAdapter(
                DBTest.this,
                arrayList,
                R.layout.line,
                new String[]{"news title","news content"},
                new int[]{R.id.my_title,R.id.my_content}
        );
        listView.setAdapter(adapter);
    }

    public static ArrayList<HashMap<String, String>> cursorToHashMap(Cursor cursor) {
        if (cursor != null) {
            int cursorCount = cursor.getCount();
            int columnCount;
            ArrayList<HashMap<String, String>> cursorData = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> rowHashMap;
            for (int i = 0; i < cursorCount; i++) {
                cursor.moveToPosition(i);
                rowHashMap = new HashMap<String, String>();
                columnCount = cursor.getColumnCount();
                for (int j = 0; j < columnCount; j++) {
                    rowHashMap.put(cursor.getColumnName(j),
                            cursor.getString(j));
                }
                cursorData.add(rowHashMap);
            }
            cursor.close();
            return cursorData;
        } else {
            return null;
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if(db != null && db.isOpen()){
            db.close();
        }
    }
}
