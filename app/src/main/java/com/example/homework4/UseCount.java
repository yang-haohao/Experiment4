package com.example.homework4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class UseCount extends AppCompatActivity {
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        preferences=getSharedPreferences("count",MODE_PRIVATE);
        int count = preferences.getInt("count",0);
        Toast.makeText(this,"程序以前被使用了"+count+"次。",Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count",++count);
        editor.commit();
    }
}
