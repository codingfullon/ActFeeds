package com.example.kavitapc.fitnessreminder.reminder;

import android.app.Application;

import com.evernote.android.job.JobManager;

/**
 * Created by KavitaPC on 12/4/2017.
 */

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new NotificationJobCreator());
    }
}
