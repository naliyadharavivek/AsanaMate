package com.asana.mate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class YogaPoseAdapter extends RecyclerView.Adapter<YogaPoseAdapter.ViewHolder> {
    private Context context;
    private List<YogaPose> yogaPoseList;

    public YogaPoseAdapter(Context context, List<YogaPose> yogaPoseList) {
        this.context = context;
        this.yogaPoseList = yogaPoseList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEngName, tvSanName, tvDescription;
        ImageView imgPose;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEngName = itemView.findViewById(R.id.tvEngName);
            tvSanName = itemView.findViewById(R.id.tvSanName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imgPose = itemView.findViewById(R.id.imgPose);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_yoga_pose, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        YogaPose pose = yogaPoseList.get(position);
        holder.tvEngName.setText(pose.engName);
        holder.tvSanName.setText(pose.sanName);
        holder.tvDescription.setText(pose.description);

        if (pose.imageLinks != null && !pose.imageLinks.isEmpty()) {
            // Load the first image (step1)
            String imageUrl = pose.imageLinks.get(0);

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.imgPose);
        }
    }

    @Override
    public int getItemCount() {
        return yogaPoseList.size();
    }
}