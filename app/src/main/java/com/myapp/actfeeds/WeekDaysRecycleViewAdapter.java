package com.myapp.actfeeds;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myapp.actfeeds.R;

import java.util.ArrayList;

/**
 * Created by KavitaPC on 10/26/2017.
 */

public class WeekDaysRecycleViewAdapter extends RecyclerView.Adapter<WeekDaysRecycleViewAdapter.WeekDaysViewHolder> {

    private LayoutInflater layoutInflater;
    public ArrayList<WeekDaysAttributes> itemList = new ArrayList<>();
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
        if(flagWeekDays){
            holder.button.setBackgroundResource(R.drawable.sun);
        }else{
            holder.button.setBackgroundResource(R.drawable.circle);
        }


       // holder.button.setChecked(flagWeekDays);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

////////////////////////////////////////////////////////////////////////////

    public class WeekDaysViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView button;

        public WeekDaysViewHolder(View itemView) {
            super(itemView);
            button = (TextView) itemView.findViewById(R.id.tvWeekDays);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemList.get(getAdapterPosition()).getBoolean()){
                itemList.get(getAdapterPosition()).aBoolean = false;
                button.setBackgroundResource(R.drawable.circle);


            }
            else{
                itemList.get(getAdapterPosition()).aBoolean = true;
                //button.setSelected(true);
                //button.setBackgroundColor(Color.BLUE);
                button.setBackgroundResource(R.drawable.sun);


            }
        }
    }


}

