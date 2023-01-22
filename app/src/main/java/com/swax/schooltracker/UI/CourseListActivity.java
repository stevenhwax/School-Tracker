package com.swax.schooltracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.util.ArrayList;
import java.util.List;

import Database.Repository;
import Entities.Course;
import Entities.Term;

public class CourseListActivity extends AppCompatActivity {

    private List<Course> courses;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(repo == null){
            repo = new Repository(getApplication());
        }
        courses = repo.getAllCourses();
        populateRecyclerview(courses);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(repo == null){
            repo = new Repository(getApplication());
        }
        courses = repo.getAllCourses();
        populateRecyclerview(courses);
    }

    public void populateRecyclerview(List<Course> courses){
        RecyclerView courseRecyclerView = findViewById(R.id.courseListRecyclerView);
        final CourseAdaptor adapter = new CourseAdaptor(this);
        courseRecyclerView.setAdapter(adapter);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(courses);
    }

    public void courseListSearchButton(View view){
        if(repo == null){
            repo = new Repository(getApplication());
        }
        courses = repo.getAllCourses();
        List<Course> filtered = new ArrayList<>();
        EditText courseListSearchEditText = findViewById(R.id.courseListSearchEditText);
        for(Course c : courses){
            if(c.getCourseName().contains(courseListSearchEditText.getText())){
                filtered.add(c);
            }
        }
        populateRecyclerview(filtered);
    }

    public void courseListFABClick(View view){
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
    }
}
