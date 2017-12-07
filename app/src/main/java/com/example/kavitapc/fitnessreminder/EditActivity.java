package com.example.kavitapc.fitnessreminder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kavitapc.fitnessreminder.data.HabitContract;
import com.example.kavitapc.fitnessreminder.data.HabitDbHelper;
import com.example.kavitapc.fitnessreminder.utilities.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    private int mPriority;

    //Setting time
    private TextView tvReminderTime;
    private EditText etHours;
    private EditText etMinutes;
    String reminderTime;
    String mHours;
    String mMins;
    public static final String REMINDER_TIME = "Reminder_Time";
    public static final String ACTIVITY_HOURS = "Hours";
    public static final String ACTIVITY_MINS= "Mins";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH);

    //set week days
    CheckBox checkBoxRepeat;
    private RecyclerView rvWeekDays;
    private WeekDaysRecycleViewAdapter weekDaysAdapter;
    private ArrayList<WeekDaysAttributes> addItemArray = new ArrayList<>();

    String title;
    Button update;
    String iconName;
    private static final String IMAGE_ICON = "ImageIcon";
    private static final String fileName = "DailyHabitsKeyValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        // Initialize to highest mPriority by default (mPriority = 1)
        ((RadioButton) findViewById(R.id.rbHigh)).setChecked(true);
         mPriority = 1;

        //Data will persist on screen rotation and on switching between apps
        tvReminderTime = (TextView) findViewById(R.id.tvTimePicker);
        etHours = (EditText) findViewById(R.id.etHours);
        etMinutes = (EditText) findViewById(R.id.etMins);
        if (savedInstanceState != null) {
            reminderTime = savedInstanceState.getString(REMINDER_TIME);
            mHours = savedInstanceState.getString(ACTIVITY_HOURS);
            mMins = savedInstanceState.getString(ACTIVITY_MINS);
            tvReminderTime.setText(reminderTime);
            etHours.setText(mHours);
            etMinutes.setText(mMins);
        }

        ////////////////////Add week days list
        addItemArray.add(new WeekDaysAttributes("Sun", true));
        addItemArray.add(new WeekDaysAttributes("Mon", true));
        addItemArray.add(new WeekDaysAttributes("Tue", true));
        addItemArray.add(new WeekDaysAttributes("Wed", true));
        addItemArray.add(new WeekDaysAttributes("Thu", true));
        addItemArray.add(new WeekDaysAttributes("Fri", true));
        addItemArray.add(new WeekDaysAttributes("Sat", true));

        rvWeekDays = (RecyclerView) findViewById(R.id.rvWeekDays);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvWeekDays.setLayoutManager(horizontalLayoutManagaer);

        checkBoxRepeat = (CheckBox) findViewById(R.id.cbRepeatOn);
        checkBoxRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxRepeat.isChecked()) {
                    rvWeekDays.setVisibility(rvWeekDays.INVISIBLE);
                } else {
                    rvWeekDays.setVisibility(rvWeekDays.VISIBLE);
                }
            }
        });
        weekDaysAdapter = new WeekDaysRecycleViewAdapter(this, addItemArray);
        rvWeekDays.setAdapter(weekDaysAdapter);
        ///////////////////////////////////////

        update = (Button) findViewById(R.id.bUpdate);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderTime = tvReminderTime.getText().toString();
                mHours = etHours.getText().toString();
                mMins = etMinutes.getText().toString();
                int hrs = Integer.valueOf(mHours);
                int mins = Integer.valueOf(mMins);
                if (hrs==0 && mins ==0 ){
                    Toast.makeText(getBaseContext(), "Activity duration can't be zero",Toast.LENGTH_LONG).show();
                }else if(reminderTime.equals("Set Time")){
                    Toast.makeText(getBaseContext(), "Time can't be left blank",Toast.LENGTH_LONG).show();
                }
                else {
                    update.setEnabled(false);
                    updateData();
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

    }
    private long updateData() {
        //Priority = mPriority
        //hours = mHours
        //Mins = mMins

        boolean isCheckRepeatOn = checkBoxRepeat.isChecked();

        HabitDbHelper mDbHelper = new HabitDbHelper(getBaseContext());
        ContentValues contentValues = new ContentValues();

        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase() ;


            contentValues.put(HabitContract.UserHabitDetailEntry.REMINDER_TIME, reminderTime);
            contentValues.put(HabitContract.UserHabitDetailEntry.ACTIVITY_HOURS, mHours);
            contentValues.put(HabitContract.UserHabitDetailEntry.ACTIVITY_MINUTES, mMins);
            contentValues.put(HabitContract.UserHabitDetailEntry.REPEAT_DAILY, isCheckRepeatOn);
            contentValues.put(HabitContract.UserHabitDetailEntry.PRIORITY, mPriority);
int id =10;
            String arg1 = " UserHabitPK="+ id;
            long habitId = sqLiteDatabase.update(HabitContract.UserHabitDetailEntry.TABLE_NAME, contentValues,arg1,null);

            Log.d("Row is", "Row updated...................." + habitId);


            contentValues.clear();
            contentValues = new ContentValues();
            String []weeks = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            int i =0;
            for (String week: weeks) {

                contentValues.put(HabitContract.RepeatOnDaysEntry.DAY_SELECTED, weekDaysAdapter.itemList.get(i).aBoolean);

                String arg2 = " UserHabitPK="+ id +" and Day= \""+ week+"\"";
                long daysRowId = sqLiteDatabase.update(HabitContract.RepeatOnDaysEntry.TABLE_NAME,contentValues, arg2, null);
                Log.d("inserted days", "" + daysRowId);
                i++;
            }
            Log.d("Row is", "Row updated...................." + habitId);


            mDbHelper.close();

        return habitId;
    }

    public void onPrioritySelected(View view){
        if (((RadioButton) findViewById(R.id.rbHigh)).isChecked()) {
            mPriority = 1;
        } else if (((RadioButton) findViewById(R.id.rbMedium)).isChecked()) {
            mPriority = 2;
        } else if (((RadioButton) findViewById(R.id.rbLow)).isChecked()) {
            mPriority = 3;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        reminderTime = tvReminderTime.getText().toString();
        mHours = etHours.getText().toString();
        mMins = etMinutes.getText().toString();
        outState.putString(REMINDER_TIME, reminderTime);
        outState.putString(ACTIVITY_HOURS, mHours);
        outState.putString(ACTIVITY_MINS, mMins);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TimePickerFragment.TEXT_VIEW_ID, v.getId());
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "TimePicker");
    }
}

