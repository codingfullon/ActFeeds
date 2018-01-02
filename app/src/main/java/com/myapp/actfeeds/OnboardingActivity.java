package com.myapp.actfeeds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.actfeeds.R;

public class OnboardingActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ImageView intro_indicator_0;
    ImageView intro_indicator_1;
    ImageView intro_indicator_2;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final Button mNextButton = (Button)findViewById(R.id.intro_btn_next);
        final Button mDoneButton = (Button)findViewById(R.id.intro_btn_finish);
        intro_indicator_0 =(ImageView)findViewById(R.id.intro_indicator_0);
        intro_indicator_1 =(ImageView)findViewById(R.id.intro_indicator_1);
        intro_indicator_2 =(ImageView)findViewById(R.id.intro_indicator_2);
        Button mSkipButton = (Button)findViewById(R.id.intro_btn_skip);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int pageNo = position;
                updateIndicator(pageNo);

                mNextButton.setVisibility(position==2? View.GONE:View.VISIBLE);
                mDoneButton.setVisibility(position==2? View.VISIBLE:View.GONE);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishOnboarding();
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem()==2){
                    finishOnboarding();
                }
                else{
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
                }
            }
        });
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishOnboarding();
            }
        });


    }

    private void finishOnboarding() {
        SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);

        preferences.edit()
                .putBoolean("onboarding_complete",true).apply();

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

        finish();
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_onboarding, container, false);

            TextView tvBenefits = (TextView) rootView.findViewById(R.id.tvBenefits);
            String textStr = "getting_started_text1";
            try {
                textStr = "getting_started_text"+getArguments().getInt(ARG_SECTION_NUMBER);
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }

           // getResources().getIdentifier(textStr, "string", "com.example.kavitapc.fitnessreminder");
            tvBenefits.setText(getResources().getIdentifier(textStr, "string", "com.myapp.actfeeds"));
            //tvBenefits.setText(getString(R.string.getting_started_text%1$,getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
    public void updateIndicator(int position) {
        switch (position) {
            case 0: {
                intro_indicator_0.setBackgroundResource(R.drawable.sun);
                intro_indicator_1.setBackgroundResource(R.drawable.circle);
                intro_indicator_2.setBackgroundResource(R.drawable.circle);
                break;
            }
            case 1: {
                intro_indicator_0.setBackgroundResource(R.drawable.circle);
                intro_indicator_1.setBackgroundResource(R.drawable.sun);
                intro_indicator_2.setBackgroundResource(R.drawable.circle);
                break;
            }
            case 2: {
                intro_indicator_0.setBackgroundResource(R.drawable.circle);
                intro_indicator_1.setBackgroundResource(R.drawable.circle);
                intro_indicator_2.setBackgroundResource(R.drawable.sun);
                break;
            }
        }
    }

}
