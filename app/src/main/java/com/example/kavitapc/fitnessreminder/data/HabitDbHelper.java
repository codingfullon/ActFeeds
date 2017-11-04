package com.example.kavitapc.fitnessreminder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by KavitaPC on 10/26/2017.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Habit.db";
    private static final int DATABASE_VERSION = 1;

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //Table for each habit entry
        final String SQL_CREATE_HABIT_ENTRY = "CREATE TABLE"+ HabitContract.HabitEntry.TABLE_NAME +"("+
                 HabitContract.HabitEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                 HabitContract.HabitEntry.HABIT_NAME + "TEXT NOT NULL"+
                 HabitContract.HabitEntry.SUB_DETAIL + "TEXT" +
                HabitContract.HabitEntry.AVERAGE_TIME + "TEXT NOT NULL"+
                HabitContract.HabitEntry.ICON+"TEXT NOT NULL"+");";

        //Table to save habit details data added by user
        final String SQL_CREATE_USER_HABIT_DETAIL_ENTRY = "CREATE TABLE"+ HabitContract.UserHabitDetailEntry.TABLE_NAME +"("+
                HabitContract.UserHabitDetailEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                HabitContract.UserHabitDetailEntry.HABIT_NAME + "TEXT NOT NULL"+
                HabitContract.UserHabitDetailEntry.START_DATE + "TEXT DEFAULT CURRENT_DATE" +
                HabitContract.UserHabitDetailEntry.END_DATE + "DATE NOT NULL"+
                HabitContract.UserHabitDetailEntry.AVERAGE_TIME +"INT NOT NULL"+
                HabitContract.UserHabitDetailEntry.REPEAT_DAILY + "BOOLEAN NOT NULL" +
                HabitContract.UserHabitDetailEntry.HABIT_PRIVATE + "BOOLEAN NOT NULL"+
                HabitContract.UserHabitDetailEntry.DESCRIPTION+"TEXT"+ ");";

        //Days on which habit will be repeated
        final String SQL_CREATE_REPEAT_ON_DAYS_ENTRY = "CREATE TABLE"+ HabitContract.RepeatOnDaysEntry.TABLE_NAME +"("+
                HabitContract.RepeatOnDaysEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                HabitContract.RepeatOnDaysEntry.SUNDAY + "BOOLEAN NOT NULL"+
                HabitContract.RepeatOnDaysEntry.MONDAY + "BOOLEAN NOT NULL"+
                HabitContract.RepeatOnDaysEntry.TUESDAY + "BOOLEAN NOT NULL"+
                HabitContract.RepeatOnDaysEntry.WEDNESDAY + "BOOLEAN NOT NULL"+
                HabitContract.RepeatOnDaysEntry.THURSDAY + "BOOLEAN NOT NULL"+
                HabitContract.RepeatOnDaysEntry.FRIDAY + "BOOLEAN NOT NULL"+
                HabitContract.RepeatOnDaysEntry.SATURDAY + "BOOLEAN NOT NULL"+
                HabitContract.RepeatOnDaysEntry.HABIT_ID + "FOREIGN KEY NOT NULL"+
                ");";

        //To save contacts added by user
        final String SQL_CREATE_CONTACT_ENTRY = "CREATE TABLE"+ HabitContract.ContactEntry.TABLE_NAME +"("+
                HabitContract.ContactEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                HabitContract.ContactEntry.CONTACT_NAME + "TEXT NOT NULL" +");";

        //To map which activity added which contacts
        final String SQL_CREATE_HABIT_USER_ENTRY = "CREATE TABLE"+ HabitContract.HabitUserEntry.TABLE_NAME +"("+
                HabitContract.HabitUserEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                HabitContract.HabitUserEntry.CONTACT_ENTRY_ID + "TEXT" +");";
        db.execSQL(SQL_CREATE_USER_HABIT_DETAIL_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE IF EXISTS"+HabitContract.UserHabitDetailEntry.TABLE_NAME);
        onCreate(db);
    }
}
