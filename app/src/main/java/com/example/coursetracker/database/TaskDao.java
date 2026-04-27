package com.example.coursetracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coursetracker.model.CalendarTask;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(CalendarTask task);

    @Update
    void update(CalendarTask task);

    @Query("SELECT * FROM calendar_tasks WHERE taskDate = :date ORDER BY id DESC")
    LiveData<List<CalendarTask>> getTasksByDate(String date);
}
