package com.example.kavitapc.fitnessreminder.reminder;

import com.firebase.jobdispatcher.JobParameters;
import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobService;

/**
 * Created by KavitaPC on 11/20/2017.
 */
//Currently not using this class
public class ActivityReminderFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Context context = ActivityReminderFirebaseJobService.this;
                ReminderTask.executeTask(context, ReminderTask.ACTION_SHOW_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
