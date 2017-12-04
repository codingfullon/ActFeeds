package com.example.kavitapc.fitnessreminder.reminder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by KavitaPC on 12/4/2017.
 */

public class NotificationJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag){
            case NotificationSyncJob.TAG:
                return new NotificationSyncJob();
            default:
                return null;
        }

    }
}
