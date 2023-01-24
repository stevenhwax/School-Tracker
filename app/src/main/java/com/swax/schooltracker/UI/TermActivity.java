package com.swax.schooltracker.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.swax.schooltracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Database.Repository;
import Entities.Course;
import Entities.Term;
import Recievers.NotificationReciever;

public class TermActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String LOG_ID = "TermActivity";
    private Term mTerm;
    private String mTermName = "Enter Name";
    private LocalDate mTermStart = LocalDate.now();
    private LocalDate mTermEnd = LocalDate.now();
    private List<Integer> mTermAssociatedCourseIds = new ArrayList<>();
    private List<String> mTermDeleteCourseStrings;
    private List<String> mTermAddCourseStrings;
    private List<Course> mTermAssociatedCourses = new ArrayList<>();
    private Repository repo = new Repository(getApplication());
    private DatePickerDialog.OnDateSetListener startDate;
    private DatePickerDialog.OnDateSetListener endDate;
    private final Calendar myCalendar = Calendar.getInstance();
    private String myFormat = "MM/dd/yyyy";
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat);
    private EditText termNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        if(getIntent().getIntExtra("id", 0) == 0){
            mTerm = new Term(mTermName, mTermStart, mTermEnd, mTermAssociatedCourseIds);
        } else {
            mTerm = repo.getTermById(getIntent().getIntExtra("id", 0));
        }

        termNameEditText = findViewById(R.id.termNameEditText);
        TextView termStartTextView = findViewById(R.id.termStartTextView);
        TextView termEndTextView = findViewById(R.id.termEndTextView);

        termNameEditText.setText(mTerm.getTermName());
        termStartTextView.setText(mTerm.getTermStart().format(formatter));
        termEndTextView.setText(mTerm.getTermEnd().format(formatter));

        termStartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myCalendar.setTime(sdf.parse(termStartTextView.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(TermActivity.this, startDate, myCalendar
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
                termStartTextView.setText(sdf.format(myCalendar.getTime()));
                mTermStart = LocalDateTime.ofInstant(myCalendar.toInstant(), ZoneId.systemDefault()).toLocalDate();
            }
        };

        termEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myCalendar.setTime(sdf.parse(termEndTextView.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(TermActivity.this, endDate, myCalendar
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
                termEndTextView.setText(sdf.format(myCalendar.getTime()));
                mTermEnd = LocalDateTime.ofInstant(myCalendar.toInstant(), ZoneId.systemDefault()).toLocalDate();
            }
        };

        mTermAssociatedCourseIds = mTerm.getAssociatedCourses();

        for(Integer i : mTermAssociatedCourseIds){
            mTermAssociatedCourses.add(repo.getCourseById(i));
        }

        populateRecyclerView();
        populateAddSpinner();
        populateDeleteSpinner();

    }


    @Override
    public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.termDetailAddCourseSpinner:
                if(position!=0){
                    String addSpinnerSelected = (String) mTermAddCourseStrings.get(position);
                    Course addSelectedCourse = repo.getCourseFromString(addSpinnerSelected);
                    mTermAssociatedCourses.add(addSelectedCourse);
                    mTermAssociatedCourseIds.add(addSelectedCourse.getCourseId());
                    populateRecyclerView();
                    populateAddSpinner();
                    populateDeleteSpinner();
                }
                break;
            case R.id.termDetailDeleteCourseSpinner:
                if(position!=0){
                    String deleteSpinnerSelected = mTermDeleteCourseStrings.get(position);
                    Course deleteSelectedCourse = repo.getCourseFromString(deleteSpinnerSelected);
                    mTermAssociatedCourses.remove(position - 1);
                    mTermAssociatedCourseIds.remove(deleteSelectedCourse.getCourseId());
                    populateRecyclerView();
                    populateAddSpinner();
                    populateDeleteSpinner();
                }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_term, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(TermActivity.this, TermListActivity.class);
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.save:
                Log.d(LOG_ID, "You clicked save!");
                mTerm.setTermName(termNameEditText.getText().toString());
                mTerm.setTermStart(mTermStart);
                mTerm.setTermEnd(mTermEnd);
                mTerm.setAssociatedCourses(mTermAssociatedCourseIds);
                if(validateFields()){
                    /*
                    String startMessage = "Term " + mTerm.getTermName() + " is starting at " + mTerm.getTermStart().format(formatter);
                    ZoneId zoneId = ZoneId.systemDefault();
                    Long startTime = mTerm.getTermStart().atStartOfDay(zoneId).toEpochSecond();
                    setNotification(startTime, startMessage);
                    String endMessage = "Term " + mTerm.getTermName() + " is ending at " + mTerm.getTermEnd().format(formatter);
                    Long endTime = mTerm.getTermEnd().atStartOfDay(zoneId).toEpochSecond();
                    setNotification(endTime, endMessage);
                     */
                    if(mTerm.getTermId() == null){
                        repo.insert(mTerm);
                    } else {
                        repo.update(mTerm);
                    }
                    startActivity(intent);
                }
                return true;
            case R.id.delete:
                Log.d(LOG_ID, "You clicked delete!");

                if(!mTerm.getAssociatedCourses().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(TermActivity.this);
                    builder.setTitle("Error Deleting")
                            .setMessage("There are still courses associated with this term.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Do nothing
                                }
                            })
                            .create()
                            .show();
                }

                if(mTerm.getTermId() != null && mTerm.getAssociatedCourses().isEmpty()){
                    repo.delete(mTerm);
                    startActivity(intent);
                }
                return true;
        }
        return false;
    }

    public void populateAddSpinner(){
        //populate the first item so the drop down shows up right
        mTermAddCourseStrings = new ArrayList<>();
        mTermAddCourseStrings.add("Add");
        //populate the list of courses for the add dropdown
        for(Course c : repo.getAllCourses()){
            if(!mTermAssociatedCourseIds.contains(c.getCourseId()) ){
                mTermAddCourseStrings.add(c.toString());
            }
        }

        Spinner addCourseSpinner = findViewById(R.id.termDetailAddCourseSpinner);
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mTermAddCourseStrings) {
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
        addCourseSpinner.setAdapter(courseAdapter);
        addCourseSpinner.setOnItemSelectedListener(this);
    }

    public void populateDeleteSpinner(){
        mTermDeleteCourseStrings = new ArrayList<>();
        //populate the first item so the drop down shows up right
        mTermDeleteCourseStrings.add("Delete");
        //populate the list of courses for the delete dropdown
        for(Integer i : mTerm.getAssociatedCourses()){
            mTermDeleteCourseStrings.add(repo.getCourseById(i).toString());
        }

        Spinner deleteCourseSpinner = findViewById(R.id.termDetailDeleteCourseSpinner);
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mTermDeleteCourseStrings) {
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
        deleteCourseSpinner.setAdapter(courseAdapter);
        deleteCourseSpinner.setOnItemSelectedListener(this);
    }

    public void populateRecyclerView(){
        RecyclerView termDetailRecyclerView = findViewById(R.id.termDetailRecyclerView);
        if(mTermAssociatedCourses.isEmpty()){
            for(Integer i : mTerm.getAssociatedCourses()){
                if(repo.getCourseById(i) != null){
                    mTermAssociatedCourses.add(repo.getCourseById(i));
                } else {
                    List<Integer> courseIds = mTerm.getAssociatedCourses();
                    courseIds.remove(i);
                    mTerm.setAssociatedCourses(courseIds);
                }
            }
        }
        final CourseAdaptor adaptor = new CourseAdaptor(this);;
        termDetailRecyclerView.setAdapter(adaptor);
        termDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor.setCourses(mTermAssociatedCourses);
    }

    public Boolean validateFields(){
        Boolean validated = true;
        String errorMessage = "";
        if(MiscHelper.checkDates(mTermStart, mTermEnd)){
            validated = false;
            errorMessage = errorMessage + "Start date is after End date.";
        }
        if(!validated) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TermActivity.this);
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

}