package com.swax.schooltracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.swax.schooltracker.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Database.Repository;
import Entities.Assessment;
import Entities.Course;
import Entities.Term;

public class StartActivity extends AppCompatActivity {

    private String LOG_ID = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Repository repo = new Repository(getApplication());
        Integer i = 1;
        //Log.d(LOG_ID, "onCreate() " + repo.getAssessmentById(i).toString());
        if(repo.getAssessmentById(i) == null) {
            //Create test data
            LocalDate startDate = LocalDate.of(2022, 12, 10);
            LocalDate endDate = LocalDate.of(2022, 12, 25);

            List<Integer> associated = List.of(1, 2);
            Assessment ass1 = new Assessment("assessment1", startDate.atStartOfDay(), endDate.atStartOfDay(), "Test");
            Assessment ass2 = new Assessment("assessment2", startDate.atStartOfDay(), endDate.atStartOfDay(), "tetytest");
            Course course1 = new Course("course1", startDate, endDate, "status", "teacher", "5551212", "teach@teacher.com", null, associated);
            Course course2 = new Course("course2", startDate, endDate, "status", "teacher", "5551212", "teach@teacher.com", null, associated);
            Term term1 = new Term("term1", startDate, endDate, associated);
            Term term2 = new Term("term2", startDate, endDate, associated);

            repo.insert(ass1);
            repo.insert(ass2);
            repo.insert(course1);
            repo.insert(course2);
            repo.insert(term1);
            repo.insert(term2);
        }
    }

    public void termListButtonOnClick(View view){
        Intent intent = new Intent(StartActivity.this, TermListActivity.class);
        startActivity(intent);
    }

    public void courseListButtonOnClick(View view){
        Intent intent = new Intent(StartActivity.this, CourseListActivity.class);
        startActivity(intent);
    }

    public void assessmentListButtonOnClick(View view){
        Intent intent = new Intent(StartActivity.this, AssessmentListActivity.class);
        startActivity(intent);
    }

}
