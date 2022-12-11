package com.swax.schooltracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.swax.schooltracker.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Database.Repository;
import Entities.Course;
import Entities.Term;

public class TermActivity extends AppCompatActivity {

    private Term mTerm;
    private String mTermName = null;
    private LocalDate mTermStart = null;
    private LocalDate mTermEnd = null;
    private List<Integer> mTermAssociatedCourseIds = new ArrayList<>();
    private List<String> mTermAssociatedCourseStrings = new ArrayList<>();

    Repository repo = new Repository(getApplication());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        if(getIntent().getIntExtra("id", 0) == 0){
            mTerm = new Term(mTermName, mTermStart, mTermEnd, mTermAssociatedCourseIds);
        } else {
            mTerm = repo.getTermById(getIntent().getIntExtra("id", 0));
        }

        //populate the list of courses for the add/remove dropdown
        for(Integer i : mTerm.getAssociatedCourses()){
            mTermAssociatedCourseStrings.add(repo.getCourseById(i).toString());
        }

        Spinner addCourseSpinner = findViewById(R.id.termDetailAddCourseSpinner);
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mTermAssociatedCourseStrings);
        addCourseSpinner.setAdapter(courseAdapter);
        addCourseSpinner.setPrompt("Add Course");

    }
}