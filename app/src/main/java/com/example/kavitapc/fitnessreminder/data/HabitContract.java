package com.example.kavitapc.fitnessreminder.data;

import android.provider.BaseColumns;

/**
 * Created by KavitaPC on 10/26/2017.
 */

public class HabitContract {

    private HabitContract(){}

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
        public static final String TABLE_NAME = "UserHabitDetail";
        public static final String HABIT_NAME = "HabitName";
        public static final String START_DATE = "StartDate";
        public static final String AVERAGE_TIME = "AverageTime";
        public static final String END_DATE = "EndDate";
        public static final String REPEAT_DAILY = "RepeatDaily";
        public static final String HABIT_PRIVATE = "habit_private";
        public static final String DESCRIPTION = "Description";
    }

    //Table for each habit's specific repeat on days
    public static final class RepeatOnDaysEntry implements BaseColumns {
        public static final String TABLE_NAME = "RepeatOnDays";
        public static final String MONDAY = "Monday";
        public static final String TUESDAY = "Tuesday";
        public static final String WEDNESDAY = "Wednesday";
        public static final String THURSDAY = "Thursday";
        public static final String FRIDAY = "Friday";
        public static final String SATURDAY = "Saturday";
        public static final String SUNDAY = "Sunday";
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

