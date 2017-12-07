package com.example.kavitapc.fitnessreminder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.example.kavitapc.fitnessreminder.reminder.MainApp;
import com.example.kavitapc.fitnessreminder.reminder.NotificationJobCreator;
import com.example.kavitapc.fitnessreminder.reminder.NotificationSyncJob;
import com.example.kavitapc.fitnessreminder.utilities.PagerAdapter;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements AddedGoals.OnFragmentInteractionListener,
        FeedsPage.OnFragmentInteractionListener, HabitsReport.OnFragmentInteractionListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionToggle;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();


        tabLayout = (TabLayout) findViewById(R.id.tabs);

        //Open drawer when clicked on Action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.addDrawerListener(mActionToggle);
        mActionToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //setting up view pager
        viewPager =(ViewPager)findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new AddedGoals(), "");
        pagerAdapter.addFragment(new FeedsPage(),"");
        pagerAdapter.addFragment(new HabitsReport(), "");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_av_timer_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_show_chart_black_24dp);

        //setting tab color to RED
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            }
        });


        //get extra data from server
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("test")) {
            Log.d(LOG_TAG, "Contains: " + extras.getString("test"));
        }else {
            Log.d(LOG_TAG, "Contains: Nothing");
        }

        //ScheduleReminder.scheduleActivityReminder(this);
        //Calling job scheduler
        JobManager.create(this).addJobCreator(new NotificationJobCreator());
        //NotificationSyncJob.scheduleJob();
        NotificationSyncJob.schedule();

        String token = FirebaseInstanceId.getInstance().getToken();
        String msg = getString(R.string.message_token_format, token);
        Log.d(LOG_TAG, msg);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionToggle.onOptionsItemSelected(item)||super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_button, menu);
        MenuItem item = menu.getItem(0);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(), GoalDetails.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }



}

