package com.heartbrers.air;

import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heart<bR>ers on 06.04.2018.
 */

//This fragment shows list of tutorials
public class TutorialsFragment extends Fragment {

    //Recycler View and its adapter
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    //Layout manager for Recycler View
    LinearLayoutManager layoutManager;


    //Progress bar
    ProgressBar mprogressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        //Inflate the view
        View view = inflater.inflate(R.layout.fragment_tutorials, container, false);

        //Get necessary views
        mprogressBar = (ProgressBar) view.findViewById(R.id.circular_progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_tutorials);

        //Set different parameters for RecyclerView
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Add dividers between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //Get Firebase Database instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Open folder with videos
        DatabaseReference reference = database.getReference("videos");

        //Get data
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //When we finished getting data - fill the array
                List<TutorialsInfo> info = new ArrayList<>();

                //For each video - add new object with data about this video like URL, name, e.t.c
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    info.add(new TutorialsInfo(child));
                }

                //Hide the progress bar
                mprogressBar.setVisibility(View.GONE);

                //Create adapter and attach it to RecyclerView
                TutAdapter adapter = new TutAdapter(getContext(), info, getActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Download failed - show error message
                Log.e("FIREBASE", "DOWNLOAD ERROR");
            }
        });
        return view;
    }

}