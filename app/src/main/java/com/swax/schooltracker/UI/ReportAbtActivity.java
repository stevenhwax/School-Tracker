package com.swax.schooltracker.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Database.Repository;
import Entities.Assessment;

public class ReportAbtActivity extends AppCompatActivity {

    private List<Assessment> objectiveCount = new ArrayList<>();
    private List<Assessment> performanceCount = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_abt);
        Repository repo = new Repository(getApplication());
        List<Assessment> assessments = repo.getAllAssessments();
        for(Assessment a : assessments){
            switch(a.getAssessmentType()){
                case "Objective":
                    objectiveCount.add(a);
                    break;
                case "Performance":
                    performanceCount.add(a);
                    break;
            }
        }
        List<Assessment> sorted = new ArrayList<>();
        sorted.addAll(objectiveCount);
        sorted.addAll(performanceCount);

        TextView reportAbtDateTextView = findViewById(R.id.reportAbtDateTextView);
        String myFormat = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat);
        reportAbtDateTextView.setText(LocalDate.now().format(formatter));

        RecyclerView reportAbtRecyclerView = findViewById(R.id.reportAbtRecyclerView);
        final ReportAbtAdaptor adapter = new ReportAbtAdaptor(this);
        reportAbtRecyclerView.setAdapter(adapter);
        reportAbtRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(sorted);
    }
}

