package com.example.heartbrers.air;
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


public class TutAdapter extends RecyclerView.Adapter<TutAdapter.TutViewHolder> {
    private List<TutorialsInfo> arrayList;
    private Context context;
    private Activity activity;

    public TutAdapter(Context ctx, List<TutorialsInfo> arrayList, Activity activity){
        this.context = ctx;
        this.arrayList=arrayList;
        this.activity = activity;
    }


    @Override
    public TutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_tutorials, parent,false);
        TutViewHolder tutViewHolder=new TutViewHolder(view);
        return tutViewHolder;
    }

    @Override
    public void onBindViewHolder(final TutViewHolder holder, int position) {
        final TutorialsInfo tutorialsInfo = arrayList.get(position);
        holder.v_name.setText(tutorialsInfo.getVideoName());
        holder.tutorialsBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = tutorialsInfo.getVideoURL();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
        Picasso.get().load(tutorialsInfo.getPlaceholderURL()).into(holder.video);

        if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("tutorial_tutorial_video_shown", false)) {
            new MaterialTapTargetPrompt.Builder(activity)
                    .setTarget(holder.tutorialsBackground)
                    .setPromptFocal(new RectanglePromptFocal())
                    .setPromptBackground(new RectanglePromptBackground())
                    .setPrimaryText("Tutorial videos")
                    .setSecondaryText("You can find all needed information about various topics here")
                    .setBackgroundColour(context.getResources().getColor(R.color.colorPrimary))
                    .show();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("tutorial_tutorial_video_shown", true).apply();
        }
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class TutViewHolder extends RecyclerView.ViewHolder {
        View tutorialsBackground;
        ImageView video;
        TextView v_name;
        public TutViewHolder (View view){
            super(view);
            tutorialsBackground = view.findViewById(R.id.layout_tutorials_holder);
            v_name= (TextView)view.findViewById(R.id.video_name);
            video=(ImageView)view.findViewById(R.id.video);

        }
    }
}

