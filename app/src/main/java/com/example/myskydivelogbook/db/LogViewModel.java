package com.example.myskydivelogbook.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LogViewModel extends AndroidViewModel {
    private LiveData<List<Log>> logs;

    public LogViewModel (Application application) {
        super(application);
        logs = LogDatabase.getDatabase(getApplication()).logDAO().getAll();
    }

    public LiveData<List<Log>> getAllLogs() { return logs; }
}
