package com.example.skydivelogbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.skydivelogbook.db.Log;
import com.example.skydivelogbook.db.LogDatabase;
import com.example.skydivelogbook.db.LogViewModel;

import static com.example.skydivelogbook.db.LogDatabase.getDatabase;
import static com.example.skydivelogbook.db.LogDatabase.insert;

public class activityAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    public void save(View view) {

        int jumpNum = Integer.parseInt(((TextView)findViewById(R.id.addJumpNumDisplay)).getText().toString());
        String date = ((EditText)findViewById(R.id.addEditTextDate)).getText().toString();
        String location = ((EditText)findViewById(R.id.addLocationDisplay)).getText().toString();
        String aircraft = ((EditText)findViewById(R.id.addEditAircraft)).getText().toString();
        String equipment = ((EditText)findViewById(R.id.addEditGearLabel)).getText().toString();
        String altitude = ((EditText)findViewById(R.id.addEditExitAlt)).getText().toString();
        String delay = ((EditText)findViewById(R.id.addEditDelay)).getText().toString();
        String wind = ((EditText)findViewById(R.id.addEditSurfaceWind)).getText().toString();
        String target = ((EditText)findViewById(R.id.addEditDistTarget)).getText().toString();
        String sign = ((EditText)findViewById(R.id.addEditSignature)).getText().toString();
        String notes = ((EditText)findViewById(R.id.addEditNotes)).getText().toString();

        getDatabase(getApplication()).logDAO().getAll();

        Log log = new Log(jumpNum,date,location,aircraft,equipment,altitude,delay,wind,target,sign,notes);

        LogDatabase.insert(log);

        ((TextView)findViewById(R.id.addJumpNumDisplay)).setText("");
        ((EditText)findViewById(R.id.addEditTextDate)).setText("");
        ((EditText)findViewById(R.id.addLocationDisplay)).setText("");
        ((EditText)findViewById(R.id.addEditAircraft)).setText("");
        ((EditText)findViewById(R.id.addEditGearLabel)).setText("");
        ((EditText)findViewById(R.id.addEditExitAlt)).setText("");
        ((EditText)findViewById(R.id.addEditDelay)).setText("");
        ((EditText)findViewById(R.id.addEditSurfaceWind)).setText("");
        ((EditText)findViewById(R.id.addEditDistTarget)).setText("");
        ((EditText)findViewById(R.id.addEditSignature)).setText("");
        ((EditText)findViewById(R.id.addEditNotes)).setText("");

    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}