package com.example.onstagram.Adapter;

import static com.example.onstagram.Adapter.SearchAdapter.BASIC;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onstagram.Activity.Chatting_group;
import com.example.onstagram.Activity.Chatting_private;
import com.example.onstagram.DTO.ChattingList;
import com.example.onstagram.DTO.Message;
import com.example.onstagram.DTO.User;
import com.example.onstagram.R;

import java.util.ArrayList;
import java.util.List;

public class ChattingListAdapter extends RecyclerView.Adapter<ChattingListAdapter.ChatListViewHolder> {

    Context context;

    ArrayList<ChattingList> list;
    List<User> user_list;

    ArrayList<String> profile_list = new ArrayList<>();

    GroupProfileAdapter adapter;

    GridLayoutManager manager;

    public ChattingListAdapter(Context context, ArrayList<ChattingList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChattingListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_list_rv, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingListAdapter.ChatListViewHolder holder, int position) {
        int members = list.get(position).getCount_members();

        for(int i = 0; i < members; i++) {
            String profile_path = list.get(position).getList().get(position).getPath();

            if(profile_path != null) {
                profile_list.add(profile_path);
            }

        }

        setImage(holder, context, position, profile_list);

        setText(holder, position,list);

    }

    private void setText(@NonNull ChattingListAdapter.ChatListViewHolder holder, int position, List<ChattingList> list){

        user_list = list.get(position).getList();

        String message = list.get(position).getMessage();
        String username = user_list.get(position).getUsername();
        String name = user_list.get(position).getName();

        holder.tv_message.setText(message);

        if(username != null) {
            holder.tv_username.setText(username);
        } else {
            holder.tv_username.setText(name);
        }

    }

    private void setImage(ChattingListAdapter.ChatListViewHolder holder, Context context, int position, ArrayList<String> profile_list) {

        int size = list.get(position).getCount_members();

        if(size > 2) {
            adapter = new GroupProfileAdapter(context, profile_list);

            manager = new GridLayoutManager(context, 2);

            holder.iv_profile.setVisibility(View.GONE);

            holder.rv_group_profile.setVisibility(View.VISIBLE);

            holder.rv_group_profile.setAdapter(adapter);

            holder.rv_group_profile.setLayoutManager(manager);

        } else {
            if(list.get(position).getList().get(position).getPath() == null ) {
                Glide.with(context)
                        .load(BASIC)
                        .into(holder.iv_profile);
            } else{
                Glide.with(context)
                        .load(list.get(position).getList().get(position).getPath())
                        .into(holder.iv_profile);
            }
        }



    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder{

        protected ImageView iv_profile;
        protected TextView tv_username;
        protected TextView tv_message;
        protected RecyclerView rv_group_profile;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.iv_profile = itemView.findViewById(R.id.chatting_list_rv_image);
            this.tv_username = itemView.findViewById(R.id.chatting_list_rv_username);
            this.tv_message = itemView.findViewById(R.id.chatting_list_rv_message);
            this.rv_group_profile = itemView.findViewById(R.id.chatting_list_rv_group_profile);
        }
    }
}
