package com.example.coursetracker.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String courseCode;
    private String courseName;
    private String instructor;
    private int progress;
    public Course(String courseCode, String courseName, String instructor, int progress) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructor = instructor;
        this.progress = progress;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getInstructor() { return instructor; }
    public int getProgress() { return progress; }
}