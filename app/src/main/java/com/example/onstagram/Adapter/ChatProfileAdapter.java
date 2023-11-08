package com.example.onstagram.Adapter;


import static com.example.onstagram.Adapter.SearchAdapter.BASIC;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onstagram.DTO.User;
import com.example.onstagram.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatProfileAdapter extends RecyclerView.Adapter<ChatProfileAdapter.ChatProfileViewHolder> {

    Context context;
    List<User> list;

    public static ArrayList<String> idx_list = new ArrayList<>();

    public ChatProfileAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatProfileAdapter.ChatProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_group_rv_item, parent, false);

        return new ChatProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatProfileAdapter.ChatProfileViewHolder holder, int position) {

        String path = list.get(position).getPath();
        String name = list.get(position).getName();
        String username = list.get(position).getUsername();


        setProfile(holder, path, name, username, context);

        set_submit(holder, position);
    }

    private void setProfile(@NonNull ChatProfileAdapter.ChatProfileViewHolder holder, @Nullable String path, @Nullable String name, String username, Context context) {
        if(path == null) {
            Glide.with(context)
                    .load(BASIC)
                    .into(holder.iv_profile);
        } else {
            Glide.with(context)
                    .load(path)
                    .into(holder.iv_profile);
        }

        holder.tv_username.setText(username);
        if(name == null) {
            holder.tv_name.setVisibility(View.GONE);
        } else {
            holder.tv_name.setText(name);
        }

    }

    private void set_submit(@NonNull ChatProfileAdapter.ChatProfileViewHolder holder, int position) {
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.cb.isChecked()) {
                    String idx = list.get(position).getIdx();

                    idx_list.add(idx);
                    System.out.println(String.valueOf(idx_list.size()));
                } else {
                    String idx = list.get(position).getIdx();

                    idx_list.removeIf(item -> item.equals(idx));

                    Collections.sort(idx_list);

                    System.out.println(String.valueOf(idx_list.size()));
                }



            }
        });
    }


    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class ChatProfileViewHolder extends RecyclerView.ViewHolder{

        protected ImageView iv_profile;
        protected TextView tv_name;
        protected TextView tv_username;
        protected CheckBox cb;
        public ChatProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.create_group_rv_profile);
            this.tv_name = itemView.findViewById(R.id.create_group_rv_name);
            this.tv_username = itemView.findViewById(R.id.create_group_rv_username);
            this.cb = itemView.findViewById(R.id.create_group_rv_cb);
        }
    }
}
