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
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.kavitapc.fitnessreminder.data.HabitDbHelper;
import com.example.kavitapc.fitnessreminder.utilities.DatePickerFragment;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.kavitapc.fitnessreminder.data.HabitContract.*;

public class GoalDetails extends AppCompatActivity {

    TextView textViewStartDate;
    TextView textViewEndDate;
    String textStartDate;
    String textEndDate;


    private RecyclerView rvWeekDays;
    private WeekDaysRecycleViewAdapter weekDaysAdapter;
    private ArrayList<WeekDaysAttributes> addItemArray = new ArrayList<>();

    public static final String START_DATE_TEXT = "Start_Date";
    public static final String END_DATE_TEXT = "End_date";
    CheckBox checkBoxRepeat;
    private int mPriority;
    String title;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);


        //Setting goal title from selected goal
        Bundle b = getIntent().getExtras();
        title = b.getString("habitTitle");
        TextView textView= (TextView)findViewById(R.id.tvGoalTitle);
        textView.setText(title);
        // Initialize to highest mPriority by default (mPriority = 1)
        ((RadioButton) findViewById(R.id.rbHigh)).setChecked(true);
        mPriority = 1;


        //Data will persist on screen rotation and on switching between apps
        textViewStartDate = (TextView)findViewById(R.id.tvStartDate);
        textViewEndDate = (TextView) findViewById(R.id.tvEndDate);
        if(savedInstanceState!=null){
            textStartDate = savedInstanceState.getString(START_DATE_TEXT);
            textEndDate = savedInstanceState.getString(END_DATE_TEXT);
            textViewStartDate.setText(textStartDate);
            textViewEndDate.setText(textEndDate);
        }
        ////////////////////Add week days list
        addItemArray.add(new WeekDaysAttributes("Sun",true));
        addItemArray.add(new WeekDaysAttributes("Mon",true));
        addItemArray.add(new WeekDaysAttributes("Tue",true));
        addItemArray.add(new WeekDaysAttributes("Wed",true));
        addItemArray.add(new WeekDaysAttributes("Thur",true));
        addItemArray.add(new WeekDaysAttributes("Fri",true));
        addItemArray.add(new WeekDaysAttributes("Sat",true));

        rvWeekDays = (RecyclerView) findViewById(R.id.rvWeekDays);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvWeekDays.setLayoutManager(horizontalLayoutManagaer);

        checkBoxRepeat = (CheckBox)findViewById(R.id.cbRepeatOn);
        checkBoxRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxRepeat.isChecked()){
                    rvWeekDays.setVisibility(rvWeekDays.INVISIBLE);
                }else{
                    rvWeekDays.setVisibility(rvWeekDays.VISIBLE);
                }
            }
        });
        weekDaysAdapter = new WeekDaysRecycleViewAdapter(this,addItemArray);
        rvWeekDays.setAdapter(weekDaysAdapter);
        ///////////////////////////////////////

        save = (Button)findViewById(R.id.bSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveData();
                Intent intent = new Intent(GoalDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerFragment.TEXT_VIEW_ID, v.getId());
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        textStartDate = textViewStartDate.getText().toString();
        textEndDate = textViewEndDate.getText().toString();
        outState.putString(START_DATE_TEXT, textStartDate);
        outState.putString(END_DATE_TEXT, textEndDate);
    }

    private long saveData() {
            //Activity name = title
            //Priority = mPriority
            textStartDate = textViewStartDate.getText().toString();
            textEndDate = textViewEndDate.getText().toString();
            boolean isCheckRepeatOn = checkBoxRepeat.isChecked();
            if(textStartDate.toString().equals("Start Date")){
                textStartDate = (new Date()).toString().substring(0,10);
            }
            if(textEndDate.toString().equals("End Date")){
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, 30);
                textEndDate = (cal.getTime()).toString().substring(0,10);
            }

            HabitDbHelper mDbHelper = new HabitDbHelper(getBaseContext());
            SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(UserHabitDetailEntry.HABIT_NAME, title);
            contentValues.put(UserHabitDetailEntry.START_DATE, textStartDate);
            contentValues.put(UserHabitDetailEntry.END_DATE, textEndDate);
            contentValues.put(UserHabitDetailEntry.AVERAGE_TIME, 45);
            contentValues.put(UserHabitDetailEntry.REPEAT_DAILY, isCheckRepeatOn);
            contentValues.put(UserHabitDetailEntry.HABIT_PRIVATE, true);
            contentValues.put(UserHabitDetailEntry.DESCRIPTION, title);
        Log.d("id is:", "aaaaaaaaaaaaaaaaaaaaaaa"+title);
        Log.d("id is:", "aaaaaaaaaaaaaaaaaaaaaaa"+textStartDate);
            long newRowId = sqLiteDatabase.insert(UserHabitDetailEntry.TABLE_NAME, null, contentValues);
            Log.d("Row is","Row inserted...................."+newRowId);
            return newRowId;
        }



}

