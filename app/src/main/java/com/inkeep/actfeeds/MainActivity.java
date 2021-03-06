package com.inkeep.actfeeds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.evernote.android.job.JobManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inkeep.actfeeds.R;

import com.inkeep.actfeeds.reminder.NotificationJobCreator;
import com.inkeep.actfeeds.reminder.NotificationSyncJob;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import tourguide.tourguide.TourGuide;

public class MainActivity extends AppCompatActivity implements AddedGoals.OnFragmentInteractionListener,
        FeedsPage.OnFragmentInteractionListener, HabitsReport.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionToggle;
    private static final String PAGE_NAME= "PageName";

    private ViewPager viewPager;
    private MyPageAdapter pagerAdapter;
    private TabLayout tabLayout;
    private int position;
    List<Fragment> fragments = new ArrayList<>();
    //private  TourGuide  mTourHandler;
    private FirebaseAuth mAuth;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //On boarding

       /* SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        if (!preferences.getBoolean("onboarding_complete", false)) {

            Intent onboarding = new Intent(this, OnboardingActivity.class);
            startActivity(onboarding);
            finish();
            //return;
        }*/
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            Intent onboarding = new Intent(this, OnboardingActivity.class);
            startActivity(onboarding);
            finish();
        }

        if (user != null) {
            // Name, email address, and profile photo Url
           // name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();


        }

        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();


        tabLayout = findViewById(R.id.tabs);

        //Open drawer when clicked on Action bar
        mDrawerLayout = findViewById(R.id.drawerLayout);
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
        viewPager = findViewById(R.id.pager);

        pagerAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
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
       // tvName.setText(name);

        Log.d("name", "success"+mAuth.getCurrentUser());

        //get extra data from server
       /* Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("test")) {
            Log.d(LOG_TAG, "Contains: " + extras.getString("test"));
        }else {
            Log.d(LOG_TAG, "Contains: Nothing");
        }*/

        //ScheduleReminder.scheduleActivityReminder(this);
        //Calling job scheduler
        JobManager.create(this).addJobCreator(new NotificationJobCreator());
        //NotificationSyncJob.scheduleJob();
        NotificationSyncJob.schedule();

        String token = FirebaseInstanceId.getInstance().getToken();
        String msg = getString(R.string.message_token_format, token);
        // Log.d(LOG_TAG, msg);

        //calling navigation view item on click listener
        setNavigationViewListner();

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();


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
        /*final ImageView addButton = (ImageView) item.getActionView();
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
                addButton.setClickable(true);
            }
        });
*/

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
        NavigationView navigationView = findViewById(R.id.navigationID);
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
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    intent.putExtra(Intent.EXTRA_TEXT, "Plan, Act and progress with ActFeeds " +
                            "https://play.google.com/store/apps/details?id=com.inkeep.actfeeds");
                    startActivity(Intent.createChooser(intent, "Choose one to share app"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
            case R.id.navAppTour:{
                Intent intent = new Intent(this,AppTour.class);
                startActivity(intent);
                break;
            }
            case R.id.navPrivacyPolicy:{
                Intent intent = new Intent(this,PrivacyPolicy.class);
                intent.putExtra(PAGE_NAME,"PrivacyPolicy");
                startActivity(intent);
                break;
            }
            case R.id.navTerms:{
                Intent intent = new Intent(this,PrivacyPolicy.class);
                intent.putExtra(PAGE_NAME,"Terms");
                startActivity(intent);
                break;
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

