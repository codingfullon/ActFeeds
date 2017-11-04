package com.example.kavitapc.fitnessreminder;
//Shows daily habits list to add

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.kavitapc.fitnessreminder.utilities.ItemAttributes;
import com.example.kavitapc.fitnessreminder.utilities.ListViewCustomAdapter;

import java.util.ArrayList;

public class AddGoals extends AppCompatActivity {

   private ListView lvDailyHabits;
   private ListViewCustomAdapter customAdapter;
   ArrayList<ItemAttributes> dailyHabitsArray = new ArrayList<>();
    private static final String fileName = "DailyHabitsKeyValue";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwm_habits_details);
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
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Swimming), R.drawable.ic_swim_pool_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Laundry), R.drawable.ic_local_laundry_wash_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CheckBills), R.drawable.ic_playlist_add_check_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.MakeToDo), R.drawable.ic_write_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Networking), R.drawable.ic_people_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CareLovedOnes), R.drawable.ic_sentiment_laugh_satisfied_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.Socialize), R.drawable.ic_read_book_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.LearnCooking), R.drawable.ic_fruits_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.DeclutterPlace), R.drawable.ic_wiping_swipe_clean_tidy));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.PlanInvestments), R.drawable.ic_attach_money_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.CheckStatement), R.drawable.ic_read_book_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.PlanTravel), R.drawable.ic_travel_car_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.RearrangeSpace), R.drawable.ic_wiping_swipe_clean_tidy));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.PlanOccasions), R.drawable.ic_write_black_24dp));
        dailyHabitsArray.add(new ItemAttributes(getString(R.string.WakeUpEarly), R.drawable.ic_wb_sunny_black_24dp));

        customAdapter = new ListViewCustomAdapter(this, R.layout.dwm_habits_rows, dailyHabitsArray, fileName);
        lvDailyHabits.setAdapter(customAdapter);
    }
}
