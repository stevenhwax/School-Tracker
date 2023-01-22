package com.swax.schooltracker.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.swax.schooltracker.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import Database.Repository;
import Entities.Course;

public class ReportCbsActivity extends AppCompatActivity {

    private Integer inProcessCount = 0;
    private Integer completedCount = 0;
    private Integer droppedCount = 0;
    private Integer planToTakeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_cbs);
        Repository repo = new Repository(getApplication());
        List<Course> courses = repo.getAllCourses();
        for(Course c : courses){
            switch(c.getCourseStatus()){
                case "In Process":
                    inProcessCount++;
                    break;
                case "Completed":
                    completedCount++;
                    break;
                case "Dropped":
                    droppedCount++;
                    break;
                case "Plan to Take":
                    planToTakeCount++;
                    break;
            }
        }
        TextView reportCbsDateTextView = findViewById(R.id.reportCbsDateTextView);
        String myFormat = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat);
        reportCbsDateTextView.setText(LocalDate.now().format(formatter));

        TextView inProcessCountTextView = findViewById(R.id.inProcessCountTextView);
        inProcessCountTextView.setText(inProcessCount.toString());

        TextView completedCountTextView = findViewById(R.id.completedCountTextView);
        completedCountTextView.setText(completedCount.toString());

        TextView droppedCountTextView = findViewById(R.id.droppedCountTextView);
        droppedCountTextView.setText(droppedCount.toString());

        TextView planToTakeCountTextView = findViewById(R.id.planToTakeCountTextView);
        planToTakeCountTextView.setText(planToTakeCount.toString());
    }
}
