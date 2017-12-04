package com.example.kavitapc.fitnessreminder.reminder;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by KavitaPC on 11/20/2017.
 */
//Currently not using this class
public class ScheduleReminder {
    //private static final int REMINDER_INTERVAL_MINUTES = 1;
    //private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    //private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;
    private static final int SYNC_FLEXTIME_SECONDS  =10;
    private static final String REMINDER_JOB_TAG = "activity_reminder_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleActivityReminder(@NonNull final Context context) {
        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        /* Create the Job to periodically create reminders to perform activity */
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(ActivityReminderFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setTrigger(Trigger.executionWindow(
                        SYNC_FLEXTIME_SECONDS,
                        SYNC_FLEXTIME_SECONDS + SYNC_FLEXTIME_SECONDS))
                .build();

        dispatcher.schedule(constraintReminderJob);

        /* The job has been initialized */
        sInitialized = true;
    }

}






/*

Job constraintReminderJob = dispatcher.newJobBuilder()
                /* The Service that will be used to write to preferences
        .setService(WaterReminderFirebaseJobService.class)
                /*
                 * Set the UNIQUE tag used to identify this Job.

        .setTag(REMINDER_JOB_TAG)
                /*
                 * Network constraints on which this Job should run. In this app, we're using the
                 * device charging constraint so that the job only executes if the device is
                 * charging.
                 *
                 * In a normal app, it might be a good idea to include a preference for this,
                 * as different users may have different preferences on when you should be
                 * syncing your application's data.

        .setConstraints(Constraint.DEVICE_CHARGING)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.

        .setLifetime(Lifetime.FOREVER)
                /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 *
        .setRecurring(true)
                /*
                 * We want the reminders to happen every 15 minutes or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 *
        .setTrigger(Trigger.executionWindow(
                REMINDER_INTERVAL_SECONDS,
                REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.

        .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job
        .build();
*/