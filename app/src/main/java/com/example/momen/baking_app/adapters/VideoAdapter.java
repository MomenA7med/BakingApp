package com.example.momen.baking_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momen.baking_app.R;
import com.example.momen.baking_app.model.Steps;

import java.util.List;

/**
 * Created by Momen on 2/8/2019.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoVH> {


    List<Steps> steps;
    Context context;

    private VideoClickListener videoClickListener;

    public interface VideoClickListener {
        public void onItemClick(int position);
    }

    public VideoAdapter(Context context,List<Steps> steps,VideoClickListener videoClickListener){
        this.context = context;
        this.steps = steps;
        this.videoClickListener = videoClickListener;
    }

    @NonNull
    @Override
    public VideoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        return new VideoVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoVH holder, int position) {

        holder.videoName.setText(steps.get(position).getShortDesc());

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class VideoVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView videoName;
        public VideoVH(View itemView) {
            super(itemView);
            videoName = itemView.findViewById(R.id.video_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            videoClickListener.onItemClick(getAdapterPosition());
        }
    }
}
