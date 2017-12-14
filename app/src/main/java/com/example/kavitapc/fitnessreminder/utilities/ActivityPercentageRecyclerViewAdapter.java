package com.example.kavitapc.fitnessreminder.utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kavitapc.fitnessreminder.R;
import com.example.kavitapc.fitnessreminder.data.HabitContract;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.components.Legend.LegendPosition.BELOW_CHART_LEFT;

/**
 * Created by KavitaPC on 11/30/2017.
 */

public class ActivityPercentageRecyclerViewAdapter extends RecyclerView.Adapter<ActivityPercentageRecyclerViewAdapter.ActivityPercentageViewHolder>{

    Cursor mCursor1;
    Context mContext;

    public ActivityPercentageRecyclerViewAdapter(Context context){
        mContext = context;
    }

    @Override
    public ActivityPercentageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_percentage_rows, parent, false);
        return new ActivityPercentageRecyclerViewAdapter.ActivityPercentageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityPercentageViewHolder holder, int position) {
        Log.d("percentage data is :", "aaaaaaaaaaaaaaaaaaaaaaa"+DatabaseUtils.dumpCursorToString(mCursor1));
        mCursor1.moveToPosition(position);
        //get column names

        String columnNameID1 = mCursor1.getColumnName(0);
        String columnNameName1 = mCursor1.getColumnName(1);
        String columnNameIcon1 = mCursor1.getColumnName(2);
        String columnNameID2 = mCursor1.getColumnName(4);
        String columnNameName2 = mCursor1.getColumnName(5);
        String columnNameIcon2 = mCursor1.getColumnName(6);
        String columnNameNotDoneCount = mCursor1.getColumnName(8);
        String columnNameDoneCount = mCursor1.getColumnName(9);

        int id1Index = mCursor1.getColumnIndex(columnNameID1);
        int name1Index = mCursor1.getColumnIndex(columnNameName1);
        int icon1Index = mCursor1.getColumnIndex(columnNameIcon1);
        int id2Index = mCursor1.getColumnIndex(columnNameID2);
        int name2Index = mCursor1.getColumnIndex(columnNameName2);
        int icon2Index = mCursor1.getColumnIndex(columnNameIcon2);
        int NotDoneCountIndex = mCursor1.getColumnIndex(columnNameNotDoneCount);
        int DoneCountIndex = mCursor1.getColumnIndex(columnNameDoneCount);

        //get column values
        final int id1 = mCursor1.getInt(id1Index);
        String name1 = mCursor1.getString(name1Index);
        String icon1 = mCursor1.getString(icon1Index);
        final int id2 = mCursor1.getInt(id2Index);
        String name2 = mCursor1.getString(name2Index);
        String icon2 = mCursor1.getString(icon2Index);
        int notDoneCount = mCursor1.getInt(NotDoneCountIndex);
        int doneCount = mCursor1.getInt(DoneCountIndex);

        //set values on screen
        String activityName="";
        String iconName="";
        if(name1==null){
            activityName=name2;
            iconName=icon2;
        }else{
            activityName=name1;
            iconName=icon1;
        }
        Log.d("icon is","iiiiiiiiiii"+iconName);

        holder.ivIcon.setImageResource(mContext.getResources().getIdentifier(
                iconName, "drawable", "com.example.kavitapc.fitnessreminder"));
        holder.tvProgress.setText(activityName);
       // holder.tvCountNotDone.setText("Not Completed"+notDoneCount);
        //holder.tvCountDone.setText("Completed"+doneCount);
        holder.progressBar.setScaleY(2f);
    int cnt=0;
    int total = (doneCount+notDoneCount);
    if(total!=0){
        double CntFinal =  (double)doneCount/(double)total*100;
        cnt = (int)CntFinal;
    }
        String dayValue = "";
        if(total>1){
            dayValue = "Days";
        }else {
            dayValue ="Day";
        }
    holder.textViewCount.setText(doneCount+"/"+total+" "+dayValue);
    holder.progressBar.setProgress(cnt);


    }

    @Override
    public int getItemCount() {
        if(mCursor1 == null){
            return 0;

        }
        return mCursor1.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor1 == c) {
            return null; // bc nothing has changed
        }

        Cursor temp = mCursor1;
        this.mCursor1 = c; // new cursor value assigned
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class ActivityPercentageViewHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        TextView tvProgress;
        TextView textViewCount;
       // TextView tvCountDone;
        ProgressBar progressBar;
        public ActivityPercentageViewHolder(View view){
            super(view);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvProgress = (TextView)itemView.findViewById(R.id.tvProgress);
            textViewCount = (TextView)itemView.findViewById(R.id.textViewCount);
           // tvCountDone = (TextView)itemView.findViewById(R.id.tvCountDone);

            progressBar =(ProgressBar)itemView.findViewById(R.id.progressBar2);
        }

    }
}
