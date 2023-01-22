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
import Entities.Assessment;

public class AssessmentListActivity extends AppCompatActivity {

    private List<Assessment> assessments;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repo = new Repository(getApplication());
        assessments = repo.getAllAssessments();
        populateRecyclerview(assessments);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(repo == null){
            repo = new Repository(getApplication());
        }
        assessments = repo.getAllAssessments();
        populateRecyclerview(assessments);
    }

    public void populateRecyclerview(List<Assessment> assessments){
        RecyclerView assessmentRecyclerView = findViewById(R.id.assessmentListRecyclerView);
        final AssessmentAdaptor adapter = new AssessmentAdaptor(this);
        assessmentRecyclerView.setAdapter(adapter);
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(assessments);
    }

    public void assessmentListSearchButton(View view){
        if(repo == null){
            repo = new Repository(getApplication());
        }
        assessments = repo.getAllAssessments();
        List<Assessment> filtered = new ArrayList<>();
        EditText assessmentListSearchEditText = findViewById(R.id.assessmentListSearchEditText);
        for(Assessment a : assessments){
            if(a.getAssessmentName().contains(assessmentListSearchEditText.getText())){
                filtered.add(a);
            }
        }
        populateRecyclerview(filtered);
    }

    public void assessmentListFABClick(View view){
        Intent intent = new Intent(this, AssessmentActivity.class);
        startActivity(intent);
    }

}
