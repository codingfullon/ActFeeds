package com.example.kavitapc.fitnessreminder.reminder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;


import com.evernote.android.job.DailyJob;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

/**
 * Created by KavitaPC on 12/4/2017.
 */

public class NotificationSyncJob extends DailyJob {

    public static final String TAG = "Notification_Job_Tag";

   /* @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Context context = getContext();
        ReminderTask.executeTask(context, ReminderTask.ACTION_SHOW_REMINDER);
        Log.d("ReminderTask", "Reminder showing");
        return Result.SUCCESS;
    }
    public static void scheduleJob(){
        new JobRequest.Builder(NotificationSyncJob.TAG)
                .setExecutionWindow(10_000L, 60_000L)
                .build()
                .schedule();
    }*/
    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        Context context = getContext();
        ReminderTask.executeTask(context, ReminderTask.ACTION_SHOW_REMINDER);
        Log.d("ReminderTask", "Reminder showing");
        return DailyJobResult.SUCCESS;
    }


    public static void schedule() {
        DailyJob.schedule(new JobRequest.Builder(TAG)
                , TimeUnit.MINUTES.toMillis(1), TimeUnit.HOURS.toMillis(0)+TimeUnit.SECONDS.toMillis(30));
    }

}