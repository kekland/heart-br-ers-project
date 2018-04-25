package com.heartbrers.air;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    //Listener for BottomNavigationView
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            //Check ID of selected object (item) in list
            switch (item.getItemId()) {
                case R.id.navigation_robots: //In case if we clicked on Robots item
                    selectedFragment = new RobotsFragment(); //Create Robots fragment
                    TextView toolbar_text = (TextView)findViewById(R.id.toolbar_text); //Get title text of toolbar
                    toolbar_text.setText("My Robots"); //Set text to 'My Robots'
                    break;
                case R.id.navigation_tutorials: //In case if we clicked on Tutorials item
                    selectedFragment = new TutorialsFragment(); //Create Tutorials fragment
                    toolbar_text = (TextView) findViewById(R.id.toolbar_text); //Get title text of toolbar
                    toolbar_text.setText("My Tutorials"); //Set text to 'My tutorials'
                    break;
                case R.id.navigation_profile: //In case if we clicked on Profile item
                    selectedFragment = new ProfileFragment(); //Create Profile fragment
                    toolbar_text = (TextView) findViewById(R.id.toolbar_text); //Get title text of toolbar
                    toolbar_text.setText("My Profile"); //Set text to 'My Profile'
                    break;
            }

            //Put our fragment into container
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            //Return True so that BottomNavigationView will handle our click
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create first fragment (that will be shown first)
        Fragment firstFragment = new RobotsFragment();

        //Put our container into its container
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                firstFragment).commit();


        //Set listener for BottomNavigationView to track clicks
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
