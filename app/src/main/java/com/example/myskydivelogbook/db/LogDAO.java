package com.example.myskydivelogbook.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LogDAO {
    @Query("SELECT * FROM Logs ORDER BY jumpNum")
    LiveData<List<Log>> getAll();

    @Query("SELECT * FROM Logs WHERE jumpNum = :jumpNum")
    Log getLog(int jumpNum);

    @Query("SELECT max(jumpNum) FROM Logs")
    int getMaxJump();

    @Insert
    void insert(Log... logs);

    @Update
    void update(Log... log);

    @Query("DELETE FROM logs WHERE jumpNum = :jumpNum")
    void delete(int jumpNum);
    
}
