package com.example.coursetracker.model;
public class Course {
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

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getProgress() {
        return progress;
    }
}
