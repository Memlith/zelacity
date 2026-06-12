package com.example.zelacity.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.zelacity.model.Report;

import java.util.List;

@Dao
public interface ReportDao {
    @Insert
    void insert(Report report);

    @Update
    void update(Report report);

    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    LiveData<List<Report>> getAllReports();

    @Query("SELECT * FROM reports WHERE isSynced = 0")
    List<Report> getUnsyncedReports();
}
