package com.example.myapp.actfeeds.reminder;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by KavitaPC on 11/20/2017.
 */

public class ActivityReminderIntentService extends IntentService{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ActivityReminderIntentService(String name) {
        super(name);
    }
    public ActivityReminderIntentService() {
        super("ActivityReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        ReminderTask.executeTask(this, action);

    }
}
