package com.example.kavitapc.fitnessreminder;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionToggle;

    private TextView tvDetailDailyHabits;
    private TextView tvDetailWeeklyHabits;
    private TextView tvDetailMonthlyHabits;
    private TextView tvDetailCreateOwnHabits;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Open drawer when clicked on Action bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.addDrawerListener(mActionToggle);
        mActionToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Initializing the variables
        //tvDetailDailyHabits = (TextView) findViewById(R.id.tvDailyHabits);
       // tvDetailWeeklyHabits = (TextView) findViewById(R.id.tvWeeklyHabits);
       // tvDetailMonthlyHabits = (TextView) findViewById(R.id.tvMonthlyHabits);
       // tvDetailCreateOwnHabits = (TextView) findViewById(R.id.tvCreateOwn);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mActionToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id==R.id.tvDailyHabits){
            mToast=Toast.makeText(this,"Item was clicked",Toast.LENGTH_LONG);
            mToast.show();
            Intent intent=new Intent(MainActivity.this,DailyHabitsDetails.class);
            startActivity(intent);
        }else if(id==R.id.tvWeeklyHabits){
            mToast=Toast.makeText(this,"Item was clicked",Toast.LENGTH_LONG);
            mToast.show();
        }
        else if(id==R.id.tvMonthlyHabits){
            mToast=Toast.makeText(this,"Item was clicked",Toast.LENGTH_LONG);
            mToast.show();

        }else if(id==R.id.tvCreateOwn){
            mToast=Toast.makeText(this,"Item was clicked",Toast.LENGTH_LONG);
            mToast.show();

        }

    }
}

