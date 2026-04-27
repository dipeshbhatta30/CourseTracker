package com.example.coursetracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.coursetracker.model.CalendarTask;
import com.example.coursetracker.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public LiveData<List<CalendarTask>> getTasksByDate(String date) {
        return repository.getTasksByDate(date);
    }

    public void insert(CalendarTask task) {
        repository.insert(task);
    }

    public void update(CalendarTask task) {
        repository.update(task);
    }
}
