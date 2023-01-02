package com.swax.schooltracker.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swax.schooltracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static int NUMBER_OF_THREADS=4;
    static final ExecutorService courseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

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

        courseStartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myCalendar.setTime(sdf.parse(courseStartTextView.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(CourseActivity.this, startDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate=new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                courseStartTextView.setText(sdf.format(myCalendar.getTime()));
                mCourseStart = LocalDateTime.ofInstant(myCalendar.toInstant(), ZoneId.systemDefault()).toLocalDate();
            }
        };

        courseEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myCalendar.setTime(sdf.parse(courseEndTextView.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(CourseActivity.this, endDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate=new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                courseEndTextView.setText(sdf.format(myCalendar.getTime()));
                mCourseEnd = LocalDateTime.ofInstant(myCalendar.toInstant(), ZoneId.systemDefault()).toLocalDate();
            }
        };

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
            case R.id.courseAddTermSpinner:
                if (position != 0){
                    Term courseAddTermSelected = repo.getTermFromString((String) courseAddTermStrings.get(position));
                    associatedTerms.add(courseAddTermSelected);
                    associatedTermIds.add(courseAddTermSelected.getTermId());
                    populateFields();
                }
                break;
            case R.id.courseDeleteTermSpinner:
                if (position != 0){
                    Term courseDeleteTermSelected = repo.getTermFromString((String) courseDeleteTermStrings.get(position));
                    associatedTerms.remove(position - 1);
                    associatedTermIds.remove(courseDeleteTermSelected.getTermId());
                    populateFields();
                }
                break;
            case R.id.courseAddAssessmentSpinner:
                if (position != 0){
                    Assessment courseAddAssessmentSelected = repo.getAssessmentFromString((String) courseAddAssessmentStrings.get(position));
                    associatedAssessments.add(courseAddAssessmentSelected);
                    associatedAssessmentIds.add(courseAddAssessmentSelected.getAssessmentId());
                    populateFields();
                }
                break;
            case R.id.courseDeleteAssessmentSpinner:
                if (position != 0){
                    Assessment courseDeleteAssessmentSelected = repo.getAssessmentFromString((String) courseDeleteAssessmentStrings.get(position));
                    associatedAssessments.remove(position - 1);
                    associatedAssessmentIds.remove(courseDeleteAssessmentSelected.getAssessmentId());
                    populateFields();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(LOG_ID, "Nothing Selected - so do nothing");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(CourseActivity.this, CourseListActivity.class);
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.save:
                Log.d(LOG_ID, "You clicked save!");
                mCourse.setCourseName(courseNameEditText.getText().toString());
                mCourse.setCourseStart(mCourseStart);
                mCourse.setCourseEnd(mCourseEnd);
                mCourse.setCourseStatus(mCourseStatus);
                mCourse.setCourseInstructor(courseInstructorEditText.getText().toString());
                mCourse.setInstructorPhone(coursePhoneEditText.getText().toString());
                mCourse.setInstructorEmail(courseEmailEditText.getText().toString());
                mCourse.setCourseNotes(courseNotesEditText.getText().toString());
                if (mCourse.getCourseId() != null){
                   /* for (Integer i : associatedTermIds){
                        if(!repo.getAssociatedTerms(mCourse).contains(i)){
                            Term term = repo.getTermById(i);
                            List<Integer> assCoursesTemp = term.getAssociatedCourses();
                            term.setTermCourses(assCoursesTemp);
                            repo.update(term);
                        };
                    }

                    */
                    repo.update(mCourse);
                } else {
                    repo.insert(mCourse);
                }
                startActivity(intent);
                return true;
            case R.id.delete:
                Log.d(LOG_ID, "You clicked delete!");
                if(mCourse.getCourseId() != null){
                    repo.delete(mCourse);
                }
                startActivity(intent);
                return true;
        }
        return false;
    }

    public void populateFields(){
        //First check to see if we are editing an existing course or making a new one
        courseExecutor.execute(()-> {
            if (mCourse.getCourseId() != null && associatedTermIds.isEmpty()) {
                //Find our associated terms
                associatedTermIds = repo.getAssociatedTerms(mCourse);
                //associatedTermIds = new ArrayList<>();
                for (Integer i : associatedTermIds) {
                    associatedTerms.add(repo.getTermById(i));
                }
                //Find our associated assessments
                associatedAssessmentIds = mCourse.getAssociatedAssessments();
                for (Integer i : associatedAssessmentIds) {
                    associatedAssessments.add(repo.getAssessmentById(i));
                }
            }
        });

        //Take that associated Term information and populate the terms recyclerview
        RecyclerView courseAssociatedTermsRecyclerView = findViewById(R.id.courseDetailTermsRecyclerView);
        final TermAdaptor termAdaptor = new TermAdaptor(CourseActivity.this);
        courseAssociatedTermsRecyclerView.setAdapter(termAdaptor);
        courseAssociatedTermsRecyclerView.setLayoutManager(new LinearLayoutManager(CourseActivity.this));
        termAdaptor.setTerms(associatedTerms);

        //Populate the Add and Delete Spinners for Terms
        courseAddTermStrings = new ArrayList<>();
        courseDeleteTermStrings = new ArrayList<>();
        courseAddTermStrings.add("Add");
        courseDeleteTermStrings.add("Delete");
        List<Term> allTerms = repo.getAllTerms();
        for (Term t : allTerms) {
            if (associatedTermIds.contains(t.getTermId())) {
                courseDeleteTermStrings.add(t.toString());
            } else {
                courseAddTermStrings.add(t.toString());
            }
        }
        Spinner courseAddTermSpinner = findViewById(R.id.courseAddTermSpinner);
        ArrayAdapter<String> courseTermAddAdapter = new ArrayAdapter<String>(CourseActivity.this, android.R.layout.simple_spinner_dropdown_item, courseAddTermStrings) {
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
        courseAddTermSpinner.setOnItemSelectedListener(CourseActivity.this);

        Spinner courseDeleteTermSpinner = findViewById(R.id.courseDeleteTermSpinner);
        ArrayAdapter<String> courseDeleteTermAdapter = new ArrayAdapter<String>(CourseActivity.this, android.R.layout.simple_spinner_dropdown_item, courseDeleteTermStrings) {
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
        courseDeleteTermSpinner.setOnItemSelectedListener(CourseActivity.this);

        //populate assessment RecyclerView
        RecyclerView courseAssociatedAssessmentsRecyclerView = findViewById(R.id.courseDetailAssessmentsRecyclerView);
        final AssessmentAdaptor assessmentAdaptor = new AssessmentAdaptor(CourseActivity.this);
        courseAssociatedAssessmentsRecyclerView.setAdapter(assessmentAdaptor);
        courseAssociatedAssessmentsRecyclerView.setLayoutManager(new LinearLayoutManager(CourseActivity.this));
        assessmentAdaptor.setAssessments(associatedAssessments);

        //populate add / delete spinners for assessments
        courseAddAssessmentStrings = new ArrayList<>();
        courseDeleteAssessmentStrings = new ArrayList<>();
        courseAddAssessmentStrings.add("Add");
        courseDeleteAssessmentStrings.add("Delete");
        List<Assessment> allAssessments = repo.getAllAssessments();
        for (Assessment a : allAssessments) {
            if (associatedAssessmentIds.contains(a.getAssessmentId())) {
                courseDeleteAssessmentStrings.add(a.toString());
            } else {
                courseAddAssessmentStrings.add(a.toString());
            }
        }
        Spinner courseAddAssessmentSpinner = findViewById(R.id.courseAddAssessmentSpinner);
        ArrayAdapter<String> courseAssessmentAddAdapter = new ArrayAdapter<String>(CourseActivity.this, android.R.layout.simple_spinner_dropdown_item, courseAddAssessmentStrings) {
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
        courseAddAssessmentSpinner.setOnItemSelectedListener(CourseActivity.this);

        Spinner courseDeleteAssessmentSpinner = findViewById(R.id.courseDeleteAssessmentSpinner);
        ArrayAdapter<String> courseDeleteAssessmentAdapter = new ArrayAdapter<String>(CourseActivity.this, android.R.layout.simple_spinner_dropdown_item, courseDeleteAssessmentStrings) {
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
        courseDeleteAssessmentSpinner.setOnItemSelectedListener(CourseActivity.this);

    }

}
