package com.example.kavitapc.fitnessreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class WeeklyHabitsDetails extends AppCompatActivity {

    private ListView lvDailyHabits;
    private ListViewCustomAdapter customAdapter;
    ArrayList<ItemAttributes> dailyHabitsArray = new ArrayList<>();
    private static final String fileName = "WeeklyHabitsKeyValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwm_habits_details);
        lvDailyHabits = (ListView) findViewById(R.id.simpleListView);


        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Swimming), R.drawable.ic_swim_pool_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Laundry), R.drawable.ic_local_laundry_wash_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CheckBills), R.drawable.ic_playlist_add_check_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.MakeToDo), R.drawable.ic_write_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Networking), R.drawable.ic_people_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CareLovedOnes), R.drawable.ic_sentiment_laugh_satisfied_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Socialize), R.drawable.ic_read_book_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.LearnCooking), R.drawable.ic_fruits_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.DeclutterPlace), R.drawable.ic_wiping_swipe_clean_tidy));

        customAdapter = new ListViewCustomAdapter(this, R.layout.dwm_habits_rows, dailyHabitsArray, fileName);
        lvDailyHabits.setAdapter(customAdapter);
    }
}
