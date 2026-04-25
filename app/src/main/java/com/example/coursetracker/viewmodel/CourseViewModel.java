package com.example.coursetracker.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursetracker.model.Course;
import com.example.coursetracker.repository.CourseRepository;

import java.util.List;

public class CourseViewModel extends ViewModel {

    private final CourseRepository repository;
    private final LiveData<List<Course>> courses;

    public CourseViewModel() {
        repository = CourseRepository.getInstance();
        courses = repository.getCourses();
    }

    public LiveData<List<Course>> getCourses() {
        return courses;
    }
}