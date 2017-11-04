package com.example.kavitapc.fitnessreminder.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kavitapc.fitnessreminder.GoalDetails;
import com.example.kavitapc.fitnessreminder.R;

import java.util.ArrayList;

/**
 * Created by KavitaPC on 10/18/2017.
 */

public class ListViewCustomAdapter extends ArrayAdapter<ItemAttributes> {
    private final AppCompatActivity context;
    ArrayList<ItemAttributes> itemList = new ArrayList<>();
    String fileName;
    public static final String HABIT_TITLE = "habitTitle";

    public ListViewCustomAdapter(AppCompatActivity context, int resource, ArrayList<ItemAttributes> objects, String fileName) {
        super(context, resource, objects);
        this.context = context;
        itemList = objects;
        this.fileName = fileName;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.dwm_habits_rows, null);
        TextView textView = (TextView) v.findViewById(R.id.tvGoalRow);
        CheckBox checkBox =(CheckBox)v.findViewById(R.id.cbDailyHabitsRow);
        final String itemName = itemList.get(position).getItemName();
        textView.setText(itemName);
        textView.setCompoundDrawablesWithIntrinsicBounds(itemList.get(position).getIcon(),0,0,0);

        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        checkBox.setChecked(sharedPreferences.getBoolean(itemName,false));

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxClick(v);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(HABIT_TITLE,itemName);
                Intent intent = new Intent(context, GoalDetails.class);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });

        return v;
    }

    private void checkBoxClick(View view){
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbDailyHabitsRow);
        String text = checkBox.getText().toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(text, checkBox.isChecked());
        editor.apply();

        Toast.makeText(context.getApplicationContext(), "Selected item: " + text, Toast.LENGTH_SHORT).show();
    }
}
