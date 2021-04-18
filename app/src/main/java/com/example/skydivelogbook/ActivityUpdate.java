package com.example.skydivelogbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skydivelogbook.db.Log;
import com.example.skydivelogbook.db.LogDatabase;

public class ActivityUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Bundle bundle = getIntent().getExtras();
        String jumpNum = bundle.getString("jumpNum");

        if (savedInstanceState != null) {
            ((EditText) findViewById(R.id.updateJumpNumDisplay)).setText(savedInstanceState.getString("jumpNum"));
            ((EditText) findViewById(R.id.updateEditTextDate)).setText(savedInstanceState.getString("date"));
            ((EditText) findViewById(R.id.updateLocationDisplay)).setText(savedInstanceState.getString("location"));
            ((EditText) findViewById(R.id.updateEditAircraft)).setText(savedInstanceState.getString("aircraft"));
            ((EditText) findViewById(R.id.updateEditGearLabel)).setText(savedInstanceState.getString("gear"));
            ((EditText) findViewById(R.id.updateEditExitAlt)).setText(savedInstanceState.getString("altitude"));
            ((EditText) findViewById(R.id.updateEditDelay)).setText(savedInstanceState.getString("delay"));
            ((EditText) findViewById(R.id.updateEditSurfaceWind)).setText(savedInstanceState.getString("wind"));
            ((EditText) findViewById(R.id.updateEditDistTarget)).setText(savedInstanceState.getString("target"));
            ((EditText) findViewById(R.id.updateEditSignature)).setText(savedInstanceState.getString("signature"));
            ((EditText) findViewById(R.id.updateEditNotes)).setText(savedInstanceState.getString("notes"));
        }
        else {
            LogDatabase.getLog(Integer.parseInt(jumpNum), log1 -> {
                Log log = log1;

                ((EditText) findViewById(R.id.updateJumpNumDisplay)).setText(Integer.toString(log.jumpNum));
                ((EditText) findViewById(R.id.updateEditTextDate)).setText(log.date);
                ((EditText) findViewById(R.id.updateLocationDisplay)).setText(log.location);
                ((EditText) findViewById(R.id.updateEditAircraft)).setText(log.aircraft);
                ((EditText) findViewById(R.id.updateEditGearLabel)).setText(log.equipment);
                ((EditText) findViewById(R.id.updateEditExitAlt)).setText(log.altitude);
                ((EditText) findViewById(R.id.updateEditDelay)).setText(log.delay);
                ((EditText) findViewById(R.id.updateEditSurfaceWind)).setText(log.wind);
                ((EditText) findViewById(R.id.updateEditDistTarget)).setText(log.target);
                ((EditText) findViewById(R.id.updateEditSignature)).setText(log.signature);
                ((EditText) findViewById(R.id.updateEditNotes)).setText(log.notes);
            });
        }
    }

    public void update(View view) {

        int jumpNum = Integer.parseInt(((TextView)findViewById(R.id.updateJumpNumDisplay)).getText().toString());
        String date = ((EditText)findViewById(R.id.updateEditTextDate)).getText().toString();
        String location = ((EditText)findViewById(R.id.updateLocationDisplay)).getText().toString();
        String aircraft = ((EditText)findViewById(R.id.updateEditAircraft)).getText().toString();
        String equipment = ((EditText)findViewById(R.id.updateEditGearLabel)).getText().toString();
        String altitude = ((EditText)findViewById(R.id.updateEditExitAlt)).getText().toString();
        String delay = ((EditText)findViewById(R.id.updateEditDelay)).getText().toString();
        String wind = ((EditText)findViewById(R.id.updateEditSurfaceWind)).getText().toString();
        String target = ((EditText)findViewById(R.id.updateEditDistTarget)).getText().toString();
        String sign = ((EditText)findViewById(R.id.updateEditSignature)).getText().toString();
        String notes = ((EditText)findViewById(R.id.updateEditNotes)).getText().toString();

        Log log = new Log(jumpNum,date,location,aircraft,equipment,altitude,delay,wind,target,sign,notes);

        LogDatabase.update(log);

        startActivity(new Intent(this, ActivityView.class));
    }

    public void cancel(View view) {
        startActivity(new Intent(this, ActivityView.class));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outstate) {
        super.onSaveInstanceState(outstate);

        outstate.putString("jumpNum", ((EditText)findViewById(R.id.updateJumpNumDisplay)).getText().toString());
        outstate.putString("date", ((EditText)findViewById(R.id.updateEditTextDate)).getText().toString());
        outstate.putString("location", ((EditText)findViewById(R.id.updateLocationDisplay)).getText().toString());
        outstate.putString("aircraft", ((EditText)findViewById(R.id.updateEditAircraft)).getText().toString());
        outstate.putString("gear", ((EditText)findViewById(R.id.updateEditGearLabel)).getText().toString());
        outstate.putString("altitude", ((EditText)findViewById(R.id.updateEditExitAlt)).getText().toString());
        outstate.putString("delay", ((EditText)findViewById(R.id.updateEditDelay)).getText().toString());
        outstate.putString("wind", ((EditText)findViewById(R.id.updateEditSurfaceWind)).getText().toString());
        outstate.putString("target", ((EditText)findViewById(R.id.updateEditDistTarget)).getText().toString());
        outstate.putString("signature", ((EditText)findViewById(R.id.updateEditSignature)).getText().toString());
        outstate.putString("notes", ((EditText)findViewById(R.id.updateEditNotes)).getText().toString());
    }
}


