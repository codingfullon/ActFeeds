package com.example.myapp.actfeeds;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.actfeeds.R;

import com.example.myapp.actfeeds.data.HabitContract;
import com.example.myapp.actfeeds.data.HabitDbHelper;
import com.example.myapp.actfeeds.utilities.DatePickerFragment;
import com.example.myapp.actfeeds.utilities.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    //set dates
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH);
    TextView textViewStartDate;
    TextView textViewEndDate;
    String textStartDate;
    String textEndDate;
    public static final String START_DATE_TEXT = "Start_Date";
    public static final String END_DATE_TEXT = "End_date";


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
    private String getTextStartDate="0";
    private String getTextEndDate="0";
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
            idFromIntent = intent.getIntExtra("UserHabitPK",0);
            name= intent.getStringExtra("TitleName");
            iconName= intent.getStringExtra("IconName");
            getTextStartDate = intent.getStringExtra("StartDate");
            getTextEndDate = intent.getStringExtra("EndDate");
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
        imageViewIcon.setImageResource(getResources().getIdentifier(iconName, "drawable", "com.example.myapp.actfeeds"));

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
        textViewStartDate = (TextView) findViewById(R.id.tvStartDate);
        textViewEndDate = (TextView) findViewById(R.id.tvEndDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(getTextStartDate));
        textStartDate = DATE_FORMAT.format(calendar.getTime());

        calendar.setTimeInMillis(Long.valueOf(getTextEndDate));
        textEndDate = DATE_FORMAT.format(calendar.getTime());


        textViewStartDate.setText(textStartDate);
        textViewEndDate.setText(textEndDate);
        if (savedInstanceState != null) {
            reminderTime = savedInstanceState.getString(REMINDER_TIME);
            mHours = savedInstanceState.getString(ACTIVITY_HOURS);
            mMins = savedInstanceState.getString(ACTIVITY_MINS);
            tvReminderTime.setText(reminderTime);
            etHours.setText(mHours);
            etMinutes.setText(mMins);
            textStartDate = savedInstanceState.getString(START_DATE_TEXT);
            textEndDate = savedInstanceState.getString(END_DATE_TEXT);
            textViewStartDate.setText(textStartDate);
            textViewEndDate.setText(textEndDate);
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

        textStartDate = textViewStartDate.getText().toString();
        textEndDate = textViewEndDate.getText().toString();


        if (textStartDate.equals("Start Date")) {
            textStartDate = DATE_FORMAT.format(new Date());

        }
        if (textEndDate.equals("End Date")) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 90);
            textEndDate = DATE_FORMAT.format(cal.getTime());
        }

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

                i++;
            }



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

        textStartDate = textViewStartDate.getText().toString();
        textEndDate = textViewEndDate.getText().toString();
        outState.putString(START_DATE_TEXT, textStartDate);
        outState.putString(END_DATE_TEXT, textEndDate);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TimePickerFragment.TEXT_VIEW_ID, v.getId());
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerFragment.TEXT_VIEW_ID, v.getId());
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
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

