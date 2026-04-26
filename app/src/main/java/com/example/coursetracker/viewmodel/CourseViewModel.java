package com.example.coursetracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.coursetracker.model.Course;
import com.example.coursetracker.repository.CourseRepository;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final CourseRepository repository;
    private final LiveData<List<Course>> courses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        courses = repository.getAllCourses();
    }

    public LiveData<List<Course>> getCourses() {
        return courses;
    }

    public void insert(Course course) {
        repository.insert(course);
    }

    public void update(Course course) {
        repository.update(course);
    }

    public void deleteById(int courseId) {
        repository.deleteById(courseId);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Course>> searchCourses(String query) {
        return repository.searchCourses(query);
    }
}