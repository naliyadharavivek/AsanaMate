package com.asana.mate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

    Context context;
    String[] modes;
    String[] modesImages;
    private OnCategoryClickListener listener;

    public CategoryAdapter(Context context, String[] modes, String[] modesImages, OnCategoryClickListener listener) {

        this.context = context;
        this.modes = modes;
        this.modesImages = modesImages;
        this.listener = listener;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category, parent, false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView modeImage;
        TextView modeName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            modeImage = itemView.findViewById(R.id.custom_category_image);
            modeName = itemView.findViewById(R.id.custom_category_name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(modes[position].toLowerCase()); // "easy", "medium", "hard"
            }
        });


        Glide
                .with(context)
                .load(modesImages[position]).into(holder.modeImage);
        holder.modeName.setText(modes[position]);

    }

    @Override
    public int getItemCount() {
        return modes.length;
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(String mode);
    }

}
