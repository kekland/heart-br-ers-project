package com.heartbrers.air;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Created by nrgz on 12.04.2018.
 */

//This is the adapter for Recycler View with tutorials
public class TutAdapter extends RecyclerView.Adapter<TutAdapter.TutViewHolder> {
    //Our dataset
    private List<TutorialsInfo> arrayList;

    //Context and Activity of Fragment that this RecyclerView is in
    private Context context;
    private Activity activity;

    //Constructor
    public TutAdapter(Context ctx, List<TutorialsInfo> arrayList, Activity activity){
        this.context = ctx;
        this.arrayList=arrayList;
        this.activity = activity;
    }


    //Recycler View uses ViewHolders to effectively manage drawing Views on the screen - thus making the app more faster
    //This function creates new ViewHolder
    @Override
    public TutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the view to attach the ViewHolder to
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_tutorials, parent,false);
        //Create new ViewHolder object
        TutViewHolder tutViewHolder = new TutViewHolder(view);
        return tutViewHolder;
    }

    //Binding ViewHolder means filling it up with data. It happens when it is visible on screen.
    @Override
    public void onBindViewHolder(final TutViewHolder holder, int position) {
        //Get data based on position of ViewHolder
        final TutorialsInfo tutorialsInfo = arrayList.get(position);

        //Set video's name
        holder.v_name.setText(tutorialsInfo.getVideoName());

        //Add listener to the card itself so that when we click on card:
        holder.tutorialsBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the video URL
                String url = tutorialsInfo.getVideoURL();

                //Open new Intent with the URL to open either YouTube or browser
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });

        //Load thumbnail image from the web by using Picasso plugin
        //It is fast, easy and very optimised
        Picasso.get().load(tutorialsInfo.getPlaceholderURL()).into(holder.video);

        //Check that we have not shown the tutorial for Tutorial Videos already
        if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("tutorial_tutorial_video_shown", false)) {
            //If we did not show tutorial - show it, using MaterialTapTargetPrompt library
            new MaterialTapTargetPrompt.Builder(activity)
                    .setTarget(holder.tutorialsBackground)
                    .setPromptFocal(new RectanglePromptFocal())
                    .setPromptBackground(new RectanglePromptBackground())
                    .setPrimaryText("Tutorial videos")
                    .setSecondaryText("You can find all needed information about various topics here")
                    .setBackgroundColour(context.getResources().getColor(R.color.colorPrimary))
                    .show();

            //Save boolean to memory to make sure that tutorial will be shown only one time
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("tutorial_tutorial_video_shown", true).apply();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    //Our ViewHolder designed for Tutorials
    public static class TutViewHolder extends RecyclerView.ViewHolder {
        View tutorialsBackground; //CardView itself
        ImageView video; //Thumbnail image
        TextView v_name; //Video name
        public TutViewHolder (View view) {
            //Set variables
            super(view);
            tutorialsBackground = view.findViewById(R.id.layout_tutorials_holder);
            v_name= (TextView)view.findViewById(R.id.video_name);
            video=(ImageView)view.findViewById(R.id.video);

        }
    }
}

