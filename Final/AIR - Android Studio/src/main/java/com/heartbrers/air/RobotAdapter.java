package com.heartbrers.air;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Created by nrgz on 11.04.2018.
 */

//This Adapter is used in RecyclerView in the RobotsFragment to display the list of our robots
public class RobotAdapter extends RecyclerView.Adapter<RobotAdapter.RoboViewHolder> {
    //Our dataset
    private List<RoboInfo> arrayList;

    //Context and Activity of RobotsFragment
    private Context context;
    private Activity activity;

    //Constructor
    public RobotAdapter(List<RoboInfo> arrayList, Context ctx, Activity activity){
        this.arrayList=arrayList;
        this.context = ctx;
        this.activity = activity;
    }

    //Recycler View uses ViewHolders to effectively manage drawing Views on the screen - thus making the app more faster
    //This function creates new ViewHolder
    @Override
    public RoboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the view to attach to the Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_robots, parent, false);

        //Create the ViewHolder object
        RoboViewHolder roboViewHolder = new RoboViewHolder(view);
        return roboViewHolder;
    }

    //Binding ViewHolder means filling it up with data. It happens when it is visible on screen.
    @Override
    public void onBindViewHolder(final RoboViewHolder holder, int position) {
        //Get the RoboInfo object based on item's position
        final RoboInfo roboInfo = arrayList.get(position);

        //Set robot's image and text
        holder.robotIcon.setBackgroundResource(roboInfo.getRobotIconId());
        holder.robotName.setText(roboInfo.getRobotName());

        //Get Date object that is set to date of last change made to roobt
        Date date = new java.util.Date(roboInfo.getRobotTimestamp());

        //Format the date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");

        //Set the text
        holder.robotTime.setText(sdf.format(date));

        //When we click delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get SharedPreferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                //Disable our robot (Mark it as deleted)
                prefs.edit().putBoolean(roboInfo.getRobotId() + ".enabled", false).apply();
                //Remove the robot from dataset
                arrayList.remove(holder.getAdapterPosition());
                //Notify that item was removed so that RecyclerView will update
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        //When we click on the card itself
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We start editing the robot - so update the timestamp of the robot
                //Get SharedPreferences object
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

                //Get the timestamp of current time
                long timestamp = Calendar.getInstance().getTimeInMillis();

                //Save the timestamp
                prefs.edit().putLong(roboInfo.getRobotId() + ".time", timestamp).apply();

                //Set time timestamp in object in the dataset
                arrayList.get(holder.getAdapterPosition()).setRobotTimestamp(timestamp);

                //Sort the dataset based on the time of last edit made
                Sort();

                //Create new intent to open Unity Activity (robot construction)
                Intent intent = new Intent(context, UnityPlayerActivity.class);

                //Create new Bundle to pass data to Unity Activity
                Bundle extras = new Bundle();
                extras.putString("robotId", roboInfo.getRobotId()); //Pass the Robot Id
                extras.putString("taskData", "NoTask"); //Pass the Data of task (none)

                //Put the data into Intent and start Unity Activity
                intent.putExtras(extras);
                context.startActivity(intent);
                //activity.finishAfterTransition();
            }
        });

        //Check that we have not shown the tutorial for Robot already
        if(!PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("tutorial_robot_shown", false)) {
            //If we did not show tutorial - show it, using MaterialTapTargetPrompt library

            new MaterialTapTargetPrompt.Builder(activity)
                    .setTarget(holder.card)
                    .setPromptFocal(new RectanglePromptFocal())
                    .setPromptBackground(new RectanglePromptBackground())
                    .setPrimaryText("Your Robot")
                    .setSecondaryText("Tap there to start constructing your dream robot")
                    .setBackgroundColour(context.getResources().getColor(R.color.colorPrimary))
                    .show();

            //Save boolean to memory to make sure that tutorial will be shown only one time
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("tutorial_robot_shown", true).apply();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    //This function sorts the dataset based on the time of last change made to robot
    public void Sort() {
        Collections.sort(arrayList, new Comparator<RoboInfo>() {
            @Override
            public int compare(RoboInfo r1, RoboInfo r2) {
                //Get timestamps from memory
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                long r1time = prefs.getLong(r1.getRobotId() + ".time", 0);
                long r2time = prefs.getLong(r2.getRobotId() + ".time", 0);

                //If it differs - swap items
                if(r1time > r2time) {
                    return -1;
                }
                else if(r1time < r2time) {
                    return 1;
                }
                else {
                    //If not - do not swap
                    return 0;
                }
            }
        });

        //Notify that dataset was changed to update RecyclerView
        notifyDataSetChanged();
    }

    //Our ViewHolder designed for Robots
    public class RoboViewHolder extends RecyclerView.ViewHolder {

        CardView card; //Card itself
        View robotIcon, deleteButton, editButton; //Robot's icon and buttons
        TextView robotName; //Robot's name
        TextView robotTime; //Robot's time of last change

        //Constructor
        public RoboViewHolder (View view){
            super(view);
            robotIcon= view.findViewById(R.id.robot_icon);
            robotName = view.findViewById(R.id.robot_name);
            deleteButton = view.findViewById(R.id.listitem_robot_delete);
            editButton = view.findViewById(R.id.listitem_robot_edit);
            card = view.findViewById(R.id.layout_robot_card);
            robotTime = view.findViewById(R.id.robot_time);
        }
    }

    public void removeItem(int position) {
        arrayList.remove(position);
        notifyItemInserted(position);
        notifyItemChanged(position, arrayList.size());
    }

    public void addItem(RoboInfo currentObject) {
        arrayList.add(currentObject);
        notifyDataSetChanged();
    }


}
