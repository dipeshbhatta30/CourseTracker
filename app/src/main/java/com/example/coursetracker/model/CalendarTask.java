package com.example.coursetracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calendar_tasks")
public class CalendarTask {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String status;
    private String taskDate;

    public CalendarTask(String title, String status, String taskDate) {
        this.title = title;
        this.status = status;
        this.taskDate = taskDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public String getTaskDate() {
        return taskDate;
    }
}
