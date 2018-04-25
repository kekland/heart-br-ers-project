package com.heartbrers.air;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Created by ASAlmaty2 on 06.04.2018.
 */

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate our fragment's view
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Get necessary objects
        final View data = view.findViewById(R.id.profile_data);
        final ProgressBar progress = view.findViewById(R.id.profile_progress);

        //Firstly, we are loading data, so make data invisible and show progress bar
        data.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        //Get instance of current activity
        final Activity activity = getActivity();

        //Get instance of preferences, so we can get data from harddisk
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        //Get username of current user
        final String username = prefs.getString("username", "");

        //Get reference of Firebase Database
        final FirebaseDatabase db = FirebaseDatabase.getInstance();

        //Load data from Firebase
        db.getReference().child("users").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //If we switched to another view already - dont do anything
                if (isDetached()) {
                    return;
                }
                //Get data from Firebase
                String region = dataSnapshot.child("region").getValue().toString();
                int coins = Integer.parseInt(dataSnapshot.child("coins").getValue().toString());

                //Get TextViews to set
                TextView profileUsernameText = view.findViewById(R.id.profile_username);
                TextView profileRegionText = view.findViewById(R.id.profile_region);
                TextView avatarText = view.findViewById(R.id.profile_avatar_name);
                TextView coinText = view.findViewById(R.id.profile_coins);

                profileRegionText.setText(region); //Set text for Region
                profileUsernameText.setText(username); //Set text for Username
                avatarText.setText(Character.toString(Character.toUpperCase(username.charAt(0)))); //Get first character of username and set it to avatar
                coinText.setText(Integer.toString(coins)); //Set text for coins

                //Show the data and hide progress bar
                data.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

                //Check that we have not shown the tutorial for Robocoins already
                if (getContext() != null && !PreferenceManager.getDefaultSharedPreferences(getContext())
                        .getBoolean("tutorial_robocoins_shown", false)) {
                    //If we did not show tutorial - show it, using MaterialTapTargetPrompt library
                    new MaterialTapTargetPrompt.Builder(activity).setTarget(view.findViewById(R.id.card_view_coins))
                            .setPromptFocal(new RectanglePromptFocal())
                            .setPromptBackground(new RectanglePromptBackground()).setPrimaryText("Robocoins")
                            .setSecondaryText("You can spend your Robocoins on extra kits")
                            .setBackgroundColour(getResources().getColor(R.color.colorPrimary)).show();

                    //Save boolean to memory to make sure that tutorial will be shown only one time
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putBoolean("tutorial_robocoins_shown", true).apply();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(view, databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });

        //TODO: Create more tasks and add Firebase interface
        //When we click on task button
        view.findViewById(R.id.taskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create Intent to switch to Activity that contains Unity Player (robot construction)
                Intent intent = new Intent(getContext(), UnityPlayerActivity.class);

                //Put necessary data into Intent
                Bundle extras = new Bundle();
                extras.putString("robotId", "task1RobotId"); //Custom Robot identifier
                extras.putString("taskData", "Distance 5"); //Task identifier
                intent.putExtras(extras);

                //Start the activity
                getContext().startActivity(intent);

                //Add coins to our balance
                db.getReference().child("users").child(username).child("coins").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().setValue(Integer.parseInt(dataSnapshot.getValue().toString()) + 5);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        return view;
    }
}