package com.example.kavitapc.fitnessreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                    }
                    if ((cbExerciseReminder).isChecked()) {
                        Log.i("checkbox 2", "clicked");
                    }
                    if ((cbFruitsReminder).isChecked()) {
                        Log.i("checkbox 3", "clicked");
                    }
                    if ((cbYogaReminder).isChecked()) {
                        Log.i("checkbox 4", "clicked");
                    }
                }
            });
        }
    }
}
