package com.swax.schooltracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.swax.schooltracker.R;

public class ReportListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
    }

    public void reportCourseByStatus(View view){
        Intent intent = new Intent(this, ReportCbsActivity.class );
        startActivity(intent);
    }

    public void reportAssessmentByType(View view){
        Intent intent = new Intent(this, ReportAbtActivity.class );
        startActivity(intent);
    }

}
