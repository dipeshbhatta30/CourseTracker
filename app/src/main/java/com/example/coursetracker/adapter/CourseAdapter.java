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
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView courseCode, courseName, instructor, progressText;
        ProgressBar progressBar;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.tvCourseName);
            instructor = itemView.findViewById(R.id.tvInstructor);
            progressBar = itemView.findViewById(R.id.progressCourse);
            progressText = itemView.findViewById(R.id.tvProgress);
        }
    }
}

