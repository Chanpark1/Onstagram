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
import com.example.onstagram.DTO.Message;
import com.example.onstagram.DTO.ViewType;
import com.example.onstagram.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Message> list;



    public MessageAdapter(Context context, List<Message> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
                case ViewType.RIGHT_CHAT :
                view = inflater.inflate(R.layout.from_message_rv, parent, false);
                return new RightViewHolder(view);

                case ViewType.LEFT_CHAT :
                    view = inflater.inflate(R.layout.to_message_rv, parent, false);
                    return new LeftViewHolder(view);
                default:
                return null;

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LeftViewHolder) {
            setLeftViewHolder((LeftViewHolder) holder, userImage, list, position);

        } else if (holder instanceof RightViewHolder) {
            setRightViewHolder((RightViewHolder) holder, list, position);

        }

    }

    private void setLeftViewHolder(@NonNull LeftViewHolder holder, String userImage, List<Message> list, int i) {
        if(userImage != null) {
            Glide.with(holder.itemView.getContext())
                    .load(userImage)
                    .into(holder.to_image);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(BASIC)
                    .into(holder.to_image);
        }

        holder.tv_message_to.setText(list.get(i).getContent());
    }

    private void setRightViewHolder(@NonNull RightViewHolder holder, List<Message> list, int i) {
        if(userImage != null) {
            Glide.with(holder.itemView.getContext())
                    .load(userImage)
                    .into(holder.from_image);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(BASIC)
                    .into(holder.from_image);
        }

        holder.tv_message_from.setText(list.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_message_from;
        protected ImageView from_image;



        public RightViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_message_from = itemView.findViewById(R.id.from_message_tv);
            this.from_image = itemView.findViewById(R.id.from_image);

        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_message_to;
        protected ImageView to_image;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_message_to = itemView.findViewById(R.id.to_message_tv);
            this.to_image = itemView.findViewById(R.id.to_message_iv);

        }
    }

}
