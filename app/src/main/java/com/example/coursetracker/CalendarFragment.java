package com.example.coursetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursetracker.adapter.TaskAdapter;
import com.example.coursetracker.model.CalendarTask;
import com.example.coursetracker.viewmodel.TaskViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarFragment extends Fragment {
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private TextView selectedDateLabel;
    private LiveData<List<CalendarTask>> activeTaskSource;
    private String selectedDate;

    public CalendarFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        RecyclerView tasksRecycler = view.findViewById(R.id.rvCalendarTasks);
        MaterialButton addTaskButton = view.findViewById(R.id.btnAddTask);
        selectedDateLabel = view.findViewById(R.id.tvSelectedDate);

        taskAdapter = new TaskAdapter();
        tasksRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        tasksRecycler.setAdapter(taskAdapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        selectedDate = toIsoDate(calendarView.getDate());
        updateSelectedDateLabel();
        observeTasksForDate(selectedDate);

        calendarView.setOnDateChangeListener((calendar, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            updateSelectedDateLabel();
            observeTasksForDate(selectedDate);
        });

        addTaskButton.setOnClickListener(v -> showAddTaskDialog());
        taskAdapter.setOnTaskClickListener(this::showEditTaskDialog);
    }

    private void observeTasksForDate(String date) {
        if (activeTaskSource != null) {
            activeTaskSource.removeObservers(getViewLifecycleOwner());
        }
        activeTaskSource = taskViewModel.getTasksByDate(date);
        activeTaskSource.observe(getViewLifecycleOwner(), tasks -> taskAdapter.setTasks(tasks));
    }

    private void showAddTaskDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Add Task");

        android.widget.LinearLayout container = new android.widget.LinearLayout(requireContext());
        container.setOrientation(android.widget.LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        container.setPadding(padding, padding, padding, 0);

        EditText taskTitle = new EditText(requireContext());
        taskTitle.setHint("Task title");
        container.addView(taskTitle);

        EditText taskStatus = new EditText(requireContext());
        taskStatus.setHint("Status (Pending/Done)");
        container.addView(taskStatus);

        builder.setView(container);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = taskTitle.getText().toString().trim();
            String status = taskStatus.getText().toString().trim();
            if (title.isEmpty()) {
                return;
            }
            if (status.isEmpty()) {
                status = "Pending";
            }
            taskViewModel.insert(new CalendarTask(title, status, selectedDate));
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showEditTaskDialog(CalendarTask task) {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Task");

        android.widget.LinearLayout container = new android.widget.LinearLayout(requireContext());
        container.setOrientation(android.widget.LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        container.setPadding(padding, padding, padding, 0);

        EditText taskTitle = new EditText(requireContext());
        taskTitle.setHint("Task title");
        taskTitle.setText(task.getTitle());
        container.addView(taskTitle);

        EditText taskStatus = new EditText(requireContext());
        taskStatus.setHint("Status (Pending/Done)");
        taskStatus.setText(task.getStatus());
        container.addView(taskStatus);

        builder.setView(container);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = taskTitle.getText().toString().trim();
            String status = taskStatus.getText().toString().trim();
            if (title.isEmpty()) {
                return;
            }
            if (status.isEmpty()) {
                status = "Pending";
            }
            CalendarTask updatedTask = new CalendarTask(title, status, task.getTaskDate());
            updatedTask.setId(task.getId());
            taskViewModel.update(updatedTask);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void updateSelectedDateLabel() {
        selectedDateLabel.setText("Selected Date: " + selectedDate);
    }

    private String toIsoDate(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(new Date(millis));
    }
}