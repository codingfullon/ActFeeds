package com.example.myapp.actfeeds.reminder;

import android.content.Context;

/**
 * Created by KavitaPC on 11/20/2017.
 */

public class ReminderTask {
    public static final String ACTION_SHOW_REMINDER = "show_reminder";
    public static final String ACTION_PERFORM_ACTIVITY ="perform_activity";
    public static final String ACTION_IGNORE_ACTIVITY = "ignore_activity";


    public static void executeTask(Context context, String action){
        if(ACTION_SHOW_REMINDER.equals(action)){
            showReminder(context);
        }
        else if(ACTION_PERFORM_ACTIVITY.equals(action)){
            setActivityCompleted(context);

        }
        else if(ACTION_IGNORE_ACTIVITY.equals(action)){
            NotificationUtils.clearAllPreviousNotifications(context);
        }
    }
    public static void showReminder(Context context){
        NotificationUtils.reminderUserPendingActivity(context);

    }

    public static void setActivityCompleted(Context context){
       // NotificationUtils.reminderUserPendingActivity(context);
        NotificationUtils.clearAllPreviousNotifications(context);

    }
}
