package com.example.onstagram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onstagram.R;

import java.util.ArrayList;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder> {

    Context context;

    ArrayList<String> list;

    public ImageSliderAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageSliderAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider, parent, false);


        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderAdapter.SliderViewHolder holder, int position) {
        String uri = list.get(position);

        Glide.with(context)
                .load(uri)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.imageSlider);
        }
    }
}
