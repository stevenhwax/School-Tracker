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
import Entities.Assessment;

public class AssessmentListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateRecyclerview();
    }

    public void populateRecyclerview(){
        RecyclerView assessmentRecyclerView = findViewById(R.id.assessmentListRecyclerView);
        Repository repo = new Repository(getApplication());
        List<Assessment> assessments = repo.getAllAssessments();
        final AssessmentAdaptor adapter = new AssessmentAdaptor(this);
        assessmentRecyclerView.setAdapter(adapter);
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(assessments);
    }

    public void assessmentListFABClick(View view){
        Intent intent = new Intent(this, AssessmentActivity.class);
        startActivity(intent);
    }

}
