package com.inkeep.actfeeds;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inkeep.actfeeds.R;

import com.inkeep.actfeeds.data.HabitContract;
import com.inkeep.actfeeds.data.HabitDbHelper;

import com.inkeep.actfeeds.utilities.DatePickerFragment;
import com.inkeep.actfeeds.utilities.ItemAttributes;
import com.inkeep.actfeeds.utilities.ListViewCustomAdapter;
import com.inkeep.actfeeds.utilities.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import static com.inkeep.actfeeds.data.HabitContract.*;

public class GoalDetails extends AppCompatActivity {



    private RecyclerView rvWeekDays;
    private WeekDaysRecycleViewAdapter weekDaysAdapter;
    private ArrayList<WeekDaysAttributes> addItemArray = new ArrayList<>();
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
    //setting Date
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd yyyy",Locale.ENGLISH);
    TextView textViewStartDate;
    TextView textViewEndDate;
    String textStartDate;
    String textEndDate;
    public static final String START_DATE_TEXT = "Start_Date";
    public static final String END_DATE_TEXT = "End_date";


    CheckBox checkBoxRepeat;
    private EditText etCreateOwn;
    private String textCreateOwn;
    private int mPriority;
    String title;
    Button save;
    String iconName;
    private static final String IMAGE_ICON = "ImageIcon";
    public static final String fileName = "DailyHabitsKeyValue";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);


            Spinner mySpinner = findViewById(R.id.spinnerActivity);
            final ArrayList<ItemAttributes> dailyHabitsArray = new ArrayList<>();
            addData(dailyHabitsArray);

            ListViewCustomAdapter customAdapter = new ListViewCustomAdapter(this, R.layout.dwm_habits_rows, dailyHabitsArray);
            mySpinner.setAdapter(customAdapter);
            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = findViewById(R.id.tvGoalRow);
                    title = textView.getText().toString();

                    iconName = dailyHabitsArray.get(position).getIconName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //Get Text from create your own
            etCreateOwn = findViewById(R.id.etCreateOwn);

            // Initialize to highest mPriority by default (mPriority = 1)
            ((RadioButton) findViewById(R.id.rbHigh)).setChecked(true);
            mPriority = 1;

        //Data will persist on screen rotation and on switching between apps
            tvReminderTime = findViewById(R.id.tvTimePicker);
            etHours = findViewById(R.id.etHours);
            etMinutes = findViewById(R.id.etMins);
            textViewStartDate = (TextView) findViewById(R.id.tvStartDate);
            textViewEndDate = (TextView) findViewById(R.id.tvEndDate);
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
                addItemArray.add(new WeekDaysAttributes("Sun", true));
                addItemArray.add(new WeekDaysAttributes("Mon", true));
                addItemArray.add(new WeekDaysAttributes("Tue", true));
                addItemArray.add(new WeekDaysAttributes("Wed", true));
                addItemArray.add(new WeekDaysAttributes("Thu", true));
                addItemArray.add(new WeekDaysAttributes("Fri", true));
                addItemArray.add(new WeekDaysAttributes("Sat", true));

                rvWeekDays = findViewById(R.id.rvWeekDays);
                LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rvWeekDays.setLayoutManager(horizontalLayoutManagaer);

                checkBoxRepeat = findViewById(R.id.cbRepeatOn);

                weekDaysAdapter = new WeekDaysRecycleViewAdapter(this, addItemArray);
                rvWeekDays.setAdapter(weekDaysAdapter);
        ///////////////////////////////////////

                save = findViewById(R.id.bSave);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reminderTime = tvReminderTime.getText().toString();
                        textCreateOwn = etCreateOwn.getText().toString();
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
                        }else if(reminderTime.equals("Set Time")){
                            Toast.makeText(getBaseContext(), "Time can't be left blank",Toast.LENGTH_LONG).show();
                        }
                        else {
                            save.setEnabled(false);
                            saveData();
                            Toast.makeText(getBaseContext(), "Activity saved for 90 days",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(GoalDetails.this, MainActivity.class);
                            startActivity(intent);
                        }

                    }
                });

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

    private long saveData() {

        String nullString ="";
        if(!nullString.equals(textCreateOwn)){
            title=textCreateOwn;
            iconName = "ic_express_grattitude_black_24dp";
        }

        textStartDate = textViewStartDate.getText().toString();
        textEndDate = textViewEndDate.getText().toString();


        if (textStartDate.equals("Start Date")) {
            textStartDate = DATE_FORMAT.format(new Date());

        }
        if (textEndDate.equals("End Date")) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 30);
            textEndDate = DATE_FORMAT.format(cal.getTime());
        }
        //Create db object
        HabitDbHelper mDbHelper = new HabitDbHelper(getBaseContext());
        ContentValues contentValues = new ContentValues();
       // SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        long habitId = 0;
            try(SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase()) {


                contentValues.put(UserHabitDetailEntry.HABIT_NAME, title);
                contentValues.put(UserHabitDetailEntry.START_DATE, DATE_FORMAT.parse(textStartDate).getTime());
                contentValues.put(UserHabitDetailEntry.END_DATE, DATE_FORMAT.parse(textEndDate).getTime());
                contentValues.put(UserHabitDetailEntry.REMINDER_TIME, reminderTime);
                contentValues.put(UserHabitDetailEntry.ACTIVITY_HOURS, mHours);
                contentValues.put(UserHabitDetailEntry.ACTIVITY_MINUTES, mMins);
                contentValues.put(UserHabitDetailEntry.REPEAT_DAILY, checkBoxRepeat.isChecked());
                contentValues.put(UserHabitDetailEntry.HABIT_PRIVATE, true);
                contentValues.put(UserHabitDetailEntry.DESCRIPTION, title);
                contentValues.put(UserHabitDetailEntry.PRIORITY, mPriority);
                contentValues.put(UserHabitDetailEntry.ICON_NAME, iconName);


                habitId = sqLiteDatabase.insert(UserHabitDetailEntry.TABLE_NAME, null, contentValues);
                int id = (int)habitId;

                contentValues.clear();
                contentValues = new ContentValues();


                for (int i = 0; i < 7; i++) {

                    contentValues.put(RepeatOnDaysEntry.WEEK_DAY, weekDaysAdapter.itemList.get(i).itemName);

                    if(checkBoxRepeat.isChecked()) {
                        contentValues.put(RepeatOnDaysEntry.DAY_SELECTED, true);
                    }else{contentValues.put(RepeatOnDaysEntry.DAY_SELECTED, weekDaysAdapter.itemList.get(i).aBoolean);}

                    contentValues.put(RepeatOnDaysEntry.HABIT_ID, habitId);
                    long daysRowId = sqLiteDatabase.insert(RepeatOnDaysEntry.TABLE_NAME, null, contentValues);


                }




                //inserting data into status table
                final SimpleDateFormat DATE_FORMAT_STATUS = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                contentValues.clear();
                contentValues = new ContentValues();

                GregorianCalendar GCStatusDate = new GregorianCalendar();
                long diffMiliSec = (DATE_FORMAT.parse(textEndDate).getTime()-DATE_FORMAT.parse(textStartDate).getTime());
                long diffDays = TimeUnit.DAYS.convert(diffMiliSec,TimeUnit.MILLISECONDS)+1;
                for (int i = 0; i < diffDays; i++) {
                    if(i==0){
                        GCStatusDate.add(Calendar.DATE,0);
                    }else{
                        GCStatusDate.add(Calendar.DATE,1);
                    }
                    contentValues.put(HabitContract.HabitStatusEntry.DONE_FLAG, 0);
                    contentValues.put(HabitContract.HabitStatusEntry.DATE_COMPLETION, DATE_FORMAT_STATUS.format(GCStatusDate.getTime()));
                    contentValues.put(HabitContract.HabitStatusEntry.HABIT_ID, habitId);
                    long statusID = sqLiteDatabase.insert(HabitStatusEntry.TABLE_NAME, null, contentValues);

                }



                mDbHelper.close();

            }
            catch (ParseException e){
                e.printStackTrace();
            }
        return habitId;
    }

    public void addData(ArrayList<ItemAttributes> dailyHabitsArray){

        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WorkOnSecretProject), "ic_secret_agent_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.ExerciseReminder), "ic_exercise_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.DeclutterPlace), "ic_wiping_swipe_clean_tidy"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Running), "ic_directions_run_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Networking), "ic_people_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WaterReminder), "ic_drink_water_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.YogaReminder), "ic_yoga_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.FruitsReminder), "ic_fruits_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.ExpressGratitude), "ic_express_grattitude_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.LaughLaughAndLaugh), "ic_sentiment_laugh_satisfied_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Read), "ic_read_book_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WriteSomething), "ic_write_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Meditate), "ic_meditation_yoga_posture"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.LearnNewWords), "ic_book_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.EatBowlSalad), "ic_fruits_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Study), "ic_read_book_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CleanTidy), "ic_wiping_swipe_clean_tidy"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CheckToDo), "ic_book_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Breakfast), "ic_fruits_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Swimming), "ic_swim_pool_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Laundry), "ic_local_laundry_wash_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CheckBills), "ic_playlist_add_check_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.MakeToDo), "ic_write_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CareLovedOnes), "ic_sentiment_laugh_satisfied_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Socialize), "ic_read_book_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.LearnCooking), "ic_fruits_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.PlanInvestments), "ic_attach_money_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CheckStatement), "ic_read_book_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.PlanTravel), "ic_travel_car_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.RearrangeSpace), "ic_wiping_swipe_clean_tidy"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.PlanOccasions), "ic_write_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WakeUpEarly), "ic_wb_sunny_black_24dp"));

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

}


