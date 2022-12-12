package com.swax.schooltracker.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import Entities.Course;

public class CourseAdaptor extends RecyclerView.Adapter<CourseAdaptor.CourseViewHolder>{

    private List<Course> mCourseList;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public CourseAdaptor(Context context){
        mInflater=LayoutInflater.from(context);
        this.mContext = context;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseIdView;
        private final TextView courseNameView;
        private final TextView courseStartView;
        private final TextView courseEndView;

        private CourseViewHolder(View itemView){
            super(itemView);
            courseIdView = itemView.findViewById(R.id.courseId);
            courseNameView = itemView.findViewById(R.id.courseName);
            courseStartView = itemView.findViewById(R.id.courseStart);
            courseEndView = itemView.findViewById(R.id.courseEnd);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContext instanceof TermActivity){
                        return;
                    } else {
                        int position = getAdapterPosition();
                        final Course current = mCourseList.get(position);
                        Intent intent = new Intent(mContext, CourseActivity.class);
                        intent.putExtra("id", current.getCourseId());
                        mContext.startActivity(intent);
                    }

                }
            });
        }
    }

    @NonNull
    @Override
    public CourseAdaptor.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseView = mInflater.inflate(R.layout.listitem_course, parent, false);
        return new CourseViewHolder(courseView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdaptor.CourseViewHolder holder, int position) {
        if(mCourseList != null) {
            Course current = mCourseList.get(position);
            holder.courseIdView.setText(current.getCourseId().toString());
            holder.courseNameView.setText(current.getCourseName());
            holder.courseStartView.setText(current.getCourseStart().format(formatter));
            holder.courseEndView.setText(current.getCourseEnd().format(formatter));
        }
    }

    @Override
    public int getItemCount() {
        if(mCourseList != null){
            return mCourseList.size();
        } else {
            return 0;
        }
    }

    public void setCourses(List<Course> courses){
        mCourseList = courses;
        notifyDataSetChanged();
    }
}
