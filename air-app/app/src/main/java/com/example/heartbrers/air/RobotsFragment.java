package com.example.heartbrers.air;

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
import java.util.List;

/**
 * Created by nrgz on 06.04.2018.
 */
public class RobotsFragment extends Fragment {

    RecyclerView recyclerView;
    RobotAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_robots, container, false);
        recyclerView= (RecyclerView)view.findViewById(R.id.recycler_view_robots);
        final View root = view.findViewById(R.id.robotsRoot);

        List<RoboInfo> info = new ArrayList<>();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        for(int i = 0; i < prefs.getInt("robotCount", 0); i++) {
            String robotId = prefs.getString("robotIds." + i, "null");
            boolean robotEnabled = prefs.getBoolean(robotId + ".enabled", false);
            if(robotEnabled) {
                String robotName = prefs.getString(robotId + ".name", "null");
                info.add(new RoboInfo(R.drawable.industrial_robot, robotName, robotId, i));
            }
        }
        adapter = new RobotAdapter(info, getContext());


        recyclerView.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Button btn = view.findViewById(R.id.button_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_robot, null);
                Log.i("Created", "Created dialogview");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setView(dialogView)
                        .setTitle("Add new robot")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText nameEdit = dialogView.findViewById(R.id.robot_name_edittext);
                                EditText idEdit = dialogView.findViewById(R.id.robot_id_edittext);

                                if(nameEdit.getText().length() != 0 && idEdit.getText().length() != 0) {
                                    String robotName = nameEdit.getText().toString();
                                    String robotId = idEdit.getText().toString();
                                    if(!prefs.getBoolean(robotId + ".enabled", false)) {
                                        int thisIndex = prefs.getInt("robotCount", 0);
                                        prefs.edit().putString(robotId + ".name", robotName)
                                                .putBoolean(robotId + ".enabled", true)
                                                .putString("robotIds." + thisIndex, robotId)
                                                .putInt("robotCount", thisIndex + 1).apply();
                                        adapter.addItem(new RoboInfo(R.drawable.industrial_robot, robotName, robotId, thisIndex));
                                    }
                                    else {
                                        Snackbar.make(root, "Robot's identifier must be unique", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Snackbar.make(root, "Robot's name or identifier cannot be empty", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.create().show();
            }
        });
        return view;


    }



}