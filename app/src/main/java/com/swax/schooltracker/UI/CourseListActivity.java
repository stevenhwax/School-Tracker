package com.swax.schooltracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.util.List;

import Database.Repository;
import Entities.Course;

public class CourseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateRecyclerview();
    }

    public void populateRecyclerview(){
        RecyclerView courseRecyclerView = findViewById(R.id.courseListRecyclerView);
        Repository repo = new Repository(getApplication());
        List<Course> courses = repo.getAllCourses();
        final CourseAdaptor adapter = new CourseAdaptor(this);
        courseRecyclerView.setAdapter(adapter);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(courses);
    }

    public void courseListFABClick(View view){
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
    }
}
