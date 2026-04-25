package com.example.coursetracker.ui.courses;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursetracker.R;
import com.example.coursetracker.adapter.CourseAdapter;
import com.example.coursetracker.model.Course;
import com.example.coursetracker.viewmodel.CourseViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CoursesFragment extends Fragment {

    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;

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

        // 🔥 Default: show all courses
        courseViewModel.getCourses().observe(getViewLifecycleOwner(), courses -> {
            courseAdapter.setCourses(courses);
        });

        // 🔥 Add Course button (dialog)
        MaterialButton btnAdd = view.findViewById(R.id.btnAddCourse);

        btnAdd.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Add Course");

            EditText input = new EditText(requireContext());
            input.setHint("Enter course name");
            builder.setView(input);

            builder.setPositiveButton("Add", (dialog, which) -> {
                String courseName = input.getText().toString().trim();

                if (!courseName.isEmpty()) {
                    courseViewModel.insert(
                            new Course(
                                    "CS" + System.currentTimeMillis(),
                                    courseName,
                                    "Prof",
                                    0
                            )
                    );
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.show();
        });

        // 🔥 Search feature (FIXED)
        TextInputEditText searchInput = view.findViewById(R.id.etSearchCourse);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = s.toString().trim();

                // Always use search (handles empty automatically)
                courseViewModel.searchCourses(query)
                        .observe(getViewLifecycleOwner(), courses -> {
                            courseAdapter.setCourses(courses);
                        });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}