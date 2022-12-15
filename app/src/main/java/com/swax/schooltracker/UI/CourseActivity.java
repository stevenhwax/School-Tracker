package com.swax.schooltracker.UI;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Database.Repository;
import Entities.Assessment;
import Entities.Course;
import Entities.Term;

public class CourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String LOG_ID = "CourseActivity";
    private Course mCourse;
    private String mCourseName = "Enter Name";
    private LocalDate mCourseStart = LocalDate.now();
    private LocalDate mCourseEnd = LocalDate.now();
    private String mCourseStatus;
    private String mCourseInstructor = "Enter Instructor";
    private String mCoursePhone = "425-555-1212";
    private String mCourseEmail = "teach@teacher.com";
    private String mCourseNotes = "";
    private List<Integer> associatedTermIds = new ArrayList<>();
    private List<Term> associatedTerms = new ArrayList<>();
    private List<String> courseAddTermStrings;
    private List<String> courseDeleteTermStrings;
    private List<Integer> associatedAssessmentIds = new ArrayList<>();
    private List<Assessment> associatedAssessments = new ArrayList<>();
    private List<String> courseAddAssessmentStrings;
    private List<String> courseDeleteAssessmentStrings;
    private Repository repo = new Repository(getApplication());
    private DatePickerDialog.OnDateSetListener startDate;
    private DatePickerDialog.OnDateSetListener endDate;
    private final Calendar myCalendar = Calendar.getInstance();
    private String myFormat = "MM/dd/yyyy";
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat);
    private EditText courseNameEditText;
    private EditText courseInstructorEditText;
    private EditText coursePhoneEditText;
    private EditText courseEmailEditText;
    private EditText courseNotesEditText;
    private List<String> courseStatusStrings = Arrays.asList("In Progress", "Completed", "Dropped", "Plan to Take");

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        if(getIntent().getIntExtra("id", 0) == 0){
            mCourse = new Course(mCourseName, mCourseStart, mCourseEnd, mCourseStatus, mCourseInstructor, mCoursePhone, mCourseEmail, mCourseNotes, associatedAssessmentIds);
        } else {
            mCourse = repo.getCourseById(getIntent().getIntExtra("id", 0));
        }

        courseNameEditText = findViewById(R.id.courseNameEditText);
        courseNameEditText.setText(mCourse.getCourseName());

        TextView courseStartTextView = findViewById(R.id.courseStartTextView);
        TextView courseEndTextView = findViewById(R.id.courseEndTextView);
        courseStartTextView.setText(mCourse.getCourseStart().format(formatter));
        courseEndTextView.setText(mCourse.getCourseEnd().format(formatter));

        Spinner courseStatusSpinner = findViewById(R.id.courseStatusSpinner);
        ArrayAdapter<String> courseStatusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, courseStatusStrings){};
        courseStatusSpinner.setAdapter(courseStatusAdapter);
        courseStatusSpinner.setOnItemSelectedListener(this);

        EditText courseInstructorEditText = findViewById(R.id.courseInstructorEditText);
        courseInstructorEditText.setText(mCourse.getCourseInstructor());

        EditText coursePhoneEditText = findViewById(R.id.coursePhoneEditText);
        coursePhoneEditText.setText(mCourse.getInstructorPhone());

        EditText courseEmailEditText = findViewById(R.id.courseEmailEditText);
        courseEmailEditText.setText(mCourse.getInstructorEmail());

        EditText courseNotesEditText = findViewById(R.id.courseNotesEditText);
        courseNotesEditText.setText(mCourse.getCourseNotes());

        populateFields();

    }

    @Override
    public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.courseStatusSpinner:
                mCourseStatus = (String) courseStatusStrings.get(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch(parent.getId()){
            case R.id.termDetailAddCourseSpinner:
                Log.d(LOG_ID, "Nothing selected");
                break;
            case R.id.termDetailDeleteCourseSpinner:
                Log.d(LOG_ID, "Nothing selected delete");
                break;
        }
    }

    public void populateFields(){
        //First check to see if we are editing an existing course or making a new one
        if(mCourse.getCourseId() != null) {
            //Find our associated terms
            associatedTermIds = repo.getAssociatedTerms(mCourse);
            //associatedTermIds = new ArrayList<>();
            for (Integer i : associatedTermIds) {
                associatedTerms.add(repo.getTermById(i));
            }
            //Find our associated assessments
            associatedAssessmentIds = mCourse.getAssociatedAssessments();
            for (Integer i : associatedAssessmentIds){
                associatedAssessments.add(repo.getAssessmentById(i));
            }
        }

        //Take that associated Term information and populate the terms recyclerview
        RecyclerView courseAssociatedTermsRecyclerView = findViewById(R.id.courseDetailTermsRecyclerView);
        final TermAdaptor termAdaptor = new TermAdaptor(this);
        courseAssociatedTermsRecyclerView.setAdapter(termAdaptor);
        courseAssociatedTermsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdaptor.setTerms(associatedTerms);

        //Populate the Add and Delete Spinners for Terms
        courseAddTermStrings = new ArrayList<>();
        courseDeleteTermStrings = new ArrayList<>();
        courseAddTermStrings.add("Add");
        courseDeleteTermStrings.add("Delete");
        List<Term> allTerms = repo.getAllTerms();
        for(Term t : allTerms){
            if(associatedTermIds.contains(t.getTermId())){
                courseDeleteTermStrings.add(t.toString());
            } else {
                courseAddTermStrings.add(t.toString());
            }
        }
        Spinner courseAddTermSpinner = findViewById(R.id.courseAddTermSpinner);
        ArrayAdapter<String> courseTermAddAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, courseAddTermStrings) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.WHITE);
                    textview.setBackgroundColor(Color.BLACK);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        courseAddTermSpinner.setAdapter(courseTermAddAdapter);
        courseAddTermSpinner.setOnItemSelectedListener(this);

        Spinner courseDeleteTermSpinner = findViewById(R.id.courseDeleteTermSpinner);
        ArrayAdapter<String> courseDeleteTermAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, courseDeleteTermStrings) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.WHITE);
                    textview.setBackgroundColor(Color.BLACK);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        courseDeleteTermSpinner.setAdapter(courseDeleteTermAdapter);
        courseDeleteTermSpinner.setOnItemSelectedListener(this);

        //populate assessment RecyclerView
        RecyclerView courseAssociatedAssessmentsRecyclerView = findViewById(R.id.courseDetailAssessmentsRecyclerView);
        final AssessmentAdaptor assessmentAdaptor = new AssessmentAdaptor(this);
        courseAssociatedAssessmentsRecyclerView.setAdapter(assessmentAdaptor);
        courseAssociatedAssessmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdaptor.setAssessments(associatedAssessments);

        //populate add / delete spinners for assessments
        courseAddAssessmentStrings = new ArrayList<>();
        courseDeleteAssessmentStrings = new ArrayList<>();
        courseAddAssessmentStrings.add("Add");
        courseDeleteAssessmentStrings.add("Delete");
        List<Assessment> allAssessments = repo.getAllAssessments();
        for(Assessment a : allAssessments){
            if(associatedAssessmentIds.contains(a.getAssessmentId())){
                courseDeleteAssessmentStrings.add(a.toString());
            } else {
                courseAddAssessmentStrings.add(a.toString());
            }
        }
        Spinner courseAddAssessmentSpinner = findViewById(R.id.courseAddAssessmentSpinner);
        ArrayAdapter<String> courseAssessmentAddAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, courseAddAssessmentStrings) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.WHITE);
                    textview.setBackgroundColor(Color.BLACK);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        courseAddAssessmentSpinner.setAdapter(courseAssessmentAddAdapter);
        courseAddAssessmentSpinner.setOnItemSelectedListener(this);

        Spinner courseDeleteAssessmentSpinner = findViewById(R.id.courseDeleteAssessmentSpinner);
        ArrayAdapter<String> courseDeleteAssessmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, courseDeleteAssessmentStrings) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.WHITE);
                    textview.setBackgroundColor(Color.BLACK);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        courseDeleteAssessmentSpinner.setAdapter(courseDeleteAssessmentAdapter);
        courseDeleteAssessmentSpinner.setOnItemSelectedListener(this);


    }

}
