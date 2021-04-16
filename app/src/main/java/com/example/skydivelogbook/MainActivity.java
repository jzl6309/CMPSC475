package com.example.skydivelogbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.skydivelogbook.db.LogDatabase;

public class MainActivity extends AppCompatActivity {

    private Menu optionsMenu;
    private boolean edit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));
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
                    findViewById(R.id.upsaNumberDisplay).setEnabled(true);
                    findViewById(R.id.uspaLicenseDisplay).setEnabled(true);
                }
                else{
                    edit = true;
                    item.setIcon(R.drawable.edit_icon);
                    findViewById(R.id.upsaNumberDisplay).setEnabled(false);
                    findViewById(R.id.uspaLicenseDisplay).setEnabled(false);
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