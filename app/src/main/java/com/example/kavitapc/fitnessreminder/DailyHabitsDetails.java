package com.example.kavitapc.fitnessreminder;
//Shows daily habits list to add

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DailyHabitsDetails extends AppCompatActivity {

   private ListView lvDailyHabits;
   private ListViewCustomAdapter customAdapter;
   ArrayList<ItemAttributes> dailyHabitsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_habits_details);
        lvDailyHabits = (ListView) findViewById(R.id.simpleListView);

        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WaterReminder), R.drawable.ic_drink_water_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.YogaReminder), R.drawable.ic_yoga_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.ExerciseReminder), R.drawable.ic_exercise_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.FruitsReminder), R.drawable.ic_fruits_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.ExpressGratitude), R.drawable.ic_express_grattitude_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.LaughLaughAndLaugh), R.drawable.ic_sentiment_laugh_satisfied_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Read), R.drawable.ic_read_book_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WriteSomething), R.drawable.ic_write_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WorkOnSecretProject), R.drawable.ic_secret_agent_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Running), R.drawable.ic_directions_run_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Meditate), R.drawable.ic_meditation_yoga_posture));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.LearnNewWords), R.drawable.ic_book_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.EatBowlSalad), R.drawable.ic_fruits_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Study), R.drawable.ic_read_book_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CleanTidy), R.drawable.ic_wiping_swipe_clean_tidy));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CheckToDo), R.drawable.ic_book_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Breakfast), R.drawable.ic_fruits_black_24dp));


        customAdapter = new ListViewCustomAdapter(this, R.layout.daily_habits_rows, dailyHabitsArray);
        lvDailyHabits.setAdapter(customAdapter);

        lvDailyHabits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View view2 = customAdapter.getView(position, view, parent);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbDailyHabitsRow);
                checkBox.setChecked(true);
                checkBox.setFocusable(true);
                checkBox.setFocusableInTouchMode(true);
                String text = checkBox.getText().toString();
                Toast.makeText(getApplicationContext(), "Selected item: " + text + " - " + position, Toast.LENGTH_SHORT).show();
                Log.i(text, "clicked");

            }
        });

    }

    protected void createPreferences(String key, Boolean value){

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();

    }


}
