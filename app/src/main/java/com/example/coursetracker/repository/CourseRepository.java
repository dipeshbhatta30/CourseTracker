package com.example.coursetracker.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.coursetracker.database.AppDatabase;
import com.example.coursetracker.database.CourseDao;
import com.example.coursetracker.model.Course;

import java.util.List;

public class CourseRepository {
    private final CourseDao courseDao;
    private final LiveData<List<Course>> allCourses;

    public CourseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        courseDao = db.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public void insert(Course course) {
        new Thread(() -> courseDao.insert(course)).start();
    }

    public LiveData<List<Course>> searchCourses(String query) {
        return courseDao.searchCourses(query);
    }
}