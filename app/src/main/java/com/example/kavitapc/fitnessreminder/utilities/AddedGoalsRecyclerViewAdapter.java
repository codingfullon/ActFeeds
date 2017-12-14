package com.example.kavitapc.fitnessreminder.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kavitapc.fitnessreminder.AddedGoals;
import com.example.kavitapc.fitnessreminder.MainActivity;
import com.example.kavitapc.fitnessreminder.R;
import com.example.kavitapc.fitnessreminder.WeekDaysAttributes;
import com.example.kavitapc.fitnessreminder.WeekDaysRecycleViewAdapter;
import com.example.kavitapc.fitnessreminder.data.HabitContract;
import com.example.kavitapc.fitnessreminder.data.HabitDbHelper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by KavitaPC on 11/3/2017.
 */

public class AddedGoalsRecyclerViewAdapter extends RecyclerView.Adapter<AddedGoalsRecyclerViewAdapter.AddGoalsViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    HabitDbHelper mDbHelper;
    public AddedGoalsRecyclerViewAdapter(Context mContext){
        this.mContext = mContext;

    }


    @Override
    public AddGoalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_goals_rows, parent, false);
        return new AddGoalsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final AddGoalsViewHolder holder, final int position) {
       // Log.d("AddGoal whole data is :", "aaaaaaaaaaaaaaaaaaaaaaa"+DatabaseUtils.dumpCursorToString(mCursor));
        int idIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.USER_HABIT_PK);
        int nameIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.HABIT_NAME);
        int reminderTime = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.REMINDER_TIME);
        int durationHours = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.ACTIVITY_HOURS);
        int durationMinutes = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.ACTIVITY_MINUTES);
        int iconNameIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.ICON_NAME);
        int repeatDailyIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.REPEAT_DAILY);
        int priorityIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.PRIORITY);

        int doneFlagIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.DONE_FLAG);
        int dateCompletionIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.DATE_COMPLETION);
        int habitStatusIDIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.HABIT_ID);
        int habitStatusPKIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.HABIT_STATUS_PK);
        mCursor.moveToPosition(position);

       // Log.d("id index:", "aaaaaaaaaaaaaaaaaaaaaaa"+idIndex);

        final int id = mCursor.getInt(idIndex);
        String name = mCursor.getString(nameIndex);
        String iconName = mCursor.getString(iconNameIndex);
        int repeatDaily = mCursor.getInt(repeatDailyIndex);
        int priority = mCursor.getInt(priorityIndex);
        int doneFlagValue = mCursor.getInt(doneFlagIndex);
        String dateOFCompletion = mCursor.getString(dateCompletionIndex);
        int habitStatusID = mCursor.getInt(habitStatusIDIndex);
        final int habitStatusPK = mCursor.getInt(habitStatusPKIndex);

        String reminderTimeStr = mCursor.getString(reminderTime);
        int durationHoursValue = mCursor.getInt(durationHours);
        int durationMinutesValue = mCursor.getInt(durationMinutes);

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
        holder.itemView.setTag(id);
        //////////////////////////////

       // Log.d("whole data is :", "aaaaaaaaaaaaaaaaaaaaaaa"+DatabaseUtils.dumpCursorToString(mCursor));
        //Log.d("id is", "aaaaaaaaaaaaaaaaaaaaaaa"+id);
        //Log.d("position is", "aaaaaaaaaaaaaaaaaaaaaaa"+position);


        holder.habitNameView.setText(name);
        holder.tvActivityTime.setText(reminderTimeStr);
        holder.getTvActivityDuration.setText(durationValue);

        holder.iconImageView.setImageResource(mContext.getResources().getIdentifier(
                iconName, "drawable", "com.example.kavitapc.fitnessreminder"));
        if(priority == 1){
            holder.iconImageView.setColorFilter(mContext.getResources().getColor(R.color.materialRed, null));
        }
        else if(priority == 2){
            holder.iconImageView.setColorFilter(mContext.getResources().getColor(R.color.materialOrange, null));
        }else if(priority == 3){
            holder.iconImageView.setColorFilter(mContext.getResources().getColor(R.color.materialYellow, null));
        }

        //Changing the habit status
        mDbHelper = new HabitDbHelper(mContext.getApplicationContext());

        final SimpleDateFormat DATE_FORMAT_STATUS = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        GregorianCalendar GCalendar = new GregorianCalendar();
        GCalendar.setTime(new Date());
        String dateCurrentStr = DATE_FORMAT_STATUS.format(GCalendar.getTime());

        if(id == habitStatusID && doneFlagValue == 1 && dateOFCompletion.equals(dateCurrentStr)){
            holder.doneCheckBox.setChecked(true);
            holder.doneCheckBox.setEnabled(false);
           // holder.doneCheckBox.setButtonDrawable(Color.GREEN);
        }
        final SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
       final ContentValues contentValues = new ContentValues();


        //Done button onClick handling
        holder.doneCheckBox.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(HabitContract.HabitStatusEntry.DONE_FLAG, 1);

            String arg = " Habit_Id="+ id + " AND HabitStatusPK=" +habitStatusPK+"";
            long habitId = sqLiteDatabase.update(HabitContract.HabitStatusEntry.TABLE_NAME, contentValues,arg,null);
            //long habitId = sqLiteDatabase.insert(HabitContract.HabitStatusEntry.TABLE_NAME, null, contentValues );
            Log.d("updated ID is", ""+habitId);


            holder.doneCheckBox.setEnabled(false);
            notifyDataSetChanged();

           // holder.doneCheckBox.setBackgroundColor(Color.GREEN);
            Toast.makeText(mContext, "Activity completed, check reports for detail",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            notifyDataSetChanged();

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
        CheckBox doneCheckBox;
        ImageView groupImageView;
        TextView tvActivityTime;
        TextView getTvActivityDuration;

        public AddGoalsViewHolder(View itemView) {
            super(itemView);
            habitNameView = (TextView)itemView.findViewById(R.id.name_text_view);
            iconImageView =(ImageView)itemView.findViewById(R.id.iconImageView);
            doneCheckBox =(CheckBox) itemView.findViewById(R.id.cbDone);
            groupImageView =(ImageView)itemView.findViewById(R.id.ivGroup);
            tvActivityTime = (TextView)itemView.findViewById(R.id.tvActivityTime);
            getTvActivityDuration = (TextView)itemView.findViewById(R.id.tvActivityDuration);


        }
    }
}
