package com.example.myapp.actfeeds.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.myapp.actfeeds.MainActivity;
import com.example.myapp.actfeeds.R;
import com.example.myapp.actfeeds.data.HabitDbHelper;

/**
 * Created by KavitaPC on 11/20/2017.
 */

public class NotificationUtils {

    private static final int NOTIFICATION_ID = 101;
    private static final int  ACTIVITY_REMINDER_PENDING_INTENT_ID = 102;
    private static final int ACTION_PEROFORM_ACTIVITY_PENDING_INTENT_ID =103;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 104;

    public static void clearAllPreviousNotifications(Context context){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void reminderUserPendingActivity(Context context){

        String reminderTitle = "ActFeeds";
        String reminderBody = "Have you checked your daily activities.";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setLargeIcon(largeIcon(context))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(reminderBody))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent(context))
                .addAction(performActivityAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);


        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(reminderTitle)
                .setContentText(reminderBody);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        /* ACTIVITY_REMINDER_NOTIFICATION_ID allows you to update or cancel the notification later on */
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
    private static NotificationCompat.Action ignoreReminderAction(Context context) {
        Intent ignoreReminderIntent = new Intent(context, ActivityReminderIntentService.class);
        ignoreReminderIntent.setAction(ReminderTask.ACTION_IGNORE_ACTIVITY);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.drawable.ic_close_black_24dp,
                "No, thanks.",
                ignoreReminderPendingIntent);
        return ignoreReminderAction;
    }

    private static NotificationCompat.Action performActivityAction(Context context) {
        Intent completeActivityIntent = new Intent(context, ActivityReminderIntentService.class);
        completeActivityIntent.setAction(ReminderTask.ACTION_PERFORM_ACTIVITY);
        PendingIntent performActivityPendingIntent = PendingIntent.getService(
                context,
                ACTION_PEROFORM_ACTIVITY_PENDING_INTENT_ID,
                completeActivityIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action completeActivityAction = new NotificationCompat.Action(R.drawable.ic_done_black_24dp,
                "I did it!",
                performActivityPendingIntent);
        return completeActivityAction;
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                ACTIVITY_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_sentiment_laugh_satisfied_black_24dp);
        return largeIcon;
    }

    public void getDataForReminder(Context context){
        HabitDbHelper dbHelper = new HabitDbHelper(context);
        SQLiteDatabase sqldb = dbHelper.getReadableDatabase();
        String query = " SELECT * FROM ";

    }
}
