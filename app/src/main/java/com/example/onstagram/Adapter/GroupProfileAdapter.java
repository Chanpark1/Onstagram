package com.example.onstagram.Adapter;

import static com.example.onstagram.Adapter.SearchAdapter.BASIC;

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
import java.util.List;

public class GroupProfileAdapter extends RecyclerView.Adapter<GroupProfileAdapter.GroupProfileViewHolder> {

    Context context;
    ArrayList<String> list;

    public GroupProfileAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GroupProfileAdapter.GroupProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_rv_item, parent, false);

        return new GroupProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupProfileAdapter.GroupProfileViewHolder holder, int position) {
        String path = list.get(position);

        if(path == null) {
            Glide.with(context)
                    .load(BASIC)
                    .into(holder.iv);
        } else{
            Glide.with(context)
                    .load(path)
                    .into(holder.iv);

        }

    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class GroupProfileViewHolder extends RecyclerView.ViewHolder{

        protected ImageView iv;

        public GroupProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv = itemView.findViewById(R.id.chatting_list_rv_profile);
        }
    }
}
