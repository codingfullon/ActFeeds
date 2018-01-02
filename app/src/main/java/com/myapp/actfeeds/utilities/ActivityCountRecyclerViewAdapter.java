package com.myapp.actfeeds.utilities;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.actfeeds.R;
import com.myapp.actfeeds.data.HabitContract;
import com.myapp.actfeeds.data.HabitDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created by KavitaPC on 11/22/2017.
 * Adapter to show daily data
 */

public class ActivityCountRecyclerViewAdapter extends RecyclerView.Adapter<ActivityCountRecyclerViewAdapter.ActivityCountViewHolder>{
    private Cursor mCursor2;
    private Context mContext;
    HabitDbHelper mDbHelper1;
    private String completedActivities ="";
    private String notCompletedActivities ="";
    private int countComp =0;
    private int countNotComp =0;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH);


    public ActivityCountRecyclerViewAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public ActivityCountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_count_rows, parent, false);
        return new ActivityCountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityCountViewHolder holder, int position) {

       // Log.d("AC whole data is:", ""+DatabaseUtils.dumpCursorToString(mCursor2));

        mCursor2.moveToPosition(position);
       // holder.itemView.setVisibility(View.VISIBLE);

        //get column Index
        int dateCompIndex = mCursor2.getColumnIndex(HabitContract.HabitStatusEntry.DATE_COMPLETION);
        int idPKIndex = mCursor2.getColumnIndex(HabitContract.UserHabitDetailEntry.USER_HABIT_PK);
        int nameIndex = mCursor2.getColumnIndex(HabitContract.UserHabitDetailEntry.HABIT_NAME);
        int iconIndex = mCursor2.getColumnIndex(HabitContract.UserHabitDetailEntry.ICON_NAME);
        int doneFlagIndex = mCursor2.getColumnIndex(HabitContract.HabitStatusEntry.DONE_FLAG);

        //get column values
        String dateValue = mCursor2.getString(dateCompIndex);
        int idPKValue = mCursor2.getInt(idPKIndex);
       // String name = mCursor2.getString(nameIndex);
        String icon = mCursor2.getString(iconIndex);
        int doneFlag = mCursor2.getInt(doneFlagIndex);



        if(doneFlag==1){
            completedActivities = completedActivities+ ", "+mCursor2.getString(nameIndex);
            countComp++;
        }else{
            notCompletedActivities=notCompletedActivities+", "+mCursor2.getString(nameIndex);
            countNotComp++;
        }

        //checking if currDate is equal to next date value
        String dateValue2="";
        if(!mCursor2.isLast()) {
            mCursor2.moveToPosition(position + 1);
             dateValue2 = mCursor2.getString(dateCompIndex);
        //    if(mCursor2.isLast()){
                mCursor2.moveToPosition(position-1);
          //  }
        }


            if( mCursor2.isLast() || !dateValue.equals(dateValue2)  ){
                    Log.d("position after",""+mCursor2.getPosition());
                holder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT));
                    //set data on screen
                    String countAll = countComp+"/"+(countComp+countNotComp)+" Completed";

                                final SimpleDateFormat DATE_FORMAT_STATUS = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                GregorianCalendar GCStatusDate = new GregorianCalendar();
                                GCStatusDate.setTime(new Date());
                                GregorianCalendar GCStatusDate2 = new GregorianCalendar();
                                GCStatusDate2.add(Calendar.DATE,-1);

                                        if(dateValue.equals(DATE_FORMAT_STATUS.format(GCStatusDate.getTime()))){
                                            holder.tvReportDate.setText(R.string.today);
                                        }
                                        else if(dateValue.equals(DATE_FORMAT_STATUS.format(GCStatusDate2.getTime()))){
                                            holder.tvReportDate.setText(R.string.yesterday);
                                        }else{
                                            holder.tvReportDate.setText(dateValue);
                                        }

                            holder.tvCountCompleted.setText(countAll);
                                    if(countComp==0){
                                        holder.tvCompleted.setText(R.string.NoActivityCompleted);
                                    }else{
                                       String compStr = completedActivities;
                                       if(compStr.startsWith(",")){
                                           compStr =compStr.substring(1, compStr.length());
                                       }
                                        holder.tvCompleted.setText(compStr);
                                    }
                                    if(countNotComp==0){
                                        holder.tvNotCompleted.setText(R.string.NoActivityIncomplete);
                                    }else{
                                        String NotCompStr = notCompletedActivities;
                                        if(NotCompStr.startsWith(",")){
                                            NotCompStr =NotCompStr.substring(1, NotCompStr.length());
                                        }
                                        holder.tvNotCompleted.setText(NotCompStr);
                                    }

                            notCompletedActivities = "";
                            countNotComp = 0;
                            completedActivities = "";
                            countComp = 0;



            }else{

                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }

    }


    @Override
    public int getItemCount() {
        if(mCursor2 == null){
            return 0;

        }
        return mCursor2.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor2)
        if (mCursor2 == c) {
            return null; //  nothing has changed
        }
        Cursor temp = mCursor2;
        this.mCursor2 = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class ActivityCountViewHolder extends RecyclerView.ViewHolder{
        CardView cvCountPage;
        TextView tvReportDate;
        TextView tvCountCompleted;
        ImageView ivCompleted;
        TextView tvCompleted;
        ImageView ivNotCompleted;
        TextView tvNotCompleted;


        public ActivityCountViewHolder(View view){
            super(view);
            cvCountPage = itemView.findViewById(R.id.cvCountPage);
            tvReportDate = itemView.findViewById(R.id.tvReportDate);
            tvCountCompleted = itemView.findViewById(R.id.tvCountCompleted);
            //ivCompleted =(ImageView)itemView.findViewById(R.id.ivCompleted);
            tvCompleted = itemView.findViewById(R.id.tvCompleted);
            //ivNotCompleted =(ImageView)itemView.findViewById(R.id.ivNotCompleted);
            tvNotCompleted = itemView.findViewById(R.id.tvNotCompleted);

        }


    }

}
