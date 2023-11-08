package com.example.onstagram.Adapter;

import static com.example.onstagram.Activity.Chatting_private.userImage;
import static com.example.onstagram.Adapter.SearchAdapter.BASIC;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onstagram.DTO.GroupMessage;
import com.example.onstagram.DTO.Message;
import com.example.onstagram.DTO.ViewType;
import com.example.onstagram.R;

import java.util.List;

public class GroupMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<GroupMessage> list;

    public GroupMessageAdapter(Context context, List<GroupMessage> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            switch(viewType) {
                case ViewType.RIGHT_CHAT :
                view = inflater.inflate(R.layout.from_message_rv, parent, false);
                return new RightViewHolder(view);

                case ViewType.LEFT_CHAT :
                    view = inflater.inflate(R.layout.to_message_group_rv, parent, false);
                    return new LeftViewHolder(view);

                default :
                return null;
            }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LeftViewHolder) {
            setLeftViewHolder((LeftViewHolder) holder,list,position);
        } else if (holder instanceof RightViewHolder) {
            setRightViewHolder((RightViewHolder) holder, list, position);
        }

    }

    private void setLeftViewHolder(@NonNull LeftViewHolder holder, List<GroupMessage> list, int i) {
        if(userImage != null) {
            Glide.with(holder.itemView.getContext())
                    .load(list.get(i).getProfileImage())
                    .into(holder.iv_profile);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(BASIC)
                    .into(holder.iv_profile);
        }

        holder.tv_message.setText(list.get(i).getContent());
    }

    private void setRightViewHolder(@NonNull RightViewHolder holder, List<GroupMessage> list, int i) {

        holder.tv_message.setText(list.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_username;
        protected ImageView iv_profile;
        protected TextView tv_message;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_username = itemView.findViewById(R.id.to_message_tv_group_username);
            this.iv_profile = itemView.findViewById(R.id.to_message_iv_group);
            this.tv_message = itemView.findViewById(R.id.to_message_tv_group);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_message;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_message = itemView.findViewById(R.id.from_message_tv);

        }
    }
}
