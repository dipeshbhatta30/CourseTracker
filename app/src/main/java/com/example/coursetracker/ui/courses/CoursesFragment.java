package com.example.coursetracker.ui.courses;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursetracker.R;
import com.example.coursetracker.adapter.CourseAdapter;
import com.example.coursetracker.viewmodel.CourseViewModel;

public class CoursesFragment extends Fragment {

    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;

    public CoursesFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rvCourses);
        courseAdapter = new CourseAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(courseAdapter);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        courseViewModel.getCourses().observe(getViewLifecycleOwner(), courses -> {
            courseAdapter.setCourses(courses);
        });
    }
}

