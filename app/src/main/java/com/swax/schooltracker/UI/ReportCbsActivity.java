package com.swax.schooltracker.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Database.Repository;
import Entities.Course;

public class ReportCbsActivity extends AppCompatActivity {

    private List<Course> inProcessCount = new ArrayList<>();
    private List<Course> completedCount = new ArrayList<>();
    private List<Course> droppedCount = new ArrayList<>();
    private List<Course> planToTakeCount = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_cbs);
        Repository repo = new Repository(getApplication());
        List<Course> courses = repo.getAllCourses();
        for(Course c : courses){
            switch(c.getCourseStatus()){
                case "In Process":
                    inProcessCount.add(c);
                    break;
                case "Completed":
                    completedCount.add(c);
                    break;
                case "Dropped":
                    droppedCount.add(c);
                    break;
                case "Plan to Take":
                    planToTakeCount.add(c);
                    break;
            }
        }
        List<Course> sorted = new ArrayList<>();
        sorted.addAll(inProcessCount);
        sorted.addAll(completedCount);
        sorted.addAll(droppedCount);
        sorted.addAll(planToTakeCount);

        TextView reportCbsDateTextView = findViewById(R.id.reportCbsDateTextView);
        String myFormat = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat);
        reportCbsDateTextView.setText(LocalDate.now().format(formatter));

        RecyclerView reportCbsRecyclerView = findViewById(R.id.reportCbsRecyclerView);
        final ReportCbsAdaptor adapter = new ReportCbsAdaptor(this);
        reportCbsRecyclerView.setAdapter(adapter);
        reportCbsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(sorted);
    }
}
