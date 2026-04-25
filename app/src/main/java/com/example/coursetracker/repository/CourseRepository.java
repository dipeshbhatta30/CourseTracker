package com.example.coursetracker.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.coursetracker.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository {

    private static CourseRepository instance;
    private final MutableLiveData<List<Course>> coursesLiveData = new MutableLiveData<>();

    private CourseRepository() {
        List<Course> courseList = new ArrayList<>();

        courseList.add(new Course("CS443", "Software Testing", "Professor", 85));
        courseList.add(new Course("CS483", "Big Data", "Professor", 75));
        courseList.add(new Course("CS401", "Android", "Professor", 60));

        coursesLiveData.setValue(courseList);
    }

    public static CourseRepository getInstance() {
        if (instance == null) {
            instance = new CourseRepository();
        }
        return instance;
    }

    public LiveData<List<Course>> getCourses() {
        return coursesLiveData;
    }
}