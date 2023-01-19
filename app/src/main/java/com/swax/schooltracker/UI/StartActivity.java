package com.swax.schooltracker.UI;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.swax.schooltracker.R;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import Database.Repository;
import Entities.Assessment;
import Entities.Course;
import Entities.Term;
import Recievers.NotificationReciever;

public class StartActivity extends AppCompatActivity {

    private String LOG_ID = "StartActivity";
    private Context context = StartActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Repository repo = new Repository(getApplication());
        if(repo.getAllTerms().isEmpty()) {
            //Create test data
            LocalDate startDate = LocalDate.of(2022, 12, 10);
            LocalDate endDate = LocalDate.of(2022, 12, 25);
            List<Integer> associated = List.of(1, 2);
            Assessment ass1 = new Assessment("assessment1", startDate.atStartOfDay(), endDate.atStartOfDay(), "Test");
            Assessment ass2 = new Assessment("assessment2", startDate.atStartOfDay(), endDate.atStartOfDay(), "tetytest");
            Assessment ass3 = new Assessment("assessment3", startDate.atStartOfDay(), endDate.atStartOfDay(), "tetytest");
            Assessment ass4 = new Assessment("assessment4", startDate.atStartOfDay(), endDate.atStartOfDay(), "tetytest");
            Course course1 = new Course("course1", startDate, endDate, "status", "teacher", "2065551212", "teach@teacher.com", "notes", associated);
            Course course2 = new Course("course2", startDate, endDate, "status", "teacher", "2065551212", "teach@teacher.com", "notes", associated);
            Course course3 = new Course("course3", startDate, endDate, "status", "teacher", "2065551212", "teach@teacher.com", "notes", associated);
            Course course4 = new Course("course4", startDate, endDate, "status", "teacher", "2065551212", "teach@teacher.com", "notes", associated);
            Term term1 = new Term("term1", startDate, endDate, associated);
            Term term2 = new Term("term2", startDate, endDate, associated);
            Term term3 = new Term("term3", startDate, endDate, associated);
            Term term4 = new Term("term4", startDate, endDate, associated);

            repo.insert(ass1);
            repo.insert(ass2);
            repo.insert(ass3);
            repo.insert(ass4);
            repo.insert(course1);
            repo.insert(course2);
            repo.insert(course3);
            repo.insert(course4);
            repo.insert(term1);
            repo.insert(term2);
            repo.insert(term3);
            repo.insert(term4);
        }

        if(!areNotificationsEnabled()){
            String startMessage = "Thank you for installing School Tracker!";
            ZoneId zoneId = ZoneId.systemDefault();
            Long startTime = LocalDate.now().atStartOfDay(zoneId).toInstant().toEpochMilli();
            setNotification(startTime, startMessage, 1);
        }
    }

    public void termListButtonOnClick(View view){
        Intent intent = new Intent(StartActivity.this, TermListActivity.class);
        startActivity(intent);
    }

    public void courseListButtonOnClick(View view){
        Intent intent = new Intent(StartActivity.this, CourseListActivity.class);
        startActivity(intent);
    }

    public void assessmentListButtonOnClick(View view){
        Intent intent = new Intent(StartActivity.this, AssessmentListActivity.class);
        startActivity(intent);
    }

    //Method code pulled from https://stackoverflow.com/questions/38198775/android-app-detect-if-app-push-notification-is-off
    public boolean areNotificationsEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (!manager.areNotificationsEnabled()) {
                return false;
            }
            List<NotificationChannel> channels = manager.getNotificationChannels();
            for (NotificationChannel channel : channels) {
                if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                    return false;
                }
            }
            return true;
        } else {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
    }

    public void setNotification(Long trigger, String message, Integer id){
        Intent intent = new Intent(StartActivity.this, NotificationReciever.class);
        intent.putExtra("message", message);
        PendingIntent sender = PendingIntent.getBroadcast(StartActivity.this, id, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

}
