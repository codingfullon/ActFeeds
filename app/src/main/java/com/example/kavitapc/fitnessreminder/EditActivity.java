package com.example.kavitapc.fitnessreminder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
    boolean checkboxValue;
    private RecyclerView rvWeekDays;
    private WeekDaysRecycleViewAdapter weekDaysAdapter;
    private ArrayList<WeekDaysAttributes> addItemArray = new ArrayList<>();

    Button update;
    private int  idFromIntent=0;
    private String name;
    private String iconName;
    private int priority;
    private String reminderTimeStr;
    private int durationHoursValue=0;
    private int  durationMinutesValue=0;
    private int repeatDaily;


    private SQLiteDatabase sqlDb;
    ImageView imageViewIcon;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Get data from Intent
        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("UserHabitPK")){
            Log.d("datafrom added goal",""+intent.getIntExtra("UserHabitPK",0));
            idFromIntent = intent.getIntExtra("UserHabitPK",0);
            name= intent.getStringExtra("TitleName");
            iconName= intent.getStringExtra("IconName");
            priority= intent.getIntExtra("Priority",1);
            reminderTimeStr= intent.getStringExtra("TimeValue");
            durationHoursValue= intent.getIntExtra("DurationHours",0);
            durationMinutesValue= intent.getIntExtra("DurationMins",15);
            repeatDaily= intent.getIntExtra("RepeatDaily",1);


            checkboxValue = repeatDaily != 0;

        }

        TextView textViewTitle = findViewById(R.id.tvEditTitle);
        textViewTitle.setText(String.valueOf(name));

        imageViewIcon = findViewById(R.id.imageViewIcon);
        imageViewIcon.setImageResource(getResources().getIdentifier(iconName, "drawable", "com.example.kavitapc.fitnessreminder"));

        mPriority = priority;
        setPriority(mPriority);

        tvReminderTime = findViewById(R.id.tvTimePicker);
        tvReminderTime.setText(reminderTimeStr);

        etHours = findViewById(R.id.etHours);
        etMinutes = findViewById(R.id.etMins);
        etHours.setText(String.valueOf(durationHoursValue));
        etMinutes.setText(String.valueOf(durationMinutesValue));

        checkBoxRepeat = findViewById(R.id.cbRepeatOn);
        checkBoxRepeat.setChecked(checkboxValue);


        //Data will persist on screen rotation and on switching between apps
        if (savedInstanceState != null) {
            reminderTime = savedInstanceState.getString(REMINDER_TIME);
            mHours = savedInstanceState.getString(ACTIVITY_HOURS);
            mMins = savedInstanceState.getString(ACTIVITY_MINS);
            tvReminderTime.setText(reminderTime);
            etHours.setText(mHours);
            etMinutes.setText(mMins);
        }

        ////////////////////Add week days list
        boolean flag;
        HabitDbHelper mdbHelper = new HabitDbHelper(this);
        sqlDb = mdbHelper.getReadableDatabase();
        Cursor mCursor = getAllWeekDays();
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            addItemArray.add(new WeekDaysAttributes(mCursor.getString(0),flag= mCursor.getInt(1) == 1));
        }
        mdbHelper.close();
        sqlDb.close();

        rvWeekDays = findViewById(R.id.rvWeekDays);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvWeekDays.setLayoutManager(horizontalLayoutManager);


        /*checkBoxRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxRepeat.isChecked()) {
                    rvWeekDays.setVisibility(rvWeekDays.INVISIBLE);
                } else {
                    rvWeekDays.setVisibility(rvWeekDays.VISIBLE);
                }
            }
        });*/
        weekDaysAdapter = new WeekDaysRecycleViewAdapter(this, addItemArray);
        rvWeekDays.setAdapter(weekDaysAdapter);
        ///////////////////////////////////////


//update data
        update = findViewById(R.id.bUpdate);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderTime = tvReminderTime.getText().toString();
                mHours = etHours.getText().toString();
                mMins = etMinutes.getText().toString();
                int hrs=0;
                int mins=15;
                if(mHours.equals("") && mMins.equals("")){
                    Toast.makeText(getBaseContext(), "Activity duration can't be left blank",Toast.LENGTH_LONG).show();
                }else if(mHours.equals("") ){
                    hrs=0;
                    mins = Integer.valueOf(mMins);
                }else if(mMins.equals("")){
                    mins=0;
                    hrs = Integer.valueOf(mHours);
                }else{
                    hrs = Integer.valueOf(mHours);
                    mins = Integer.valueOf(mMins);
                }

                if (hrs==0 && mins ==0 ){
                    Toast.makeText(getBaseContext(), "Activity duration can't be zero",Toast.LENGTH_LONG).show();
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

        //Time = reminderTime
        //hours = mHours
        //Mins = mMins
        //onPrioritySelected(view);
        boolean isCheckRepeatOn = checkBoxRepeat.isChecked();

        HabitDbHelper mDbHelper = new HabitDbHelper(getBaseContext());
        ContentValues contentValues = new ContentValues();

        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase() ;


            contentValues.put(HabitContract.UserHabitDetailEntry.REMINDER_TIME, reminderTime);
            contentValues.put(HabitContract.UserHabitDetailEntry.ACTIVITY_HOURS, mHours);
            contentValues.put(HabitContract.UserHabitDetailEntry.ACTIVITY_MINUTES, mMins);
            contentValues.put(HabitContract.UserHabitDetailEntry.REPEAT_DAILY, isCheckRepeatOn);
            contentValues.put(HabitContract.UserHabitDetailEntry.PRIORITY, mPriority);

            String arg1 = " UserHabitPK="+ idFromIntent;
            long habitId = sqLiteDatabase.update(HabitContract.UserHabitDetailEntry.TABLE_NAME, contentValues,arg1,null);

            Log.d("Row is", "Row updated...................." + habitId);


            contentValues.clear();
            contentValues = new ContentValues();
            String []weeks = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            int i =0;
            for (String week: weeks) {
                if(isCheckRepeatOn) {
                    contentValues.put(HabitContract.RepeatOnDaysEntry.DAY_SELECTED, true);
                }else{contentValues.put(HabitContract.RepeatOnDaysEntry.DAY_SELECTED, weekDaysAdapter.itemList.get(i).aBoolean);}

                String arg2 = " Habit_Id="+ idFromIntent +" and Day= \""+ week+"\"";
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

    private Cursor getAllWeekDays() {
        return sqlDb.rawQuery(
                " select Day, DaySelected from RepeatOnDays where HABIT_ID = "+idFromIntent+" ",
                null);
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

    public void setPriority(int mPriority ){
        if (mPriority ==1) {
            ((RadioButton) findViewById(R.id.rbHigh)).setChecked(true);
            imageViewIcon.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.materialRed, null));
        } else if (mPriority == 2) {
            ((RadioButton) findViewById(R.id.rbMedium)).setChecked(true);
            imageViewIcon.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.materialOrange, null));
        } else if (mPriority ==3) {
            ((RadioButton) findViewById(R.id.rbLow)).setChecked(true);
            imageViewIcon.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.materialYellow, null));
        }
    }


}

