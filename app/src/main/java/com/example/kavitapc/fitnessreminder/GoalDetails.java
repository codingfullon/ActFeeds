package com.example.kavitapc.fitnessreminder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kavitapc.fitnessreminder.data.HabitDbHelper;
import com.example.kavitapc.fitnessreminder.utilities.DatePickerFragment;
import com.example.kavitapc.fitnessreminder.utilities.ItemAttributes;
import com.example.kavitapc.fitnessreminder.utilities.ListViewCustomAdapter;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.kavitapc.fitnessreminder.data.HabitContract.*;

public class GoalDetails extends AppCompatActivity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd yyyy");
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
    String iconName;
    private static final String IMAGE_ICON = "ImageIcon";
    private static final String fileName = "DailyHabitsKeyValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);


        Spinner mySpinner = (Spinner)findViewById(R.id.spinnerActivity);
        final ArrayList<ItemAttributes> dailyHabitsArray = new ArrayList<>();
        addData(dailyHabitsArray);
        ListViewCustomAdapter customAdapter = new ListViewCustomAdapter(this, R.layout.dwm_habits_rows, dailyHabitsArray, fileName);
        mySpinner.setAdapter(customAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.tvGoalRow);
                title = textView.getText().toString();

                iconName = dailyHabitsArray.get(position).getIconName();
                Log.d("iiiiiiiiii", ""+iconName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Initialize to highest mPriority by default (mPriority = 1)
        ((RadioButton) findViewById(R.id.rbHigh)).setChecked(true);
        mPriority = 1;




        //Data will persist on screen rotation and on switching between apps
        textViewStartDate = (TextView) findViewById(R.id.tvStartDate);
        textViewEndDate = (TextView) findViewById(R.id.tvEndDate);
        if (savedInstanceState != null) {
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
        addItemArray.add(new WeekDaysAttributes("Thur", true));
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

        save = (Button) findViewById(R.id.bSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setEnabled(false);
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
        if (textStartDate.equals("Start Date")) {
            textStartDate = DATE_FORMAT.format(new Date());

        }
        if (textEndDate.equals("End Date")) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 30);
            textEndDate = DATE_FORMAT.format(cal.getTime());

        }

        HabitDbHelper mDbHelper = new HabitDbHelper(getBaseContext());
        ContentValues contentValues = new ContentValues();
        try (SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase()) {

            contentValues.put(UserHabitDetailEntry.HABIT_NAME, title);
            contentValues.put(UserHabitDetailEntry.START_DATE, DATE_FORMAT.parse(textStartDate).getTime());
            contentValues.put(UserHabitDetailEntry.END_DATE, DATE_FORMAT.parse(textEndDate).getTime());
            contentValues.put(UserHabitDetailEntry.AVERAGE_TIME, 45);
            contentValues.put(UserHabitDetailEntry.REPEAT_DAILY, isCheckRepeatOn);
            contentValues.put(UserHabitDetailEntry.HABIT_PRIVATE, true);
            contentValues.put(UserHabitDetailEntry.DESCRIPTION, title);
            contentValues.put(UserHabitDetailEntry.PRIORITY, mPriority);
            contentValues.put(UserHabitDetailEntry.ICON_NAME, iconName);

            
            long habitId = sqLiteDatabase.insert(UserHabitDetailEntry.TABLE_NAME, null, contentValues);


            Log.d("Row is", "Row inserted...................." + habitId);
            contentValues.clear();
            contentValues = new ContentValues();

            for (int i = 0; i < 7; i++) {
                contentValues.put(RepeatOnDaysEntry.WEEK_DAY, weekDaysAdapter.itemList.get(i).itemName);
                contentValues.put(RepeatOnDaysEntry.DAY_SELECTED, weekDaysAdapter.itemList.get(i).aBoolean);
                contentValues.put(RepeatOnDaysEntry.HABIT_ID, habitId);
                long daysRowId = sqLiteDatabase.insert(RepeatOnDaysEntry.TABLE_NAME, null, contentValues);
                Log.d("inserted days", "" + daysRowId);
            }


            return habitId;
        } catch (ParseException e) {
            Log.e("Error adding habit", Log.getStackTraceString(e));
        }
        mDbHelper.close();
        return -1;
    }

    public void addData(ArrayList<ItemAttributes> dailyHabitsArray){

        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WaterReminder), "ic_drink_water_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.YogaReminder), "ic_yoga_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.ExerciseReminder), "ic_exercise_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.FruitsReminder), "ic_fruits_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.ExpressGratitude), "ic_express_grattitude_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.LaughLaughAndLaugh), "ic_sentiment_laugh_satisfied_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Read), "ic_read_book_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WriteSomething), "ic_write_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WorkOnSecretProject), "ic_secret_agent_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Running), "ic_directions_run_black_24dp"));
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
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Networking), "ic_people_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CareLovedOnes), "ic_sentiment_laugh_satisfied_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Socialize), "ic_read_book_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.LearnCooking), "ic_fruits_black_24dp"));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.DeclutterPlace), "ic_wiping_swipe_clean_tidy"));
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


