package com.swax.schooltracker.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.swax.schooltracker.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import Database.Repository;
import Entities.Assessment;

public class ReportAbtActivity extends AppCompatActivity {

    private Integer objectiveCount = 0;
    private Integer performanceCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_abt);
        Repository repo = new Repository(getApplication());
        List<Assessment> assessments = repo.getAllAssessments();
        for(Assessment a : assessments){
            switch(a.getAssessmentType()){
                case "Objective":
                    objectiveCount++;
                    break;
                case "Performance":
                    performanceCount++;
                    break;
            }
        }
        TextView reportAbtDateTextView = findViewById(R.id.reportAbtDateTextView);
        String myFormat = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat);
        reportAbtDateTextView.setText(LocalDate.now().format(formatter));

        TextView objectiveCountTextView = findViewById(R.id.objectiveCountTextView);
        objectiveCountTextView.setText(objectiveCount.toString());

        TextView performanceCountTextView = findViewById(R.id.performanceCountTextView);
        performanceCountTextView.setText(performanceCount.toString());
    }
}

