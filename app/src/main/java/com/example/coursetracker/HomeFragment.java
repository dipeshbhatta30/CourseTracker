package com.example.coursetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coursetracker.model.Course;
import com.example.coursetracker.viewmodel.CourseViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private TextView totalCoursesView;
    private TextView ongoingCoursesView;
    private TextView completedCoursesView;
    private TextView gpaView;
    private CourseViewModel courseViewModel;

    public HomeFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        totalCoursesView = view.findViewById(R.id.tvTotalCourses);
        ongoingCoursesView = view.findViewById(R.id.tvOngoingCourses);
        completedCoursesView = view.findViewById(R.id.tvCompletedCourses);
        gpaView = view.findViewById(R.id.tvGpa);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getCourses().observe(getViewLifecycleOwner(), courses -> {
            int total = courses == null ? 0 : courses.size();
            int ongoing = countOngoingCourses(courses);
            int completed = countCompletedCourses(courses);
            double gpa = computeGpa(courses);

            totalCoursesView.setText(String.valueOf(total));
            ongoingCoursesView.setText(String.valueOf(ongoing));
            completedCoursesView.setText(String.valueOf(completed));
            gpaView.setText(String.format(Locale.US, "%.2f", gpa));
        });
    }

    private double computeGpa(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return 0.0;
        }

        Map<String, Double> gradeMap = new HashMap<>();
        gradeMap.put("A", 4.0);
        gradeMap.put("A-", 3.7);
        gradeMap.put("B+", 3.3);
        gradeMap.put("B", 3.0);
        gradeMap.put("B-", 2.7);
        gradeMap.put("C+", 2.3);
        gradeMap.put("C", 2.0);
        gradeMap.put("C-", 1.7);
        gradeMap.put("D+", 1.3);
        gradeMap.put("D", 1.0);
        gradeMap.put("F", 0.0);

        double weightedPoints = 0.0;
        int totalCredits = 0;

        for (Course course : courses) {
            String gradeKey = course.getLetterGrade() == null
                    ? "N/A"
                    : course.getLetterGrade().trim().toUpperCase(Locale.US);
            Double points = gradeMap.get(gradeKey);
            int credits = Math.max(0, course.getCredits());

            if (points != null && credits > 0) {
                weightedPoints += points * credits;
                totalCredits += credits;
            }
        }

        if (totalCredits == 0) {
            return 0.0;
        }
        return weightedPoints / totalCredits;
    }

    private int countOngoingCourses(List<Course> courses) {
        if (courses == null) {
            return 0;
        }
        int count = 0;
        for (Course course : courses) {
            if (course.getProgress() < 100) {
                count++;
            }
        }
        return count;
    }

    private int countCompletedCourses(List<Course> courses) {
        if (courses == null) {
            return 0;
        }
        int count = 0;
        for (Course course : courses) {
            if (course.getProgress() >= 100) {
                count++;
            }
        }
        return count;
    }
}