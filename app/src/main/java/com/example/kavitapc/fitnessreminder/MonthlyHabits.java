package com.example.kavitapc.fitnessreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MonthlyHabits extends AppCompatActivity {

    private ListView lvDailyHabits;
    private ListViewCustomAdapter customAdapter;
    ArrayList<ItemAttributes> dailyHabitsArray = new ArrayList<>();
    private static final String fileName = "WeeklyHabitsKeyValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwm_habits_details);
        lvDailyHabits = (ListView) findViewById(R.id.simpleListView);


        dailyHabitsArray.add(new ItemAttributes(getString(R.string.PlanInvestments), R.drawable.ic_attach_money_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CheckStatement), R.drawable.ic_read_book_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.PlanTravel), R.drawable.ic_travel_car_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.RearrangeSpace), R.drawable.ic_wiping_swipe_clean_tidy));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.PlanOccasions), R.drawable.ic_write_black_24dp));


        customAdapter = new ListViewCustomAdapter(this, R.layout.dwm_habits_rows, dailyHabitsArray, fileName);
        lvDailyHabits.setAdapter(customAdapter);
    }
}
