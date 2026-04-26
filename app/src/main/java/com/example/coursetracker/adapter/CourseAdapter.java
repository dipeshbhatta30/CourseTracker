package com.example.coursetracker.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursetracker.R;
import com.example.coursetracker.model.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses = new ArrayList<>();
    private OnCourseActionListener onCourseActionListener;

    public interface OnCourseActionListener {
        void onEditCourse(Course course);
        void onDeleteCourse(Course course);
    }

    public void setOnCourseActionListener(OnCourseActionListener listener) {
        this.onCourseActionListener = listener;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.courseCode.setText(course.getCourseCode());
        holder.courseName.setText(course.getCourseName());
        holder.instructor.setText(course.getInstructor());
        holder.progressBar.setProgress(course.getProgress());
        holder.progressText.setText(course.getProgress() + "% Complete");
        holder.editButton.setOnClickListener(v -> {
            if (onCourseActionListener != null) {
                onCourseActionListener.onEditCourse(course);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (onCourseActionListener != null) {
                onCourseActionListener.onDeleteCourse(course);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onCourseActionListener == null) {
                return false;
            }

            String[] options = {"Edit", "Delete"};
            new android.app.AlertDialog.Builder(v.getContext())
                    .setTitle(course.getCourseName())
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            onCourseActionListener.onEditCourse(course);
                        } else if (which == 1) {
                            onCourseActionListener.onDeleteCourse(course);
                        }
                    })
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView courseCode, courseName, instructor, progressText;
        ProgressBar progressBar;
        com.google.android.material.button.MaterialButton editButton, deleteButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseCode = itemView.findViewById(R.id.tvCourseCode);
            courseName = itemView.findViewById(R.id.tvCourseName);
            instructor = itemView.findViewById(R.id.tvInstructor);
            progressBar = itemView.findViewById(R.id.progressCourse);
            progressText = itemView.findViewById(R.id.tvProgress);
            editButton = itemView.findViewById(R.id.btnEditCourse);
            deleteButton = itemView.findViewById(R.id.btnDeleteCourse);
        }
    }
}

