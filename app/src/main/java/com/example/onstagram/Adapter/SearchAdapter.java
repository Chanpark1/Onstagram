package com.example.onstagram.Adapter;

import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onstagram.Activity.MainActivity;
import com.example.onstagram.Activity.ProfileActivity;
import com.example.onstagram.DTO.Others;
import com.example.onstagram.DTO.Profile;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;

import java.lang.annotation.Repeatable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;
    List<Profile> list;

    final static String BASIC = "http://43.200.84.107/basic/basic.jpg";

    SQLite_User sqLite_user;
    SQLiteDatabase db;

    public SearchAdapter(Context context, List<Profile> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_search_item, parent, false);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {

        String path = list.get(position).getProfile();
        String username = list.get(position).getUsername();
        String name = list.get(position).getName();

        if(path != null) {
            Glide.with(holder.itemView.getContext())
                    .load(path)
                    .into(holder.image);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(BASIC)
                    .into(holder.image);
        }

        holder.username.setText(username);
        holder.name.setText(name);

        holder.itemView.setOnClickListener(view -> {

            String idx = list.get(position).getIdx();

            RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

            Call<Others> call = retrofitAPI.check_others(idx, getIdx(holder));

            call.enqueue(new Callback<Others>() {
                @Override
                public void onResponse(@NonNull Call<Others> call, @NonNull Response<Others> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(holder.image.getContext(), ProfileActivity.class);

                        intent.putExtra("idx_master", idx);
                        intent.putExtra("idx_visitor", getIdx(holder));
                        intent.putExtra("isLocked", response.body().getIsLocked());
                        intent.putExtra("isFollower", response.body().getIsFollower());

                        view.getContext().startActivity(intent);

                    }

                }

                @Override
                public void onFailure(@NonNull Call<Others> call, @NonNull Throwable t) {

                }
            });
        });

    }

    private String getIdx(SearchViewHolder holder) {
        sqLite_user = new SQLite_User(holder.itemView.getContext(), SQLite_User.tableName, null, SQLite_User.VERSION );
        db = sqLite_user.getReadableDatabase();

        return sqLite_user.getIdx(db);
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView username;
        protected TextView name;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            this.image = itemView.findViewById(R.id.rv_search_image);
            this.username = itemView.findViewById(R.id.rv_search_username);
            this.name = itemView.findViewById(R.id.rv_search_name);
        }
    }
}
