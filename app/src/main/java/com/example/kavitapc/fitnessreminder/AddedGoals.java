package com.example.kavitapc.fitnessreminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kavitapc.fitnessreminder.data.HabitContract;
import com.example.kavitapc.fitnessreminder.data.HabitDbHelper;
import com.example.kavitapc.fitnessreminder.utilities.AddedGoalsRecyclerViewAdapter;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddedGoals extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH);
    private OnFragmentInteractionListener mListener;
    private TextView tvDate;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerViewAddedGoals;
    AddedGoalsRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    public static final int TASK_LOADER_ID = 1;

    public AddedGoals(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_added_goals, container, false);
        tvDate =(TextView)view.findViewById(R.id.tvSetText);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        tvDate.setText(date.toString().substring(0,10));

        recyclerViewAddedGoals =(RecyclerView)view.findViewById(R.id.rvAddedGoals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAddedGoals.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewAddedGoals.getContext(),
                linearLayoutManager.getOrientation());
        recyclerViewAddedGoals.addItemDecoration(dividerItemDecoration);

        mAdapter = new AddedGoalsRecyclerViewAdapter(getActivity());
        recyclerViewAddedGoals.setAdapter(mAdapter);



        floatingActionButton =(FloatingActionButton)view.findViewById(R.id.fabAddGoals);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GoalDetails.class);
                startActivity(intent);
            }
        });
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mHabitDetailData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mHabitDetailData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mHabitDetailData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                try {
                    HabitDbHelper mDbHelper = new HabitDbHelper(getActivity().getBaseContext());
                    SQLiteDatabase sqldb = mDbHelper.getWritableDatabase();


                    Calendar utcCalendar = Calendar.getInstance();
                    utcCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    utcCalendar.set(Calendar.MINUTE, 0);
                    utcCalendar.set(Calendar.SECOND, 0);
                    utcCalendar.set(Calendar.MILLISECOND, 0);
                    Date startDate = utcCalendar.getTime();


                    Cursor cursor = null;
                    String Query ="SELECT * FROM UserHabitDetail user INNER JOIN RepeatOnDays days ON user._id = days.Habit_Id WHERE " +
                            " days.Day = \""+ (new SimpleDateFormat("EEE")).format(new Date()) + "\"" +
                            " AND DaySelected = 1" +
                            " AND StartDate<=" + startDate.getTime()+
                            " AND EndDate>=" + startDate.getTime() +" ORDER BY Priority";

                    cursor = sqldb.rawQuery(Query, null);
                    return cursor;

                } catch (Exception e) {
                    Log.e("Failed", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mHabitDetailData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {mAdapter.swapCursor(null);    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
