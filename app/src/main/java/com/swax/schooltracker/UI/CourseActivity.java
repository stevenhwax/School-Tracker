package com.swax.schooltracker.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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
import Recievers.NotificationReciever;

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
    private List<Integer> associatedAssessmentIds = new ArrayList<>();
    private List<Assessment> associatedAssessments = new ArrayList<>();
    private List<String> courseAddAssessmentStrings;
    private List<String> courseDeleteAssessmentStrings;
    private Repository repo;
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

        repo = new Repository(getApplication());

        if(getIntent().getIntExtra("id", 0) == 0){
            mCourse = new Course(mCourseName, mCourseStart, mCourseEnd, mCourseStatus, mCourseInstructor, mCoursePhone, mCourseEmail, mCourseNotes, associatedAssessmentIds);
        } else {
            mCourse = repo.getCourseById(getIntent().getIntExtra("id", 0));
            mCourseStart = mCourse.getCourseStart();
            mCourseEnd = mCourse.getCourseEnd();
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

        courseInstructorEditText = findViewById(R.id.courseInstructorEditText);
        courseInstructorEditText.setText(mCourse.getCourseInstructor());

        coursePhoneEditText = findViewById(R.id.coursePhoneEditText);
        coursePhoneEditText.setText(mCourse.getInstructorPhone());

        courseEmailEditText = findViewById(R.id.courseEmailEditText);
        courseEmailEditText.setText(mCourse.getInstructorEmail());

        courseNotesEditText = findViewById(R.id.courseNotesEditText);
        courseNotesEditText.setText(mCourse.getCourseNotes());

        populateFields();

    }

    @Override
    public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.courseStatusSpinner:
                mCourseStatus = (String) courseStatusStrings.get(position);
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
                if (validateFields()){
                    String startMessage = mCourse.getCourseName() + " is starting at " + mCourse.getCourseStart().format(formatter);
                    ZoneId zoneId = ZoneId.systemDefault();
                    Long startTime = mCourse.getCourseStart().atStartOfDay(zoneId).toInstant().toEpochMilli();
                    String endMessage = mCourse.getCourseName() + " is ending at " + mCourse.getCourseEnd().format(formatter);
                    Long endTime = mCourse.getCourseEnd().atStartOfDay(zoneId).toInstant().toEpochMilli();
                    if (mCourse.getCourseId() != null){
                        setNotification(startTime, startMessage, mCourse.getCourseId());
                        setNotification(endTime, endMessage, mCourse.getCourseId() + 100000);
                        repo.update(mCourse);
                    } else {
                        setNotification(startTime, startMessage, repo.getMaxCourseId() + 1);
                        setNotification(endTime, endMessage, repo.getMaxCourseId() + 100001);
                        repo.insert(mCourse);
                    }
                    startActivity(intent);
                }
                return true;
            case R.id.delete:
                Log.d(LOG_ID, "You clicked delete!");
                if(!mCourse.getAssociatedAssessments().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                    builder.setTitle("Error Deleting")
                            .setMessage("There are still assessments associated with this course.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Do nothing
                                }
                            })
                            .create()
                            .show();
                }
                if(mCourse.getCourseId() != null && mCourse.getAssociatedAssessments().isEmpty()){
                    deleteNotification(mCourse.getCourseId());
                    deleteNotification(mCourse.getCourseId() + 100000);
                    repo.delete(mCourse);
                    startActivity(intent);
                }
                return true;
            case R.id.share:
                String message = "Course Name: " + mCourse.getCourseName() + " Course Notes: " + mCourse.getCourseNotes();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TITLE, "Course Notes from SchoolTracker");
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                shareIntent.setType("text/plain");
                Intent sendIntent = Intent.createChooser(shareIntent, null);
                startActivity(sendIntent);
                return true;
        }
        return false;
    }

    public void populateFields(){
        //First check to see if we are editing an existing course or making a new one
        if (mCourse.getCourseId() != null && associatedAssessmentIds.isEmpty()) {
            //Find our associated assessments
            associatedAssessmentIds = mCourse.getAssociatedAssessments();
            for (Integer i : associatedAssessmentIds) {
                if(repo.getAssessmentById(i) != null){
                    associatedAssessments.add(repo.getAssessmentById(i));
                } else {
                    associatedAssessmentIds.remove(i);
                }
            }
        }

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

    public Boolean validateFields() {
        Boolean validated = true;
        String errorMessage = "";

        //Check that Start Date is earlier than End Date
        if(mCourse.getCourseStart().isAfter(mCourse.getCourseEnd())){
            validated = false;
            errorMessage = errorMessage + "Start date is after End Date. \n";
        }

        //Check that the phone number matches expected patterns
        //Regex care of: https://stackoverflow.com/questions/42104546/java-regular-expressions-to-validate-phone-numbers
        String phoneRegex = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        if(!mCourse.getInstructorPhone().matches(phoneRegex)){
            validated = false;
            errorMessage = errorMessage + "Phone number is not entered correctly. \n";
        }

        //Check that the email matches expected patterns
        //Regex care of: https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
        String emailRegex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if(!mCourse.getInstructorEmail().matches(emailRegex)){
            validated = false;
            errorMessage = errorMessage + "Email address is not entered correctly. ";
        }

        if(!validated) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
            builder.setTitle("Error Saving")
                    .setMessage(errorMessage)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Do nothing
                        }
                    })
                    .create()
                    .show();
        }

        return validated;
    }

    public void setNotification(Long trigger, String message, Integer id){
        Intent intent = new Intent(CourseActivity.this, NotificationReciever.class);
        intent.putExtra("message", message);
        PendingIntent sender = PendingIntent.getBroadcast(CourseActivity.this, id, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    public void deleteNotification(Integer id){
        Intent intent = new Intent(CourseActivity.this, NotificationReciever.class);
        PendingIntent sender = PendingIntent.getBroadcast(CourseActivity.this, id,  intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

}
