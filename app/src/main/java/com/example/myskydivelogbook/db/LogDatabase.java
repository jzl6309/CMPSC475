package com.example.myskydivelogbook.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

@Database(entities = {Log.class}, version = 2, exportSchema = false)
public abstract class LogDatabase extends RoomDatabase {
    public interface LogListener{
        void onLogReturned(Log log, int jumpNum);
    }
    public interface MaxListener{
        void onMaxReturned(int max);
    }
    public interface LogListListener{
        void onListReturned(List<Log> logs);
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
        }
    };

    public static void getCurrentLogs(LogListListener listener) {
        new AsyncTask<Void, Void, List<Log>>  () {
            protected List<Log> doInBackground(Void... voids) {
                return INSTANCE.logDAO().getCurrentLogs();
            }

            protected void onPostExecute(List<Log> logs) {
                listener.onListReturned(logs);
            }
        }.execute();
    }

    public static void getLog(int jumpnum, LogListener listener) {
        new AsyncTask<Integer, Void, Log> () {
            protected Log doInBackground(Integer... jumps) {
                return INSTANCE.logDAO().getLog(jumps[0]);
            }

            protected void onPostExecute(Log log) {
                super.onPostExecute(log);
                listener.onLogReturned(log, jumpnum);
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

    public static void getMaxJump(MaxListener listener) {
        new AsyncTask<Void,Void,Integer>() {
            protected Integer doInBackground(Void... voids) {
                return INSTANCE.logDAO().getMaxJump();
            }

            protected void onPostExecute(Integer max) {
                super.onPostExecute(max);
                listener.onMaxReturned(max);
            }
        }.execute();
    }

    public static void getCount(MaxListener listener) {
        new AsyncTask<Void,Void,Integer>() {
            protected Integer doInBackground(Void... voids) {
                return INSTANCE.logDAO().getCount();
            }

            protected void onPostExecute(Integer max) {
                super.onPostExecute(max);
                listener.onMaxReturned(max);
            }
        }.execute();
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

