package com.example.skydivelogbook.db;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

@Database(entities = {Log.class}, version = 1, exportSchema = false)
public abstract class LogDatabase extends RoomDatabase {
    public interface LogListener{
        void onLogReturned(Log log);
    }

    public abstract LogDAO logDAO();

    private static LogDatabase INSTANCE;

    public static LogDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LogDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LogDatabase.class, "log_database")
                            .addCallback(createLogDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback createLogDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            createLogTable();
        }
    };

    private static void createLogTable() {
    /*    for (int i = 0; i < DefaultContent.TITLE.length; i++) {

        }*/
    }

    public static void getLog(int jumpnum, LogListener listener) {
        new AsyncTask<Integer, Void, Log> () {
            protected Log doInBackground(Integer... jumps) {
                return INSTANCE.logDAO().getLog(jumps[0]);
            }

            protected void onPostExecute(Log log) {
                super.onPostExecute(log);
                listener.onLogReturned(log);
            }
        }.execute(jumpnum);
    }

    public static void insert(Log log) {
        new AsyncTask<Log, Void, Void>() {
            protected Void doInBackground(Log... logs) {
                INSTANCE.logDAO().insert(logs);
                return null;
            }
        }.execute(log);
    }

    public static void update(Log log) {
        new AsyncTask<Log, Void, Void>() {
            protected Void doInBackground(Log... logs) {
                INSTANCE.logDAO().update(logs);
                return null;
            }
        }.execute(log);
    }

    public static void delete(int jumpNum) {
        new AsyncTask<Integer, Void, Void>() {
            protected Void doInBackground(Integer... jumpNums) {
                INSTANCE.logDAO().delete(jumpNums[0]);
                return null;
            }
        }.execute(jumpNum);
    }

}

