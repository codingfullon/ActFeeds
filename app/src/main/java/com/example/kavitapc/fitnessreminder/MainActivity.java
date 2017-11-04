package com.example.kavitapc.fitnessreminder;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.kavitapc.fitnessreminder.utilities.PagerAdapter;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements AddedGoals.OnFragmentInteractionListener,
        FeedsPage.OnFragmentInteractionListener, HabitsReport.OnFragmentInteractionListener {

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
        pagerAdapter.addFragment(new AddedGoals(),"AddedHabits");
        pagerAdapter.addFragment(new FeedsPage(),"AddNewHabits");
        pagerAdapter.addFragment(new HabitsReport(), "Reports");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //get extra data from server
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("test")) {
            Log.d(LOG_TAG, "Contains: " + extras.getString("test"));
        }else {
            Log.d(LOG_TAG, "Contains: Nothing");
        }

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
}

