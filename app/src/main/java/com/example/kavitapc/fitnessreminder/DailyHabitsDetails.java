package com.example.kavitapc.fitnessreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DailyHabitsDetails extends AppCompatActivity {

    private CheckBox cbWaterReminder;
    private CheckBox cbExerciseReminder;
    private CheckBox cbFruitsReminder;
    private CheckBox cbYogaReminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_habits_details);

        cbWaterReminder = (CheckBox) findViewById(R.id.cbWaterReminder);
        cbExerciseReminder = (CheckBox) findViewById(R.id.cbExerciseReminder);
        cbFruitsReminder = (CheckBox) findViewById(R.id.cbFruitsReminder);
        cbYogaReminder = (CheckBox) findViewById(R.id.cbYogaReminder);

        CheckBox[] checkBoxes = {cbWaterReminder, cbExerciseReminder, cbFruitsReminder, cbYogaReminder};

        for (CheckBox cb : checkBoxes) {
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((cbWaterReminder).isChecked()) {
                        Log.i("checkbox 1", "clicked");
                        createPreferences("cbWaterReminder", true);
                    }
                    if ((cbExerciseReminder).isChecked()) {
                        Log.i("checkbox 2", "clicked");
                        createPreferences("cbExerciseReminder", true);
                    }
                    if ((cbFruitsReminder).isChecked()) {
                        Log.i("checkbox 3", "clicked");
                        createPreferences("cbFruitsReminder", true);
                    }
                    if ((cbYogaReminder).isChecked()) {
                        Log.i("checkbox 4", "clicked");
                        createPreferences("cbYogaReminder", true);
                    }
                }
            });
        }



    }

    protected void createPreferences(String key, Boolean value){

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();

    }

}
