package com.example.kavitapc.fitnessreminder.utilities;

import android.content.ContentValues;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kavitapc.fitnessreminder.R;
import com.example.kavitapc.fitnessreminder.WeekDaysAttributes;
import com.example.kavitapc.fitnessreminder.WeekDaysRecycleViewAdapter;
import com.example.kavitapc.fitnessreminder.data.HabitContract;
import com.example.kavitapc.fitnessreminder.data.HabitDbHelper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        int idIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.USER_HABIT_PK);
        int nameIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.HABIT_NAME);
        int startDateIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.START_DATE);
        int averageTimeIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.AVERAGE_TIME);
        int endDateIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.END_DATE);
        int iconNameIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.ICON_NAME);
        int repeatDailyIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.REPEAT_DAILY);
        int priorityIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.PRIORITY);

        int doneFlagIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.DONE_FLAG);
        int dateCompletionIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.DATE_COMPLETION);
        int habitStatusIDIndex = mCursor.getColumnIndex(HabitContract.HabitStatusEntry.HABIT_ID);
        mCursor.moveToPosition(position);

        Log.d("id index:", "aaaaaaaaaaaaaaaaaaaaaaa"+idIndex);

        final int id = mCursor.getInt(idIndex);
        String name = mCursor.getString(nameIndex);
        String iconName = mCursor.getString(iconNameIndex);
        int repeatDaily = mCursor.getInt(repeatDailyIndex);
        int priority = mCursor.getInt(priorityIndex);
        int doneFlagValue = mCursor.getInt(doneFlagIndex);
        String dateOFCompletion = mCursor.getString(dateCompletionIndex);
        int habitStatusID = mCursor.getInt(habitStatusIDIndex);

        String str = DatabaseUtils.dumpCursorToString(mCursor);
        Log.d("whole data is :", "aaaaaaaaaaaaaaaaaaaaaaa"+str);
        Log.d("id is", "aaaaaaaaaaaaaaaaaaaaaaa"+id);
        Log.d("position is", "aaaaaaaaaaaaaaaaaaaaaaa"+position);


        holder.habitNameView.setText(name);
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
        Calendar utcCalendar = Calendar.getInstance();
        utcCalendar.set(Calendar.HOUR_OF_DAY, 0);
        utcCalendar.set(Calendar.MINUTE, 0);
        utcCalendar.set(Calendar.SECOND, 0);
        utcCalendar.set(Calendar.MILLISECOND, 0);
        final Date currentDate = utcCalendar.getTime();

        String dateCurrentStr = Long.toString(currentDate.getTime());
        if(id == habitStatusID && doneFlagValue == 1 && dateOFCompletion.equals(dateCurrentStr)){
            holder.doneImageButton.setText("Completed");
            holder.doneImageButton.setEnabled(false);
            holder.doneImageButton.setTextColor(Color.GREEN);
        }


        //Done button onClick handling
        holder.doneImageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues = new ContentValues();
            contentValues.put(HabitContract.HabitStatusEntry.DATE_COMPLETION, currentDate.getTime());
            contentValues.put(HabitContract.HabitStatusEntry.DONE_FLAG, 1);
            contentValues.put(HabitContract.HabitStatusEntry.HABIT_ID, id);

            long habitId = sqLiteDatabase.insert(HabitContract.HabitStatusEntry.TABLE_NAME, null, contentValues );
            Log.d("updated ID is", ""+habitId);

            holder.doneImageButton.setText("Completed");
            holder.doneImageButton.setEnabled(false);
            holder.doneImageButton.setTextColor(Color.GREEN);
            Toast.makeText(mContext, "Activity completed, check reports for detail",Toast.LENGTH_LONG).show();

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
        TextView doneImageButton;

        public AddGoalsViewHolder(View itemView) {
            super(itemView);
            habitNameView = (TextView)itemView.findViewById(R.id.name_text_view);
            iconImageView =(ImageView)itemView.findViewById(R.id.iconImageView);
            doneImageButton =(TextView) itemView.findViewById(R.id.ibDone);


        }
    }
}
