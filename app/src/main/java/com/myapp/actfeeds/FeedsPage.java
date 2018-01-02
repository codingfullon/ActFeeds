package com.myapp.actfeeds;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myapp.actfeeds.R;

import com.myapp.actfeeds.data.HabitDbHelper;
import com.myapp.actfeeds.utilities.ActivityCountRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class FeedsPage extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private OnFragmentInteractionListener mListener;
    RecyclerView activityCountRecyclerView;
    ActivityCountRecyclerViewAdapter activityCountAdapter;
    public static final int TASK_LOADER_ID = 2;
    private TextView textViewEmpty2;
    View view;
    boolean isDataLoaded = false,isVisibleToUser;
    private HabitDbHelper mDbHelper;
    private SQLiteDatabase sqldb;

    public FeedsPage() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_count, container, false);

        textViewEmpty2  =(TextView) view.findViewById(R.id.textViewEmpty2);

        activityCountRecyclerView = (RecyclerView)view.findViewById(R.id.rvActivityCount);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        activityCountRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

       // DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activityCountRecyclerView.getContext(),
               // linearLayoutManager.getOrientation());
       // activityCountRecyclerView.addItemDecoration(dividerItemDecoration);

        activityCountAdapter = new ActivityCountRecyclerViewAdapter(getActivity());
        activityCountRecyclerView.setAdapter(activityCountAdapter);

        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }



    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getContext()) {

            Cursor mActivityCountData = null;


            @Override
            protected void onStartLoading() {
                if (mActivityCountData != null) {
                    deliverResult(mActivityCountData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {

                     mDbHelper = new HabitDbHelper(getActivity().getBaseContext());
                     sqldb = mDbHelper.getReadableDatabase();

                    Calendar utcCalendar = Calendar.getInstance();
                   // utcCalendar.add(Calendar.DATE, -1);
                    utcCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    utcCalendar.set(Calendar.MINUTE, 0);
                    utcCalendar.set(Calendar.SECOND, 0);
                    utcCalendar.set(Calendar.MILLISECOND, 0);
                    Date startDate = utcCalendar.getTime();

                    final SimpleDateFormat DATE_FORMAT_STATUS = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    GregorianCalendar GCStatusDate = new GregorianCalendar();
                    GCStatusDate.setTime(new Date());


                    Cursor cursorReport = null;
                    String Query = " select a.DateOfCompletion, c.UserHabitPK, HabitName,IconName, a.DoneFlag "+
                            " from UserHabitDetail c inner join HabitStatus a "+
                            " on c.UserHabitPK = a.Habit_Id "+
                            " inner join RepeatOnDays d "+
                            " on c.UserHabitPK = d.Habit_Id "+
                            " and d.Day =(case cast (strftime('%w', a.DateOfCompletion) as integer) "+
                            " when 0 then 'Sun' "+
                            " when 1 then 'Mon' "+
                            " when 2 then 'Tue' "+
                            " when 3 then 'Wed' "+
                            " when 4 then 'Thu' "+
                            " when 5 then 'Fri' "+
                            " else 'Sat' end) "+
                            " and d.DaySelected = 1 "+
                            " and DateOfCompletion <= \"" +  DATE_FORMAT_STATUS.format(GCStatusDate.getTime())+"\""+
                            " AND StartDate <= " + startDate.getTime()+
                            " AND EndDate >= " + startDate.getTime() +
                            " order by a.DateOfCompletion desc";


                    cursorReport = sqldb.rawQuery(Query, null);




                    return cursorReport;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mActivityCountData = data;
                if(mActivityCountData==null || mActivityCountData.getCount()==0){
                    textViewEmpty2.setVisibility(view.VISIBLE);
                    activityCountRecyclerView.setVisibility(view.GONE);
                }else{
                    textViewEmpty2.setVisibility(view.GONE);
                    activityCountRecyclerView.setVisibility(view.VISIBLE);
                }
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        activityCountAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activityCountAdapter.swapCursor(null);
        mDbHelper.close();
        sqldb.close();
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
