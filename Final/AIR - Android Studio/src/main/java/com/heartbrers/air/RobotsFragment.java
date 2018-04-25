package com.heartbrers.air;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Created by nrgz on 06.04.2018.
 */

 //This fragment contains data about Robots
public class RobotsFragment extends Fragment {

    //Recycler view and its adapter
    RecyclerView recyclerView;
    RobotAdapter adapter;

    //LayoutManager for RecyclerView
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the view
        View view = inflater.inflate(R.layout.fragment_robots, container, false);

        //Get necessary Views
        recyclerView= (RecyclerView)view.findViewById(R.id.recycler_view_robots);
        final View root = view.findViewById(R.id.robotsRoot);

        //Create list for data and get SharedPrefs
        List<RoboInfo> info = new ArrayList<>();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        //Retrieve data from storage
        for(int i = 0; i < prefs.getInt("robotCount", 0); i++) {
            String robotId = prefs.getString("robotIds." + i, "null"); //Get robot's id on index
            boolean robotEnabled = prefs.getBoolean(robotId + ".enabled", false); //Check if robot is not disabled (not marked as deleted)
            //If it is enabled
            if(robotEnabled) {
                String robotName = prefs.getString(robotId + ".name", "null"); //Get robot's name
                long timestamp = prefs.getLong(robotId + ".time", 0); //Get the timestamp
                RoboInfo data = new RoboInfo(R.drawable.industrial_robot, robotName, robotId, i); //Set data
                data.setRobotTimestamp(timestamp); //Set the timestamp
                info.add(data); //Add the data to the list

            }
        }

        //Create new adapter and pass the data
        adapter = new RobotAdapter(info, getContext(), getActivity());

        //Sort the data
        adapter.Sort();

        //Set necessary parameters for RecyclerView
        recyclerView.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //Check that we have not shown the tutorial for Creating Robots already
        if(!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("tutorial_create_robot_shown", false)) {
            //If we did not show tutorial - show it, using MaterialTapTargetPrompt library
            new MaterialTapTargetPrompt.Builder(getActivity())
                    .setTarget(view.findViewById(R.id.button_add))
                    .setPromptFocal(new RectanglePromptFocal())
                    .setPromptBackground(new RectanglePromptBackground())
                    .setPrimaryText("Create your first robot")
                    .setSecondaryText("Click this button to start building the future")
                    .setBackgroundColour(getResources().getColor(R.color.colorPrimary))
                    .show();

            //Save boolean to memory to make sure that tutorial will be shown only one time
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("tutorial_create_robot_shown", true).apply();
        }

        //Get the button 'Add new robot'
        Button btn = view.findViewById(R.id.button_add);

        //Set listener on the button's click
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inflate new view for the dialog
                final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_robot, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //Set the view, title for the dialog
                builder.setView(dialogView)
                        .setTitle("Add new robot")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //When we click on 'Add button'

                                //Get necessary views
                                EditText nameEdit = dialogView.findViewById(R.id.robot_name_edittext);
                                EditText idEdit = dialogView.findViewById(R.id.robot_id_edittext);

                                //If neither of them is empty
                                if(nameEdit.getText().length() != 0 && idEdit.getText().length() != 0) {
                                    //Get the string values of them
                                    String robotName = nameEdit.getText().toString();
                                    String robotId = idEdit.getText().toString();

                                    //Check that this robot's ID is unique
                                    if(!prefs.getBoolean(robotId + ".enabled", false)) {
                                        //If it is unique - save the data to storage
                                        int thisIndex = prefs.getInt("robotCount", 0);
                                        prefs.edit().putString(robotId + ".name", robotName) //Save the name
                                                .putBoolean(robotId + ".enabled", true) //Set that it is active (Created)
                                                .putString("robotIds." + thisIndex, robotId) //Set its index
                                                .putLong(robotId + ".time", Calendar.getInstance().getTimeInMillis()) //Set the timestamp
                                                .putInt("robotCount", thisIndex + 1).apply(); //Increment the counter of robots

                                        //Create new object of type RoboInfo
                                        long timestamp = prefs.getLong(robotId + ".time", 0);
                                        RoboInfo data = new RoboInfo(R.drawable.industrial_robot, robotName, robotId, i);
                                        data.setRobotTimestamp(timestamp);

                                        //Add the item to adapter
                                        adapter.addItem(data);
                                    }
                                    else {
                                        //If ID is not unique - show error message
                                        Snackbar.make(root, "Robot's identifier must be unique", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    //If username or identifier are empty - show error message
                                    Snackbar.make(root, "Robot's name or identifier cannot be empty", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //If we clicked on Cancel button - close dialog
                        dialogInterface.dismiss();
                    }
                });

                //Show the dialog
                builder.create().show();
            }
        });
        return view;


    }



}
