package com.example.kavitapc.fitnessreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.example.kavitapc.fitnessreminder.reminder.MainApp;
import com.example.kavitapc.fitnessreminder.reminder.NotificationJobCreator;
import com.example.kavitapc.fitnessreminder.reminder.NotificationSyncJob;
import com.example.kavitapc.fitnessreminder.utilities.PagerAdapter;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

public class MainActivity extends AppCompatActivity implements AddedGoals.OnFragmentInteractionListener,
        FeedsPage.OnFragmentInteractionListener, HabitsReport.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionToggle;

    private ViewPager viewPager;
    private MyPageAdapter pagerAdapter;
    private TabLayout tabLayout;
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private int position;
    List<Fragment> fragments = new ArrayList<Fragment>();
    private  TourGuide  mTourHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Onboarding

        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        if(!preferences.getBoolean("onboarding_complete",false)){

            Intent onboarding = new Intent(this, OnboardingActivity.class);
            startActivity(onboarding);
            finish();
            //return;
            }

        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();


        tabLayout = (TabLayout) findViewById(R.id.tabs);

        //Open drawer when clicked on Action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.addDrawerListener(mActionToggle);
        mActionToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragments.add(new AddedGoals());
        fragments.add(new FeedsPage());
        fragments.add(new HabitsReport());
       /* ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
        pagerItems.add(new PagerItem("Fragment1", new AddedGoals()));
        pagerItems.add(new PagerItem("Fragment2", new FeedsPage()));
        pagerItems.add(new PagerItem("Fragment3", new HabitsReport()));
*/
        //setting up view pager
        viewPager =(ViewPager)findViewById(R.id.pager);
        pagerAdapter = new MyPageAdapter(getSupportFragmentManager(),fragments);
       // pagerAdapter.addFragment(new AddedGoals(), "");
       // pagerAdapter.addFragment(new FeedsPage(),"");
       // pagerAdapter.addFragment(new HabitsReport(), "");

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
                position = tabLayout.getSelectedTabPosition();
                Log.d("position","ppppppp"+position);
                viewPager.setCurrentItem(position);
                // viewPager.getAdapter().notifyDataSetChanged();

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

        //calling navigation view item on click listener
        setNavigationViewListner();

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


        //Creating tour guide
        /*ImageView addButton = (ImageView) item.getActionView();
        addButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_input_add));
        mTourHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .motionType(TourGuide.MotionType.ClickOnly)
                .setPointer(new Pointer().setGravity(Gravity.START | Gravity.BOTTOM))
                .setToolTip( new ToolTip().setTitle("Welcome").setDescription("click to add an activity")
                        .setBackgroundColor(Color.parseColor("#e54d26"))
                        .setShadow(true).setGravity(Gravity.LEFT | Gravity.BOTTOM))
                .setOverlay(new Overlay()).playOn(addButton);


        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTourHandler.cleanUp();
            }
        });*/


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

    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationID);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //case R.id.navUserProfile:{
                    //Intent intent = new Intent(this,UserProfile.class);
           // }
           // case R.id.navLoginLogout:{
                    //String login="";
            //}
            case R.id.navContactUs:{
                Intent intent = new Intent(this,ContactUS.class);
                startActivity(intent);
                break;
            }
            case R.id.navShareApp:{
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ActFeed");
                    intent.putExtra(Intent.EXTRA_TEXT, "Act to develop new habits and to achieve goals" +
                            "market://play.google.com/store/apps/details?id=Orion.Soft"); //TODO:change the id to your app id
                    startActivity(Intent.createChooser(intent, "Choose one to share app"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            case R.id.navAppTour:{
                //Intent intent = new Intent(this,AppTour.class);
            }
            case 0:{
                break;
            }
        }
        return true;
    }

    public class MyPageAdapter extends FragmentStatePagerAdapter {

        // fragments to instantiate in the viewpager
        private List<Fragment> fragments;

        // constructor
        public MyPageAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        // return access to fragment from position, required override
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        // number of fragments in list, required override
        @Override
        public int getCount() {
            return this.fragments.size();
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }
}

