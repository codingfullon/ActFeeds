package com.example.kavitapc.fitnessreminder.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by KavitaPC on 10/26/2017.
 */

public class HabitContract {

    private HabitContract(){}

    public static final String AUTHORITY = "com.example.kavitapc.fitnessreminder";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);   // The base content URI = "content://" + <authority>

    // Define the possible paths for accessing data in this contract
    public static final String PATH_USER_HABITS_Detail = "UserHabitDetail";  // This is the path for the "UserHabitDetail" directory



    //Table for each habit
    public static final class HabitEntry implements BaseColumns {
        public static final String TABLE_NAME = "Habit";
        public static final String HABIT_NAME = "HabitName";
        public static final String SUB_DETAIL = "Sub_Detail";
        public static final String AVERAGE_TIME = "AverageTime";
        public static final String ICON = "Icon";
    }

    //Table for each habit added by user
    public static final class UserHabitDetailEntry implements BaseColumns {

     //   public static final Uri CONTENT_URI =
       //         BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_HABITS_Detail).build();  // PATH_USER_HABITS_Detail content URI = base content URI + path
        public static final String TABLE_NAME = "UserHabitDetail";
        public static final String USER_HABIT_PK = "UserHabitPK";
        public static final String HABIT_NAME = "HabitName";
        public static final String START_DATE = "StartDate";
        public static final String AVERAGE_TIME = "AverageTime";
        public static final String END_DATE = "EndDate";
        public static final String REPEAT_DAILY = "RepeatDaily";
        public static final String HABIT_PRIVATE = "habit_private";
        public static final String DESCRIPTION = "Description";
        public static final String PRIORITY = "Priority";
        public static final String ICON_NAME = "IconName";
    }

    //Table for each habit's specific repeat on days
    public static final class RepeatOnDaysEntry implements BaseColumns {
        public static final String TABLE_NAME = "RepeatOnDays";
        public static final String WEEK_DAY_PK = "WeekDayPK";
        public static final String WEEK_DAY = "Day";
        public static final String DAY_SELECTED = "DaySelected";
        public static final String HABIT_ID = "Habit_Id";
    }

    //Table for habit's status
    public static final class HabitStatusEntry implements BaseColumns {
        public static final String TABLE_NAME = "HabitStatus";
        public static final String HABIT_STATUS_PK = "HabitStatusPK";
        public static final String DATE_COMPLETION = "DateOfCompletion";
        public static final String DONE_FLAG = "DoneFlag";
        public static final String HABIT_ID = "Habit_Id";
    }

    //Table for added contacts in app
    public static final class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "Contact";
        public static final String CONTACT_NAME = "Contact_Name";
    }

    //Table for added habit's specific users
    public static final class HabitUserEntry implements BaseColumns {
        public static final String TABLE_NAME = "HabitUser";
        public static final String CONTACT_ENTRY_ID = "ContactEntryId";
    }
}

