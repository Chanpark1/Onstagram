package com.example.onstagram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onstagram.DTO.ImagePath;
import com.example.onstagram.R;

import java.util.ArrayList;

public class PathSliderAdapter extends RecyclerView.Adapter<PathSliderAdapter.PathViewHolder> {

    Context context;

    ArrayList<ImagePath> list;

    public PathSliderAdapter(Context context, ArrayList<ImagePath> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PathSliderAdapter.PathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider, parent, false);

        return new PathViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PathSliderAdapter.PathViewHolder holder, int position) {

        String path = list.get(position).getImage();
        System.out.println(path);

        Glide.with(context)
                .load(path)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class PathViewHolder extends RecyclerView.ViewHolder{

        protected ImageView imageView;

        public PathViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.imageSlider);
        }
    }
}
