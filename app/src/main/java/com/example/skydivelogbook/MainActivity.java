package com.example.skydivelogbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.skydivelogbook.db.LogDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void add(View view) {
        Intent intent = new Intent(this, activityAdd.class);
        startActivity(intent);

    }

    public void view(View view) {
        Intent intent = new Intent(this, ActivityView.class);
        startActivity(intent);


    }
}