package com.example.heartbrers.air;

import android.app.Activity;
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
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final View data = view.findViewById(R.id.profile_data);
        final ProgressBar progress = view.findViewById(R.id.profile_progress);

        data.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        final Activity activity = getActivity();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String username = prefs.getString("username", "");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference().child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isDetached()) {
                    return;
                }
                String region = dataSnapshot.child("region").getValue().toString();
                int coins = Integer.parseInt(dataSnapshot.child("coins").getValue().toString());

                TextView profileUsernameText = view.findViewById(R.id.profile_username);
                TextView profileRegionText = view.findViewById(R.id.profile_region);
                TextView avatarText = view.findViewById(R.id.profile_avatar_name);
                TextView coinText = view.findViewById(R.id.profile_coins);

                profileRegionText.setText(region);
                profileUsernameText.setText(username);
                avatarText.setText(Character.toString(Character.toUpperCase(username.charAt(0))));
                coinText.setText(Integer.toString(coins));
                data.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

                new MaterialTapTargetPrompt.Builder(activity)
                        .setTarget(view.findViewById(R.id.card_view_coins))
                        .setPromptFocal(new RectanglePromptFocal())
                        .setPromptBackground(new RectanglePromptBackground())
                        .setPrimaryText("Robocoins")
                        .setSecondaryText("You can spend your Robocoins on extra kits")
                        .setBackgroundColour(getResources().getColor(R.color.colorPrimary))
                        .show();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(view, databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}