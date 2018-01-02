package com.myapp.actfeeds.utilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.actfeeds.EditActivity;
import com.myapp.actfeeds.MainActivity;
import com.myapp.actfeeds.R;

import com.myapp.actfeeds.data.HabitContract;
import com.myapp.actfeeds.data.HabitDbHelper;


/**
 * Created by KavitaPC on 11/3/2017.
 */

public class AddedGoalsRecyclerViewAdapter extends RecyclerView.Adapter<AddedGoalsRecyclerViewAdapter.AddGoalsViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private HabitDbHelper mDbHelper;
    //put extra variables
    private String HABIT_PK= "UserHabitPK";
    private String TITLE_NAME= "TitleName";
    private String ICON_NAME= "IconName";
    private String PRIORITY= "Priority";
    private String TIME_VALUE= "TimeValue";
    private String DURATION_HOURS= "DurationHours";
    private String DURATION_MINS= "DurationMins";
    private String REPEAT_DAILY= "RepeatDaily";
    private String START_DATE ="StartDate";
    private String END_DATE="EndDate";


    public AddedGoalsRecyclerViewAdapter(Context mContext){
        this.mContext = mContext;

    }

    @Override
    public AddGoalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_goals_rows, parent, false);
        return new AddGoalsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final AddGoalsViewHolder holder, final int position) {
        if(position==0) {
            Log.d("AddGoal whole data is :", "aaaaaaaaaaaaaaaaaaaaaaa" + DatabaseUtils.dumpCursorToString(mCursor));
        }
        int idIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.USER_HABIT_PK);
        int nameIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.HABIT_NAME);
        int startDateIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.START_DATE);
        int endDateIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.END_DATE);
        final int reminderTime = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.REMINDER_TIME);
        final int durationHours = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.ACTIVITY_HOURS);
        final int durationMinutes = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.ACTIVITY_MINUTES);
        final int iconNameIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.ICON_NAME);
        final int repeatDailyIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.REPEAT_DAILY);
        final int priorityIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.PRIORITY);

        final int doneFlagIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.DONE_FLAG);
        final int dateCompletionIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.DATE_COMPLETION);
        final int habitStatusIDIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.HABIT_ID);
        final int habitStatusPKIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.HABIT_STATUS_PK);
        mCursor.moveToPosition(position);

       // Log.d("id index:", "aaaaaaaaaaaaaaaaaaaaaaa"+idIndex);

        final int id = mCursor.getInt(idIndex);
        final String name = mCursor.getString(nameIndex);
        final String iconName = mCursor.getString(iconNameIndex);
        final String startDate = mCursor.getString(startDateIndex);
        final String endDate = mCursor.getString(endDateIndex);
        final int repeatDaily = mCursor.getInt(repeatDailyIndex);
        final int priority = mCursor.getInt(priorityIndex);
        int doneFlagValue = mCursor.getInt(doneFlagIndex);
        String dateOFCompletion = mCursor.getString(dateCompletionIndex);
        int habitStatusID = mCursor.getInt(habitStatusIDIndex);
        final int habitStatusPK = mCursor.getInt(habitStatusPKIndex);

        final String reminderTimeStr = mCursor.getString(reminderTime);
        final int durationHoursValue = mCursor.getInt(durationHours);
        final int durationMinutesValue = mCursor.getInt(durationMinutes);

        String durationValue = "";
        if(durationMinutesValue==0){
            durationValue = durationMinutesValue+" Min";
        }else if(durationMinutesValue==1){
            durationValue = durationMinutesValue+" Min";
        }else if(durationMinutesValue>1){
            durationValue = durationMinutesValue+" Mins";
        }

        if(durationHoursValue==0){
            durationValue = durationValue+"";
        }else if(durationHoursValue==1){
            durationValue = durationHoursValue+" Hr,"+durationValue;
        }else if(durationHoursValue>1){
            durationValue = durationHoursValue+" Hrs,"+durationValue;
        }
        ////////////////////////////
        //set tag for swipe
            MyObject tagValue = new MyObject(id,habitStatusPK );
            holder.itemView.setTag(tagValue);
        //////////////////////////////

        holder.habitNameView.setText(name);
        holder.tvActivityTime.setText(reminderTimeStr);
        holder.getTvActivityDuration.setText(durationValue);

        holder.iconImageView.setImageResource(mContext.getResources().getIdentifier(
                iconName, "drawable", "com.myapp.actfeeds"));
        if(priority == 1){
            holder.iconImageView.setColorFilter(Color.parseColor("#ffcc0000"));
        }
        else if(priority == 2){
            holder.iconImageView.setColorFilter(Color.parseColor("#ffff8800"));
        }else if(priority == 3){
            holder.iconImageView.setColorFilter(Color.parseColor("#F1C40F"));
        }


        mDbHelper = new HabitDbHelper(mContext.getApplicationContext());
        final SQLiteDatabase sqldb = mDbHelper.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();


        //Open option for each item
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onClick(View v) {

            PopupMenu popup = new PopupMenu(mContext, holder.textViewOptions);
            popup.inflate(R.menu.edit_delete);

            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                       /* case R.id.edit:
                            //handle menu1 click
                            Intent intent = new Intent(mContext, EditActivity.class)
                                    .putExtra(HABIT_PK, id)
                                    .putExtra(TITLE_NAME, name)
                                    .putExtra(ICON_NAME, iconName)
                                    .putExtra(PRIORITY, priority)
                                    .putExtra(TIME_VALUE, reminderTimeStr)
                                    .putExtra(DURATION_HOURS, durationHoursValue)
                                    .putExtra(DURATION_MINS, durationMinutesValue)
                                    .putExtra(REPEAT_DAILY, repeatDaily);
                            mContext.startActivity(intent);
                            break;*/

                    case R.id.delete:
                            //handle menu2 click
                            sqldb.execSQL("DELETE FROM UserHabitDetail WHERE UserHabitPK = "+id+"");
                            sqldb.execSQL("DELETE FROM RepeatOnDays WHERE Habit_Id = "+id+"");
                            sqldb.execSQL("DELETE FROM HabitStatus WHERE Habit_Id = "+id+"");
                            sqldb.close();
                            mDbHelper.close();
                            Toast.makeText(mContext, "Activity deleted, check reports for detail",Toast.LENGTH_SHORT).show();

                            Intent intentRestart = new Intent(mContext, MainActivity.class);
                            mContext.startActivity(intentRestart);
                            notifyDataSetChanged();
                            break;
                    }

                    return false;
                }

            });
            //displaying the popup
            popup.show();
        }
    });



        //To open item starts edit activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditActivity.class)
                        .putExtra(HABIT_PK, id)
                        .putExtra(TITLE_NAME, name)
                        .putExtra(ICON_NAME,iconName)
                        .putExtra(PRIORITY, priority)
                        .putExtra(TIME_VALUE, reminderTimeStr)
                        .putExtra(DURATION_HOURS, durationHoursValue)
                        .putExtra(DURATION_MINS, durationMinutesValue)
                        .putExtra(START_DATE,startDate )
                        .putExtra(END_DATE, endDate)
                        .putExtra(REPEAT_DAILY, repeatDaily);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;

        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class AddGoalsViewHolder extends RecyclerView.ViewHolder{
        TextView habitNameView;
        ImageView iconImageView;
        TextView tvActivityTime;
        TextView getTvActivityDuration;
        TextView textViewOptions;

        public AddGoalsViewHolder(View itemView) {
            super(itemView);
            habitNameView = itemView.findViewById(R.id.name_text_view);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            tvActivityTime = itemView.findViewById(R.id.tvActivityTime);
            getTvActivityDuration = itemView.findViewById(R.id.tvActivityDuration);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);

        }
    }
}
