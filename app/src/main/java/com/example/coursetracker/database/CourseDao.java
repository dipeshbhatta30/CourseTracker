package com.example.coursetracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coursetracker.model.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    void insert(Course course);

    @Update
    void update(Course course);

    @Query("DELETE FROM courses WHERE id = :courseId")
    void deleteById(int courseId);

    @Query("SELECT * FROM courses")
    LiveData<List<Course>> getAllCourses();

    @Query("DELETE FROM courses")
    void deleteAll();

    @Query("SELECT * FROM courses WHERE courseName LIKE '%' || :query || '%'")
    LiveData<List<Course>> searchCourses(String query);
}