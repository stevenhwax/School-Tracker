package com.swax.schooltracker.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.swax.schooltracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Database.Repository;
import Entities.Assessment;
import Recievers.NotificationReciever;

public class AssessmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String LOG_ID = "AssessmentActivity";
    private Assessment mAssessment;
    private String mAssessmentName = "New Assessment";
    private LocalDateTime assessmentTime = LocalDateTime.now();
    private String mAssessmentType = "Objective";
    private List<String> assessmentTypeStrings = Arrays.asList("Objective", "Performance");
    private Repository repo = new Repository(getApplication());
    private DatePickerDialog.OnDateSetListener startDatePicker;
    private DatePickerDialog.OnDateSetListener endDatePicker;
    private LocalDate startDate;
    private LocalDate endDate;
    private int startHours;
    private int startMinutes;
    private int endHours;
    private int endMinutes;
    private List<String> hourOptions;
    private List<String> minuteOptions;
    private final Calendar myCalendar = Calendar.getInstance();
    private String myDateFormat = "MM/dd/yyyy";
    private SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(myDateFormat);
    private String myDateTimeFormat = "MM/dd/yyyy HH:mm";
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(myDateTimeFormat);
    private EditText assessmentNameEditText;
    private TextView assessmentStartTextView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        if(getIntent().getIntExtra("id", 0) == 0){
            mAssessment = new Assessment(mAssessmentName, roundMinutes(assessmentTime), roundMinutes(assessmentTime), mAssessmentType);
        } else {
            mAssessment = repo.getAssessmentById(getIntent().getIntExtra("id", 0));
        }

        startDate = mAssessment.getAssessmentStart().toLocalDate();
        startHours = mAssessment.getAssessmentStart().getHour();
        startMinutes = mAssessment.getAssessmentStart().getMinute();
        endDate = mAssessment.getAssessmentEnd().toLocalDate();
        endHours = mAssessment.getAssessmentEnd().getHour();
        endMinutes = mAssessment.getAssessmentEnd().getMinute();

        assessmentNameEditText = findViewById(R.id.assessmentNameEditText);
        assessmentNameEditText.setText(mAssessment.getAssessmentName());

        assessmentStartTextView = findViewById(R.id.assessmentStartTextView);
        assessmentStartTextView.setText(mAssessment.getAssessmentStart().format(dateFormatter));

        assessmentStartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myCalendar.setTime(sdf.parse(assessmentStartTextView.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AssessmentActivity.this, startDatePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDatePicker=new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                assessmentStartTextView.setText(sdf.format(myCalendar.getTime()));
                startDate = LocalDateTime.ofInstant(myCalendar.toInstant(), ZoneId.systemDefault()).toLocalDate();
            }
        };

        hourOptions = new ArrayList<>();
        for(int i = 0; i < 24; i++){
            hourOptions.add(Integer.toString(i));
        }

        minuteOptions = Arrays.asList("00", "15", "30", "45");

        Spinner assessmentStartHoursSpinner = findViewById(R.id.assessmentStartHoursSpinner);
        ArrayAdapter<String> assessmentStartHoursAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hourOptions){};
        assessmentStartHoursSpinner.setAdapter(assessmentStartHoursAdapter);
        assessmentStartHoursSpinner.setOnItemSelectedListener(this);
        assessmentStartHoursSpinner.setSelection(startHours);

        Spinner assessmentStartMinutesSpinner = findViewById(R.id.assessmentStartMinutesSpinner);
        ArrayAdapter<String> assessmentStartMinutesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, minuteOptions){};
        assessmentStartMinutesSpinner.setAdapter(assessmentStartMinutesAdapter);
        assessmentStartMinutesSpinner.setOnItemSelectedListener(this);
        assessmentStartMinutesSpinner.setSelection(getSpinnerPosition(mAssessment.getAssessmentStart()));

        TextView assessmentEndTextView = findViewById(R.id.assessmentEndTextView);
        assessmentEndTextView.setText(mAssessment.getAssessmentEnd().format(dateFormatter));

        assessmentEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myCalendar.setTime(sdf.parse(assessmentEndTextView.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AssessmentActivity.this, endDatePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDatePicker=new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                assessmentEndTextView.setText(sdf.format(myCalendar.getTime()));
                endDate = LocalDateTime.ofInstant(myCalendar.toInstant(), ZoneId.systemDefault()).toLocalDate();
            }
        };

        Spinner assessmentEndHoursSpinner = findViewById(R.id.assessmentEndHoursSpinner);
        ArrayAdapter<String> assessmentEndHoursAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hourOptions){};
        assessmentEndHoursSpinner.setAdapter(assessmentEndHoursAdapter);
        assessmentEndHoursSpinner.setOnItemSelectedListener(this);
        assessmentEndHoursSpinner.setSelection(endHours);

        Spinner assessmentEndMinutesSpinner = findViewById(R.id.assessmentEndMinutesSpinner);
        ArrayAdapter<String> assessmentEndMinutesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, minuteOptions){};
        assessmentEndMinutesSpinner.setAdapter(assessmentEndMinutesAdapter);
        assessmentEndMinutesSpinner.setOnItemSelectedListener(this);
        assessmentEndMinutesSpinner.setSelection(getSpinnerPosition(mAssessment.getAssessmentEnd()));

        Spinner assessmentTypeSpinner = findViewById(R.id.assessmentTypeSpinner);
        ArrayAdapter<String> assessmentTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, assessmentTypeStrings){};
        assessmentTypeSpinner.setAdapter(assessmentTypeAdapter);
        assessmentTypeSpinner.setOnItemSelectedListener(this);
        if(mAssessment.getAssessmentType().equals("Objective")){
            assessmentTypeSpinner.setSelection(0);
        } else {
            assessmentTypeSpinner.setSelection(1);
        }
    }

    @Override
    public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.assessmentStartHoursSpinner:
                startHours = Integer.parseInt(hourOptions.get(position));
                break;
            case R.id.assessmentStartMinutesSpinner:
                startMinutes = Integer.parseInt(minuteOptions.get(position));
                break;
            case R.id.assessmentEndHoursSpinner:
                endHours = Integer.parseInt(hourOptions.get(position));
                break;
            case R.id.assessmentEndMinutesSpinner:
                endMinutes = Integer.parseInt(minuteOptions.get(position));
                break;
            case R.id.assessmentTypeSpinner:
                mAssessment.setAssessmentType((String) assessmentTypeStrings.get(position));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(LOG_ID, "Nothing Selected - so do nothing");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assessment, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(AssessmentActivity.this, AssessmentListActivity.class);
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.save:
                Log.d(LOG_ID, "You clicked save!");
                    mAssessment.setAssessmentName(assessmentNameEditText.getText().toString());
                    mAssessment.setAssessmentStart(startDate.atTime(startHours, startMinutes));
                    mAssessment.setAssessmentEnd(endDate.atTime(endHours, endMinutes));
                if (validateFields()){
                    if (mAssessment.getAssessmentId() != null){
                        repo.update(mAssessment);
                    } else {
                        repo.insert(mAssessment);
                    }
                    startActivity(intent);
                }
                return true;
            case R.id.delete:
                Log.d(LOG_ID, "You clicked delete!");
                if(mAssessment.getAssessmentId() != null){
                    deleteNotification(mAssessment.getAssessmentId() + 50000);
                    deleteNotification(mAssessment.getAssessmentId() + 500000);
                    repo.delete(mAssessment);
                }
                startActivity(intent);
                return true;
            case R.id.alertStart:
                String startAlertMessage = "Set alert for Start date of Assessment?";
                AlertDialog.Builder startBuilder = new AlertDialog.Builder(AssessmentActivity.this);
                startBuilder.setTitle("Set Alert")
                        .setMessage(startAlertMessage)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String startMessage = mAssessment.getAssessmentName() + " is starting at " + mAssessment.getAssessmentStart().format(dateTimeFormatter);
                                ZoneId zoneId = ZoneId.systemDefault();
                                ZoneOffset offset = zoneId.getRules().getOffset(mAssessment.getAssessmentStart());
                                Long startTime = mAssessment.getAssessmentStart().toInstant(offset).toEpochMilli();
                                if (mAssessment.getAssessmentId() != null){
                                    setNotification(startTime, startMessage, mAssessment.getAssessmentId() + 50000);
                                } else {
                                    setNotification(startTime, startMessage, repo.getMaxAssessmentId() + 50001);
                                }
                            }
                        })
                        .create()
                        .show();
                return true;
            case R.id.alertEnd:
                String endAlertMessage = "Set alert for End date of Assessment?";
                AlertDialog.Builder endBuilder = new AlertDialog.Builder(AssessmentActivity.this);
                endBuilder.setTitle("Set Alerts")
                        .setMessage(endAlertMessage)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ZoneId zoneId = ZoneId.systemDefault();
                                ZoneOffset offset = zoneId.getRules().getOffset(mAssessment.getAssessmentStart());
                                String endMessage = mAssessment.getAssessmentName() + " is ending at " + mAssessment.getAssessmentEnd().format(dateTimeFormatter);
                                Long endTime = mAssessment.getAssessmentEnd().toInstant(offset).toEpochMilli();
                                if (mAssessment.getAssessmentId() != null){
                                    setNotification(endTime, endMessage, mAssessment.getAssessmentId() + 500000);
                                } else {
                                    setNotification(endTime, endMessage, repo.getMaxAssessmentId() + 500001);
                                }
                            }
                        })
                        .create()
                        .show();
                return true;
        }
        return false;
    }

    public Boolean validateFields(){
        Boolean validated = true;
        String errorMessage = "";
        if(mAssessment.getAssessmentStart().isAfter(mAssessment.getAssessmentEnd())){
            validated = false;
            errorMessage = errorMessage + "Start date is after End date.";
        }
        if(!validated) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentActivity.this);
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

    public LocalDateTime roundMinutes(LocalDateTime time){
        LocalDate tempDate = time.toLocalDate();
        int tempHour = time.getHour();
        int tempMinute = time.getMinute();
        if(tempMinute < 15){
            tempMinute = 0;
        }
        if(tempMinute > 15 && tempMinute < 30){
            tempMinute = 15;
        }
        if(tempMinute > 30 && tempMinute < 45){
            tempMinute = 30;
        }
        if(tempMinute > 45){
            tempMinute = 45;
        }
        return tempDate.atTime(tempHour, tempMinute);
    }

    public int getSpinnerPosition(LocalDateTime time){
        int pos = 0;
        if (time.getMinute() < 15){
            pos = 0;
        }
        if (time.getMinute() >= 15 && time.getMinute() < 30){
            pos = 1;
        }
        if (time.getMinute() >= 30 && time.getMinute() < 45){
            pos = 2;
        }
        if (time.getMinute() >= 45){
            pos = 3;
        }
        return pos;
    }

    public void setNotification(Long trigger, String message, Integer id){
        Intent intent = new Intent(AssessmentActivity.this, NotificationReciever.class);
        intent.putExtra("message", message);
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentActivity.this, id, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    public void deleteNotification(Integer id){
        Intent intent = new Intent(AssessmentActivity.this, NotificationReciever.class);
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentActivity.this, id,  intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }
}
