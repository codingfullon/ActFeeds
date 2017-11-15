package com.example.kavitapc.fitnessreminder.utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.kavitapc.fitnessreminder.R;
import com.example.kavitapc.fitnessreminder.WeekDaysAttributes;
import com.example.kavitapc.fitnessreminder.WeekDaysRecycleViewAdapter;
import com.example.kavitapc.fitnessreminder.data.HabitContract;

import java.util.ArrayList;

/**
 * Created by KavitaPC on 11/3/2017.
 */

public class AddedGoalsRecyclerViewAdapter extends RecyclerView.Adapter<AddedGoalsRecyclerViewAdapter.AddGoalsViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public AddedGoalsRecyclerViewAdapter(Context mContext){
        this.mContext = mContext;

    }


    @Override
    public AddGoalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_goals_rows, parent, false);
        return new AddGoalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddGoalsViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry._ID);
        int nameIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.HABIT_NAME);
        int startDateIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.START_DATE);
        int averageTimeIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.AVERAGE_TIME);
        int endDateIndex = mCursor.getColumnIndex(HabitContract.UserHabitDetailEntry.END_DATE);
        mCursor.moveToPosition(position);

        Log.d("id index:", "aaaaaaaaaaaaaaaaaaaaaaa"+idIndex);

        final int id = mCursor.getInt(idIndex);
        String name = mCursor.getString(nameIndex);
        String averageTime = mCursor.getString(averageTimeIndex);

        Log.d("id is:", "aaaaaaaaaaaaaaaaaaaaaaa"+id);
        Log.d("id is:", "aaaaaaaaaaaaaaaaaaaaaaa"+name);
        Log.d("id is:", "aaaaaaaaaaaaaaaaaaaaaaa"+averageTime);
        String str = DatabaseUtils.dumpCursorToString(mCursor);
        Log.d("whole data is :", "aaaaaaaaaaaaaaaaaaaaaaa"+str);
        holder.habitNameView.setText(name);
       // R.drawable.ic_book_black_24dp;

String imagename = "ic_book_black_24dp";
        holder.iconImageView.setImageResource(mContext.getResources().getIdentifier(
                imagename, "drawable", "com.example.kavitapc.fitnessreminder"));
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

        public AddGoalsViewHolder(View itemView) {
            super(itemView);
            habitNameView = (TextView)itemView.findViewById(R.id.name_text_view);
            iconImageView =(ImageView)itemView.findViewById(R.id.iconImageView);
        }
    }
}
