package com.example.myskydivelogbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myskydivelogbook.db.Log;
import com.example.myskydivelogbook.db.LogDatabase;
import com.google.android.material.snackbar.Snackbar;

public class activityAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    public void save(View view) {

        int jumpNum;
        try {
            jumpNum = Integer.parseInt(((TextView) findViewById(R.id.addJumpNumDisplay)).getText().toString().trim());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Unable to update! You must enter a valid jump number!", Toast.LENGTH_LONG).show();
            return;
        }
        LogDatabase.getLog(jumpNum, (log, jumpNum1) -> {

            if (log == null ) {
                String date = ((EditText) findViewById(R.id.addEditTextDate)).getText().toString();
                String location = ((EditText) findViewById(R.id.addLocationDisplay)).getText().toString();
                String aircraft = ((EditText) findViewById(R.id.addEditAircraft)).getText().toString();
                String equipment = ((EditText) findViewById(R.id.addEditGearLabel)).getText().toString();
                String altitude = ((EditText) findViewById(R.id.addEditExitAlt)).getText().toString();
                String delay = ((EditText) findViewById(R.id.addEditDelay)).getText().toString();
                String wind = ((EditText) findViewById(R.id.addEditSurfaceWind)).getText().toString();
                String target = ((EditText) findViewById(R.id.addEditDistTarget)).getText().toString();
                String sign = ((EditText) findViewById(R.id.addEditSignature)).getText().toString();

                String notes = ((EditText) findViewById(R.id.addEditNotes)).getText().toString();

                Log log1 = new Log(jumpNum, date, location, aircraft, equipment, altitude, delay, wind, target, sign, notes);

                LogDatabase.insert(log1);
            }
            else {
                Snackbar.make(view,"There was an issue. Please try again.",Snackbar.LENGTH_LONG ).show();
            }

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

            findViewById(R.id.addJumpNumDisplay).setFocusable(true);
            findViewById(R.id.addJumpNumDisplay).requestFocus();
        });
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }
}