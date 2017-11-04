package com.example.kavitapc.fitnessreminder;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.kavitapc.fitnessreminder.data.HabitContract;
import com.example.kavitapc.fitnessreminder.data.HabitDbHelper;
import com.example.kavitapc.fitnessreminder.utilities.DatePickerFragment;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);


        //Setting goal title from selected goal
        Bundle b = getIntent().getExtras();
        String title = b.getString("habitTitle");
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

//////////////Accessing contacts content provider
    public class AsyncTaskContacts extends AsyncTask<Void, Void, Cursor> {


        @Override
        protected Cursor doInBackground(Void... params) {

            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            return null;
        }

        public void saveData() {
            HabitDbHelper mDbHelper = new HabitDbHelper(getBaseContext());
            SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(UserHabitDetailEntry.HABIT_NAME, "Read");
            contentValues.put(UserHabitDetailEntry.START_DATE, "Read");
            contentValues.put(UserHabitDetailEntry.END_DATE, "Read");
            contentValues.put(UserHabitDetailEntry.AVERAGE_TIME, "Read");
            contentValues.put(UserHabitDetailEntry.REPEAT_DAILY, "Read");
            contentValues.put(UserHabitDetailEntry.HABIT_PRIVATE, "Read");
            contentValues.put(UserHabitDetailEntry.DESCRIPTION, "Read");
            contentValues.put(UserHabitDetailEntry.HABIT_NAME, "Read");

            long newRowId = sqLiteDatabase.insert(UserHabitDetailEntry.TABLE_NAME, null, contentValues);
        }

    }

}

