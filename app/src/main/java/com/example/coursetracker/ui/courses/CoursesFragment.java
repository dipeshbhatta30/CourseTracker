package com.example.coursetracker.ui.courses;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoursesFragment extends Fragment {

    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;
    private List<Course> allCourses = new ArrayList<>();
    private String searchQuery = "";
    private FilterType selectedFilter = FilterType.ALL;

    private MaterialButton btnFilterAll;
    private MaterialButton btnFilterOngoing;
    private MaterialButton btnFilterCompleted;

    private enum FilterType {
        ALL,
        ONGOING,
        COMPLETED
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
            allCourses = courses == null ? new ArrayList<>() : courses;
            applyFilters();
        });

        courseAdapter.setOnCourseActionListener(new CourseAdapter.OnCourseActionListener() {
            @Override
            public void onEditCourse(Course course) {
                showEditCourseDialog(course);
            }

            @Override
            public void onDeleteCourse(Course course) {
                showDeleteCourseDialog(course);
            }
        });

        MaterialButton btnAdd = view.findViewById(R.id.btnAddCourse);
        MaterialButton btnDeleteAll = view.findViewById(R.id.btnDeleteAllCourses);
        btnFilterAll = view.findViewById(R.id.btnFilterAll);
        btnFilterOngoing = view.findViewById(R.id.btnFilterOngoing);
        btnFilterCompleted = view.findViewById(R.id.btnFilterCompleted);

        btnFilterAll.setOnClickListener(v -> {
            selectedFilter = FilterType.ALL;
            applyFilters();
            updateFilterButtons();
        });
        btnFilterOngoing.setOnClickListener(v -> {
            selectedFilter = FilterType.ONGOING;
            applyFilters();
            updateFilterButtons();
        });
        btnFilterCompleted.setOnClickListener(v -> {
            selectedFilter = FilterType.COMPLETED;
            applyFilters();
            updateFilterButtons();
        });
        updateFilterButtons();

        btnAdd.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Add Course");

            LinearLayout container = new LinearLayout(requireContext());
            container.setOrientation(LinearLayout.VERTICAL);
            int padding = (int) (16 * getResources().getDisplayMetrics().density);
            container.setPadding(padding, padding, padding, 0);

            EditText courseCodeInput = new EditText(requireContext());
            courseCodeInput.setHint("Course code");
            container.addView(courseCodeInput);

            EditText courseNameInput = new EditText(requireContext());
            courseNameInput.setHint("Course name");
            container.addView(courseNameInput);

            EditText instructorInput = new EditText(requireContext());
            instructorInput.setHint("Instructor");
            container.addView(instructorInput);

            EditText progressInput = new EditText(requireContext());
            progressInput.setHint("Progress (0-100)");
            progressInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            container.addView(progressInput);

            builder.setView(container);

            builder.setPositiveButton("Add", (dialog, which) -> {
                String courseCode = courseCodeInput.getText().toString().trim();
                String courseName = courseNameInput.getText().toString().trim();
                String instructor = instructorInput.getText().toString().trim();
                String progressText = progressInput.getText().toString().trim();

                if (courseCode.isEmpty() || courseName.isEmpty() || instructor.isEmpty()) {
                    return;
                }

                int progressValue = 0;
                if (!progressText.isEmpty()) {
                    try {
                        progressValue = Integer.parseInt(progressText);
                    } catch (NumberFormatException ignored) {
                        progressValue = 0;
                    }
                }
                progressValue = Math.max(0, Math.min(100, progressValue));

                courseViewModel.insert(
                        new Course(
                                courseCode,
                                courseName,
                                instructor,
                                progressValue
                        )
                );
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.show();
        });

        btnDeleteAll.setOnClickListener(v -> showDeleteAllDialog());

        TextInputEditText searchInput = view.findViewById(R.id.etSearchCourse);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().trim();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void applyFilters() {
        List<Course> filteredCourses = new ArrayList<>();
        String normalizedQuery = searchQuery.toLowerCase(Locale.US);

        for (Course course : allCourses) {
            boolean matchesFilter;
            if (selectedFilter == FilterType.ONGOING) {
                matchesFilter = course.getProgress() < 100;
            } else if (selectedFilter == FilterType.COMPLETED) {
                matchesFilter = course.getProgress() >= 100;
            } else {
                matchesFilter = true;
            }

            boolean matchesSearch = normalizedQuery.isEmpty()
                    || course.getCourseName().toLowerCase(Locale.US).contains(normalizedQuery)
                    || course.getCourseCode().toLowerCase(Locale.US).contains(normalizedQuery)
                    || course.getInstructor().toLowerCase(Locale.US).contains(normalizedQuery);

            if (matchesFilter && matchesSearch) {
                filteredCourses.add(course);
            }
        }

        courseAdapter.setCourses(filteredCourses);
    }

    private void updateFilterButtons() {
        btnFilterAll.setAlpha(selectedFilter == FilterType.ALL ? 1f : 0.6f);
        btnFilterOngoing.setAlpha(selectedFilter == FilterType.ONGOING ? 1f : 0.6f);
        btnFilterCompleted.setAlpha(selectedFilter == FilterType.COMPLETED ? 1f : 0.6f);
    }

    private void showEditCourseDialog(Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Course");

        LinearLayout container = new LinearLayout(requireContext());
        container.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        container.setPadding(padding, padding, padding, 0);

        EditText courseCodeInput = new EditText(requireContext());
        courseCodeInput.setHint("Course code");
        courseCodeInput.setText(course.getCourseCode());
        container.addView(courseCodeInput);

        EditText courseNameInput = new EditText(requireContext());
        courseNameInput.setHint("Course name");
        courseNameInput.setText(course.getCourseName());
        container.addView(courseNameInput);

        EditText instructorInput = new EditText(requireContext());
        instructorInput.setHint("Instructor");
        instructorInput.setText(course.getInstructor());
        container.addView(instructorInput);

        EditText progressInput = new EditText(requireContext());
        progressInput.setHint("Progress (0-100)");
        progressInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        progressInput.setText(String.valueOf(course.getProgress()));
        container.addView(progressInput);

        builder.setView(container);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String courseCode = courseCodeInput.getText().toString().trim();
            String courseName = courseNameInput.getText().toString().trim();
            String instructor = instructorInput.getText().toString().trim();
            String progressText = progressInput.getText().toString().trim();

            if (courseCode.isEmpty() || courseName.isEmpty() || instructor.isEmpty()) {
                return;
            }

            int progressValue = course.getProgress();
            if (!progressText.isEmpty()) {
                try {
                    progressValue = Integer.parseInt(progressText);
                } catch (NumberFormatException ignored) {
                    progressValue = course.getProgress();
                }
            }
            progressValue = Math.max(0, Math.min(100, progressValue));

            Course updatedCourse = new Course(
                    courseCode,
                    courseName,
                    instructor,
                    progressValue
            );
            updatedCourse.setId(course.getId());
            courseViewModel.update(updatedCourse);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showDeleteCourseDialog(Course course) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Course")
                .setMessage("Delete " + course.getCourseName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> courseViewModel.deleteById(course.getId()))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showDeleteAllDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete All Courses")
                .setMessage("This will remove all courses permanently.")
                .setPositiveButton("Delete All", (dialog, which) -> courseViewModel.deleteAll())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}