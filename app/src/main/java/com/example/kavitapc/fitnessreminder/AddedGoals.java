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
import android.widget.Toast;

import com.example.kavitapc.fitnessreminder.data.HabitContract;
import com.example.kavitapc.fitnessreminder.data.HabitDbHelper;
import com.example.kavitapc.fitnessreminder.utilities.AddedGoalsRecyclerViewAdapter;
import com.example.kavitapc.fitnessreminder.utilities.MyObject;

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
    private TextView textViewCompleted;
    private View view;
    private TextView textViewGone;
    private  TourGuide  mTourHandler;
    private HabitDbHelper mDbHelper;
    private SQLiteDatabase sqldb;
    private Calendar utcCalendar;
    private Date startDate;
    final SimpleDateFormat DATE_FORMAT_STATUS = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private GregorianCalendar GCStatusDate;
    public AddedGoals(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_added_goals, container, false);
        //date variables
        utcCalendar = Calendar.getInstance();
        utcCalendar.set(Calendar.HOUR_OF_DAY, 0);
        utcCalendar.set(Calendar.MINUTE, 0);
        utcCalendar.set(Calendar.SECOND, 0);
        utcCalendar.set(Calendar.MILLISECOND, 0);
         startDate = utcCalendar.getTime();
         GCStatusDate = new GregorianCalendar();
        GCStatusDate.setTime(new Date());


        textViewEmpty1  = view.findViewById(R.id.textViewEmpty1);
        textViewCompleted  = view.findViewById(R.id.textViewCompleted);

        recyclerViewAddedGoals = view.findViewById(R.id.rvAddedGoals);
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

                MyObject tagValue = (MyObject) viewHolder.itemView.getTag(); //get id of the item to complete
               int id = tagValue.getIdUserHabit();
               int habitStatusPK = tagValue.getIdHabitStatus();
                Log.d("id from get tag"," "+tagValue.getIdUserHabit());
                Log.d("habit id from get tag"," "+tagValue.getIdHabitStatus());

                //delete data from db
                 mDbHelper = new HabitDbHelper(getContext());
                 sqldb = mDbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(HabitContract.HabitStatusEntry.DONE_FLAG, 1);

                String arg = " Habit_Id="+ id + " AND HabitStatusPK=" +habitStatusPK+"";
                long habitId = sqldb.update(HabitContract.HabitStatusEntry.TABLE_NAME, contentValues,arg,null);
                Log.d("updated ID is", ""+habitId);
                Toast.makeText(getActivity(), "Activity completed, check reports for detail",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, AddedGoals.this);
                recyclerViewAddedGoals.getAdapter().notifyDataSetChanged();


            }
        }).attachToRecyclerView(recyclerViewAddedGoals);

        //Open edit activity when user clicks on recycler view's item

        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        //adding tour guide
       /* textViewGone  =(TextView) view.findViewById(R.id.textViewTour);
        mTourHandler = TourGuide.init(getActivity()).with(TourGuide.Technique.Click)
                .setPointer(new Pointer().setGravity(Gravity.BOTTOM))
                .setToolTip( new ToolTip().setDescription("swipe right to complete. Tap to view details or edit")
                        .setBackgroundColor(Color.parseColor("#e54d26"))
                        .setShadow(true))
                .setOverlay(new Overlay()).playOn(((textViewGone)));


        textViewGone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTourHandler.cleanUp();
                textViewGone.setVisibility(View.GONE);
            }
        });*/


        return view;
    }

    public Cursor allCompleted(){
        return sqldb.rawQuery(
                " SELECT * FROM UserHabitDetail user INNER JOIN RepeatOnDays days ON user.UserHabitPK = days.Habit_Id " +
                " AND days.Day = \""+ (new SimpleDateFormat("EEE")).format(new Date()) + "\"" +
                        " AND DaySelected = 1" +
                        " AND StartDate<=" + startDate.getTime()+
                        " AND EndDate>=" + startDate.getTime() +
                        " INNER JOIN HabitStatus ON user.UserHabitPK = HabitStatus.Habit_Id "+
                        " WHERE DateOfCompletion = \"" + DATE_FORMAT_STATUS.format(GCStatusDate.getTime())+"\""+""
                       ,
                null);
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



                   // Log.d("ssssssssssss",""+ DATE_FORMAT_STATUS.format(GCStatusDate.getTime()));

                    Cursor cursor = null;
                    String Query ="SELECT * FROM UserHabitDetail user INNER JOIN RepeatOnDays days ON user.UserHabitPK = days.Habit_Id " +
                            " AND days.Day = \""+ (new SimpleDateFormat("EEE")).format(new Date()) + "\"" +
                            " AND DaySelected = 1" +
                            " AND StartDate<=" + startDate.getTime()+
                            " AND EndDate>=" + startDate.getTime() +
                            " INNER JOIN HabitStatus ON user.UserHabitPK = HabitStatus.Habit_Id "+
                            " WHERE DateOfCompletion = \"" + DATE_FORMAT_STATUS.format(GCStatusDate.getTime())+"\""+
                            " AND DoneFlag =0 "+
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
                Cursor cursor = allCompleted();

                if(mHabitDetailData!=null && mHabitDetailData.getCount()>0){
                    recyclerViewAddedGoals.setVisibility(View.VISIBLE);
                    textViewCompleted.setVisibility(View.GONE);
                    textViewEmpty1.setVisibility(View.GONE);
                }else if(cursor==null || cursor.getCount()==0){
                    recyclerViewAddedGoals.setVisibility(View.GONE);
                    textViewEmpty1.setVisibility(View.VISIBLE);
                    textViewCompleted.setVisibility(View.GONE);
                }else{
                    recyclerViewAddedGoals.setVisibility(View.GONE);
                    textViewEmpty1.setVisibility(View.GONE);
                    textViewCompleted.setVisibility(View.VISIBLE);
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
