package com.example.myapp.actfeeds;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapp.actfeeds.R;

import com.example.myapp.actfeeds.data.HabitDbHelper;
import com.example.myapp.actfeeds.utilities.ActivityPercentageRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class HabitsReport extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private OnFragmentInteractionListener mListener;
    private RecyclerView activityPercentageRecyclerView;
    ActivityPercentageRecyclerViewAdapter activityPercentageAdapter;
    public static final int TASK_LOADER_ID = 3;
    private TextView textViewEmpty3;
    private View view;
    private HabitDbHelper mDbHelper;
    private SQLiteDatabase sqldb;

    public HabitsReport() {
        // Required empty public constructor
    }
   

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_habbits_report, container, false);
        textViewEmpty3  =(TextView) view.findViewById(R.id.textViewEmpty3);
        activityPercentageRecyclerView = (RecyclerView)view.findViewById(R.id.rvActivityPercentage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        activityPercentageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activityPercentageRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        activityPercentageRecyclerView.addItemDecoration(dividerItemDecoration);


        activityPercentageAdapter = new ActivityPercentageRecyclerViewAdapter(getActivity());
        activityPercentageRecyclerView.setAdapter(activityPercentageAdapter);
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
        //getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getContext()) {

            Cursor mActivityPercentageData = null;


            @Override
            protected void onStartLoading() {
                if (mActivityPercentageData != null) {
                    deliverResult(mActivityPercentageData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    Log.d("in on load", "sddddddddddddddddddddddd");
                    mDbHelper = new HabitDbHelper(getActivity().getBaseContext());
                     sqldb = mDbHelper.getReadableDatabase();

                    Calendar utcCalendar = Calendar.getInstance();
                    utcCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    utcCalendar.set(Calendar.MINUTE, 0);
                    utcCalendar.set(Calendar.SECOND, 0);
                    utcCalendar.set(Calendar.MILLISECOND, 0);
                    Date currDate = utcCalendar.getTime();

                    final SimpleDateFormat DATE_FORMAT_STATUS = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    GregorianCalendar GCStatusDate = new GregorianCalendar();
                    GCStatusDate.setTime(new Date());

                    Cursor cursorReport = null;

                    String Query =
                    " select A.Habit_Id as ID1, A.HabitName as Name1, A.IconName as Icon1, A.DoneFlag, B.Habit_Id  as ID2, B.HabitName as Name2, B.IconName as Icon2, B.DoneFlag, NotDoneCount, DoneCount from "+

                           " (select a.Habit_Id, HabitName,IconName,DoneFlag,DateOfCompletion,  count(*) as NotDoneCount "+
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
                                    " and a.DoneFlag = 0 "+
                                    " group by a.Habit_Id) as A left join "+

                       " (select a.Habit_Id, HabitName,IconName,DoneFlag,DateOfCompletion,  count(*) as DoneCount "+
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
                            " and a.DoneFlag = 1 "+
                            " group by a.Habit_Id) as B "+
                            " on A.Habit_Id=B.Habit_Id "+
                        " union "+

                        " select A.Habit_Id as ID1, A.HabitName as Name1, A.IconName as Icon1, A.DoneFlag, B.Habit_Id  as ID2, B.HabitName as Name2,B.IconName as Icon2, B.DoneFlag, A.NotDoneCount, DoneCount from "+
                             " (select a.Habit_Id, HabitName,IconName,DoneFlag,DateOfCompletion,  count(*) as DoneCount "+
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
                            "  and a.DoneFlag = 1 "+
                            "  group by a.Habit_Id) as B left join "+

                       " (select a.Habit_Id, HabitName,IconName,DoneFlag,DateOfCompletion,  count(*) as NotDoneCount "+
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
                            " and a.DoneFlag = 0 "+
                            " group by a.Habit_Id) as A "+
                            "  on A.Habit_Id=B.Habit_Id ";



                        cursorReport = sqldb.rawQuery(Query, null);

                   // Log.d("ssssssssssss",""+(new SimpleDateFormat("EEE")).format(new Date()));

                    /*TextView tvEmptyReport =(TextView) view.findViewById(R.id.tvEmptyReport);
                    if(cursor==null){
                        tvEmptyReport.setVisibility(View.VISIBLE);
                        activityCountRecyclerView.setVisibility(view.INVISIBLE);
                    }else{


                        tvEmptyReport.setVisibility(View.INVISIBLE);
                        activityCountRecyclerView.setVisibility(view.VISIBLE);
                    }*/

                    return cursorReport;

                } catch (Exception e) {
                    Log.e("Failed", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mActivityPercentageData = data;
                if(mActivityPercentageData==null || mActivityPercentageData.getCount()==0){
                    textViewEmpty3.setVisibility(view.VISIBLE);
                    activityPercentageRecyclerView.setVisibility(view.GONE);
                }else{
                    textViewEmpty3.setVisibility(view.GONE);
                    activityPercentageRecyclerView.setVisibility(view.VISIBLE);
                }
                super.deliverResult(data);
            }


        };



    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        activityPercentageAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activityPercentageAdapter.swapCursor(null);
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
