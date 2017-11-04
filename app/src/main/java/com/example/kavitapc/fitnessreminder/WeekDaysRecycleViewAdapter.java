package com.example.kavitapc.fitnessreminder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by KavitaPC on 10/26/2017.
 */

public class WeekDaysRecycleViewAdapter extends RecyclerView.Adapter<WeekDaysRecycleViewAdapter.WeekDaysViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<WeekDaysAttributes> itemList = new ArrayList<>();
    ListItemClickListener mClickListener;
    boolean flagWeekDays;

    public interface ListItemClickListener {
        void onListItemClick(View view, int position);
    }
    public WeekDaysRecycleViewAdapter(AppCompatActivity context, ArrayList<WeekDaysAttributes> objects) {
        this.layoutInflater = LayoutInflater.from(context);
        this.itemList = objects;
    }

    @Override
    public WeekDaysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.week_days_items, parent, false);
        WeekDaysViewHolder weekDaysViewHolder = new WeekDaysViewHolder(view);
        return weekDaysViewHolder;

    }

    @Override
    public void onBindViewHolder(WeekDaysViewHolder holder, int position) {
        String itemName = itemList.get(position).getItemName();
        flagWeekDays = itemList.get(position).getBoolean();
        holder.button.setText(itemName);
        holder.button.setBackgroundColor(Color.BLUE);
        holder.button.setSelected(flagWeekDays);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

////////////////////////////////////////////////////////////////////////////

    public class WeekDaysViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Switch button;

        public WeekDaysViewHolder(View itemView) {
            super(itemView);
            button = (Switch) itemView.findViewById(R.id.bWeekDays);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemList.get(getAdapterPosition()).getBoolean()){
                itemList.get(getAdapterPosition()).aBoolean = false;
                Log.d("enabled 0", "true"+button.getText());

                button.setBackgroundColor(Color.WHITE);

            }
            else{
                itemList.get(getAdapterPosition()).aBoolean = true;
                Log.d("enabled 0", "false");
                button.setSelected(true);
                button.setBackgroundColor(Color.BLUE);

            }
        }
    }


}
