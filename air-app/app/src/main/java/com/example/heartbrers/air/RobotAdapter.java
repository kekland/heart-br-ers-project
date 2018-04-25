package com.example.heartbrers.air;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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


public class RobotAdapter extends RecyclerView.Adapter<RobotAdapter.RoboViewHolder> {
    private List<RoboInfo> arrayList;
    private Context context;
    private Activity activity;

    public RobotAdapter(List<RoboInfo> arrayList, Context ctx, Activity activity){
        this.arrayList=arrayList;
        this.context = ctx;
        this.activity = activity;
    }


    @Override
    public RoboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_robots, parent,false);
        RoboViewHolder roboViewHolder=new RoboViewHolder(view);
        return roboViewHolder;
    }

    @Override
    public void onBindViewHolder(final RoboViewHolder holder, int position) {
        final RoboInfo roboInfo = arrayList.get(position);

        holder.robotIcon.setBackgroundResource(roboInfo.getRobotIconId());
        holder.robotName.setText(roboInfo.getRobotName());

        Date date = new java.util.Date(roboInfo.getRobotTimestamp());
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");

        holder.robotTime.setText(sdf.format(date));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                prefs.edit().putBoolean(roboInfo.getRobotId() + ".enabled", false).apply();
                arrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Unity Project
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                long timestamp = Calendar.getInstance().getTimeInMillis();
                prefs.edit().putLong(roboInfo.getRobotId() + ".time", timestamp).apply();
                arrayList.get(holder.getAdapterPosition()).setRobotTimestamp(timestamp);
                Sort();
            }
        });

        if(!PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("tutorial_robot_shown", false)) {
            new MaterialTapTargetPrompt.Builder(activity)
                    .setTarget(holder.card)
                    .setPromptFocal(new RectanglePromptFocal())
                    .setPromptBackground(new RectanglePromptBackground())
                    .setPrimaryText("Your Robot")
                    .setSecondaryText("Tap there to start constructing your dream robot")
                    .setBackgroundColour(context.getResources().getColor(R.color.colorPrimary))
                    .show();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("tutorial_robot_shown", true).apply();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void Sort() {
        Collections.sort(arrayList, new Comparator<RoboInfo>() {
            @Override
            public int compare(RoboInfo r1, RoboInfo r2) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                long r1time = prefs.getLong(r1.getRobotId() + ".time", 0);
                long r2time = prefs.getLong(r2.getRobotId() + ".time", 0);
                if(r1time > r2time) {
                    return -1;
                }
                else if(r1time < r2time) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });

        notifyDataSetChanged();
    }

    public class RoboViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        View robotIcon, deleteButton, editButton;
        TextView robotName;
        TextView robotTime;

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
