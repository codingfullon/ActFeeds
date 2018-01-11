package com.inkeep.actfeeds;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;


import com.inkeep.actfeeds.data.HabitDbHelper;
import com.inkeep.actfeeds.utilities.OverallRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AllActivities extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerViewAllActivities;
    OverallRecyclerViewAdapter mAdapter;
    public static final int TASK_LOADER_ID = 15;

    private TextView textViewEmptyAll;
    private HabitDbHelper mDbHelper;
    private SQLiteDatabase sqldb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.inkeep.actfeeds.R.layout.activity_all_activities);

        recyclerViewAllActivities = findViewById(com.inkeep.actfeeds.R.id.rvAllActivities);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewAllActivities.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewAllActivities.getContext(),
                linearLayoutManager.getOrientation());
        recyclerViewAllActivities.addItemDecoration(dividerItemDecoration);

        mAdapter = new OverallRecyclerViewAdapter(this);
        recyclerViewAllActivities.setAdapter(mAdapter);

        textViewEmptyAll  = findViewById(com.inkeep.actfeeds.R.id.textViewEmptyAll);
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

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
                    mDbHelper = new HabitDbHelper(getBaseContext());
                    sqldb = mDbHelper.getReadableDatabase();

                    Cursor cursor = null;
                    String Query ="SELECT * FROM UserHabitDetail"+
                            "  ORDER BY UserHabitPK desc";
                    cursor = sqldb.rawQuery(Query, null);
                    return cursor;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mHabitDetailData = data;

                if(mHabitDetailData!=null && mHabitDetailData.getCount()>0){
                    recyclerViewAllActivities.setVisibility(View.VISIBLE);
                    textViewEmptyAll.setVisibility(View.GONE);
                }else{
                    recyclerViewAllActivities.setVisibility(View.GONE);
                    textViewEmptyAll.setVisibility(View.VISIBLE);
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


}
