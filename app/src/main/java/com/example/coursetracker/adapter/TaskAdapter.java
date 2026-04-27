package com.example.coursetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursetracker.R;
import com.example.coursetracker.model.CalendarTask;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<CalendarTask> tasks = new ArrayList<>();
    private OnTaskClickListener onTaskClickListener;

    public interface OnTaskClickListener {
        void onEditTask(CalendarTask task);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.onTaskClickListener = listener;
    }

    public void setTasks(List<CalendarTask> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assignment, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        CalendarTask task = tasks.get(position);
        holder.assignmentName.setText(task.getTitle());
        holder.dueDate.setText("Date: " + task.getTaskDate());
        holder.status.setText(task.getStatus());

        holder.itemView.setOnClickListener(v -> {
            if (onTaskClickListener != null) {
                onTaskClickListener.onEditTask(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView assignmentName;
        TextView dueDate;
        TextView status;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            assignmentName = itemView.findViewById(R.id.tvAssignmentName);
            dueDate = itemView.findViewById(R.id.tvDueDate);
            status = itemView.findViewById(R.id.tvStatus);
        }
    }
}
