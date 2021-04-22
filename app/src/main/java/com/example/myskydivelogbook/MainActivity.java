package com.example.myskydivelogbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myskydivelogbook.db.Log;
import com.example.myskydivelogbook.db.LogDatabase;

public class MainActivity extends AppCompatActivity {

    private Menu optionsMenu;
    private boolean edit = true;
    private boolean newUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String name = sharedPref.getString("name","");
        String nameHint = sharedPref.getString("nameHint","");
        String memID = sharedPref.getString("MemID", "");
        String license = sharedPref.getString("license", "");
        newUser = sharedPref.getBoolean("newUser", true);

        ((TextView) findViewById(R.id.nameDisplay)).setText(name);
        ((EditText) findViewById(R.id.EditName)).setText(nameHint);

        if (memID != null)
            ((EditText) (findViewById(R.id.upsaNumberDisplay))).setText(memID);

        if (license != null)
            ((EditText) (findViewById(R.id.uspaLicenseDisplay))).setText(license);

        if (newUser) {
            Toast.makeText(this, "To enter your info, click the edit icon", Toast.LENGTH_LONG).show();
        }

        LogDatabase.getDatabase(getApplication());

        LogDatabase.getMaxJump(max -> {
            int maxJump = max;
            if (maxJump > 0) {
                ((TextView) findViewById(R.id.jumpNumberDisplay)).setText(Integer.toString(maxJump));
            }
            LogDatabase.getLog(maxJump,log -> {
                Log myLog = log;
                if (myLog != null) {
                    ((TextView) findViewById(R.id.lastJumpDiplay)).setText(myLog.date);
                }
            });
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuEdit:
                if (edit) {
                    edit = false;
                    item.setIcon(R.drawable.save_icon);
                    findViewById(R.id.EditName).setVisibility(View.VISIBLE);
                    findViewById(R.id.nameDisplay).setVisibility(View.INVISIBLE);
                    findViewById(R.id.upsaNumberDisplay).setEnabled(true);
                    findViewById(R.id.uspaLicenseDisplay).setEnabled(true);
                    findViewById(R.id.EditName).requestFocus();
                }
                else{
                    edit = true;
                    newUser = false;
                    item.setIcon(R.drawable.edit_icon);
                    findViewById(R.id.nameDisplay).setVisibility(View.VISIBLE);
                    findViewById(R.id.EditName).setVisibility(View.INVISIBLE);
                    findViewById(R.id.upsaNumberDisplay).setEnabled(false);
                    findViewById(R.id.uspaLicenseDisplay).setEnabled(false);
                    ((TextView) findViewById(R.id.nameDisplay)).setText(((EditText) findViewById(R.id.EditName)).getText().toString());
                    ((EditText) findViewById(R.id.EditName)).setText(((EditText) findViewById(R.id.EditName)).getText().toString());

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name", ((TextView) findViewById(R.id.nameDisplay)).getText().toString());
                    editor.putString("MemID",((EditText) findViewById(R.id.upsaNumberDisplay)).getText().toString());
                    editor.putString("license",((EditText) findViewById(R.id.uspaLicenseDisplay)).getText().toString());
                    editor.putString("nameHint",((EditText) findViewById(R.id.EditName)).getText().toString());
                    editor.putBoolean("newUser",newUser);
                    editor.apply();
                }
                return true;
            default: return true;
        }
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