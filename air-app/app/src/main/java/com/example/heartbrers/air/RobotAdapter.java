package com.example.heartbrers.air;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nrgz on 11.04.2018.
 */

public class RobotAdapter extends RecyclerView.Adapter<RobotAdapter.RoboViewHolder> {
    private List<RoboInfo> arrayList;
    private Context context;

    public RobotAdapter(List<RoboInfo> arrayList, Context ctx){
        this.arrayList=arrayList;
        this.context = ctx;
    }


    @Override
    public RoboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_robots, parent,false);
        RoboViewHolder roboViewHolder=new RoboViewHolder(view);
        return roboViewHolder;
    }

    @Override
    public void onBindViewHolder(RoboViewHolder holder, final int position) {
        final RoboInfo roboInfo = arrayList.get(position);

        holder.robotIcon.setBackgroundResource(roboInfo.getRobotIconId());
        holder.robotName.setText(roboInfo.getRobotName());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                prefs.edit().putBoolean(roboInfo.getRobotId() + ".enabled", false).apply();
                arrayList.remove(position);
                notifyItemRemoved(position);
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Unity Project
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public class RoboViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        View robotIcon, deleteButton, editButton;
        TextView robotName;


        public RoboViewHolder (View view){
            super(view);
            robotIcon= view.findViewById(R.id.robot_icon);
            robotName = view.findViewById(R.id.robot_name);
            deleteButton = view.findViewById(R.id.listitem_robot_delete);
            editButton = view.findViewById(R.id.listitem_robot_edit);
            card = view.findViewById(R.id.layout_robot_card);
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
