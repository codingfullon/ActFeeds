package com.example.kavitapc.fitnessreminder;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
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
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class AddedGoals extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH);
    private OnFragmentInteractionListener mListener;
    private TextView tvDate;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerViewAddedGoals;
    AddedGoalsRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    public static final int TASK_LOADER_ID = 1;
    private TextView textViewEmpty1;
    private View view;
    private TextView textViewGone;
    private  TourGuide  mTourHandler;
    private HabitDbHelper mDbHelper;
    private SQLiteDatabase sqldb;

    public AddedGoals(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_added_goals, container, false);
        //tvDate =(TextView)view.findViewById(R.id.tvSetText);
        //DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //Date date = new Date();
        //tvDate.setText(date.toString().substring(0,10));
        textViewEmpty1  =(TextView) view.findViewById(R.id.textViewEmpty1);
        recyclerViewAddedGoals =(RecyclerView)view.findViewById(R.id.rvAddedGoals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAddedGoals.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewAddedGoals.getContext(),
                linearLayoutManager.getOrientation());
        recyclerViewAddedGoals.addItemDecoration(dividerItemDecoration);

        mAdapter = new AddedGoalsRecyclerViewAdapter(getActivity());
        recyclerViewAddedGoals.setAdapter(mAdapter);



        //Delete item in recycler view when user swipes the item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                int id = (int) viewHolder.itemView.getTag(); //get id of the item to delete
                Log.d("id from get tag"," "+id);

                //delete data from db
                HabitDbHelper mDbHelper = new HabitDbHelper(getContext());
                SQLiteDatabase sqldb = mDbHelper.getWritableDatabase();

                sqldb.execSQL("DELETE FROM UserHabitDetail WHERE UserHabitPK = "+id+"");
                sqldb.execSQL("DELETE FROM RepeatOnDays WHERE Habit_Id = "+id+"");
                sqldb.execSQL("DELETE FROM HabitStatus WHERE Habit_Id = "+id+"");
                sqldb.close();
                mDbHelper.close();

                                // COMPLETED (3) Restart the loader to re-query for all tasks after a deletion
                //getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, AddedGoals.this);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, AddedGoals.this);
                recyclerViewAddedGoals.getAdapter().notifyDataSetChanged();

            }
        }).attachToRecyclerView(recyclerViewAddedGoals);

        //Open edit activity when user clicks on recycler view's item

        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        //adding tour guide
       /* textViewGone  =(TextView) view.findViewById(R.id.textView8);
        mTourHandler = TourGuide.init(getActivity()).with(TourGuide.Technique.Click)
                .setPointer(new Pointer().setGravity(Gravity.BOTTOM))
                .setToolTip( new ToolTip().setDescription("Tap to view details or edit/swipe right to delete")
                        .setBackgroundColor(Color.parseColor("#e54d26"))
                        .setShadow(true))
                .setOverlay(new Overlay()).playOn(((textViewGone)));


        textViewGone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTourHandler.cleanUp();
            }
        });*/


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
                     mDbHelper = new HabitDbHelper(getActivity().getBaseContext());
                     sqldb = mDbHelper.getReadableDatabase();


                    Calendar utcCalendar = Calendar.getInstance();
                    utcCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    utcCalendar.set(Calendar.MINUTE, 0);
                    utcCalendar.set(Calendar.SECOND, 0);
                    utcCalendar.set(Calendar.MILLISECOND, 0);
                    Date startDate = utcCalendar.getTime();
                    final SimpleDateFormat DATE_FORMAT_STATUS = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    GregorianCalendar GCStatusDate = new GregorianCalendar();
                    GCStatusDate.setTime(new Date());
                    Log.d("ssssssssssss",""+ DATE_FORMAT_STATUS.format(GCStatusDate.getTime()));

                    Cursor cursor = null;
                    String Query ="SELECT * FROM UserHabitDetail user INNER JOIN RepeatOnDays days ON user.UserHabitPK = days.Habit_Id " +
                            " AND days.Day = \""+ (new SimpleDateFormat("EEE")).format(new Date()) + "\"" +
                            " AND DaySelected = 1" +
                            " AND StartDate<=" + startDate.getTime()+
                            " AND EndDate>=" + startDate.getTime() +
                            " INNER JOIN HabitStatus ON user.UserHabitPK = HabitStatus.Habit_Id "+
                            " WHERE DateOfCompletion = \"" + DATE_FORMAT_STATUS.format(GCStatusDate.getTime())+"\""+
                            "  ORDER BY Priority";


                    cursor = sqldb.rawQuery(Query, null);
                    Log.d("ssssssssssss",""+(new SimpleDateFormat("EEE")).format(new Date()));

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
                if(mHabitDetailData==null || mHabitDetailData.getCount()==0){
                    textViewEmpty1.setVisibility(view.VISIBLE);
                    recyclerViewAddedGoals.setVisibility(view.GONE);
                }else{
                    textViewEmpty1.setVisibility(view.GONE);
                    recyclerViewAddedGoals.setVisibility(view.VISIBLE);
                }
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
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
    @Override
    protected void finalize() throws Throwable {
        mDbHelper.close();
        sqldb.close();
        super.finalize();
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
