package com.example.kavitapc.fitnessreminder.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
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
        final String itemName = itemList.get(position).getItemName();
        textView.setText(itemName);
        final int icon = itemList.get(position).getIcon();
        ImageView iconView = (ImageView) v.findViewById(R.id.tvIcon) ;
        iconView.setImageResource(icon);

        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.dwm_habits_rows, null);
        TextView textView = (TextView) v.findViewById(R.id.tvGoalRow);
        final String itemName = itemList.get(position).getItemName();
        textView.setText(itemName);
        final int icon = itemList.get(position).getIcon();
        ImageView iconView = (ImageView) v.findViewById(R.id.tvIcon) ;
        iconView.setImageResource(icon);

        //iconView.setCompoundDrawablesWithIntrinsicBounds(icon,0,icon,0);
        return v;
    }
}
