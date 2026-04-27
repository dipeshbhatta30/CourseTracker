package com.example.coursetracker.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.coursetracker.database.AppDatabase;
import com.example.coursetracker.database.TaskDao;
import com.example.coursetracker.model.CalendarTask;

import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();
    }

    public LiveData<List<CalendarTask>> getTasksByDate(String date) {
        return taskDao.getTasksByDate(date);
    }

    public void insert(CalendarTask task) {
        new Thread(() -> taskDao.insert(task)).start();
    }

    public void update(CalendarTask task) {
        new Thread(() -> taskDao.update(task)).start();
    }
}
