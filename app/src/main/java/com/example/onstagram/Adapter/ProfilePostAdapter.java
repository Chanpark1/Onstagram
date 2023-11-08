package com.example.onstagram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onstagram.Activity.PostDetails;
import com.example.onstagram.DTO.ProfilePost;
import com.example.onstagram.R;

import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ProfilePostViewHolder> {

    Context context;

    List<ProfilePost> list;

    public ProfilePostAdapter(Context context, List<ProfilePost> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post, parent, false);

        return new ProfilePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostViewHolder holder, int position) {

        String path = list.get(position).getThumbnail();

        String postIdx = list.get(position).getPostIdx();

        String image_num = list.get(position).getImage_num();

        String idx = list.get(position).getIdx();

        int num = Integer.parseInt(image_num);


        if(path != null) {
            Glide.with(holder.itemView.getContext())
                    .load(path)
                    .into(holder.imageView);
        }

        if(num  > 1) {
            holder.icon.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(holder.itemView.getContext(), PostDetails.class);

            intent.putExtra("idx", idx);

            holder.itemView.getContext().startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class ProfilePostViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView icon;

        public ProfilePostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.post_profile_iv);
            this.icon = itemView.findViewById(R.id.post_profile_icon);

        }
    }
}
