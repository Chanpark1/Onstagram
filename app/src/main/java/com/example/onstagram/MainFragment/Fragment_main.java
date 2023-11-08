package com.example.onstagram.MainFragment;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onstagram.Activity.ChattingList;
import com.example.onstagram.Adapter.PostAdapter;
import com.example.onstagram.DTO.Post;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_main extends Fragment {

    private View view;

    SQLite_User sqlite;
    SQLiteDatabase db;

    ArrayList<Post> list = new ArrayList<>();

    RecyclerView rv;

    PostAdapter adapter;

    LinearLayoutManager manager;

    ImageButton ib_chat;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container,false);

        init();

        setPost();

        set_chat();

        return view;
    }



    private void init() {
        ib_chat = view.findViewById(R.id.frag_main_chat);
    }

    private void set_chat() {
        ib_chat.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), ChattingList.class);
            intent.putExtra("idx", getIdx());
            startActivity(intent);
        });
    }

    private void setPost() {


        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<ArrayList<Post>> call = retrofitAPI.get_main_post(getIdx());

        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Post>> call,@NonNull Response<ArrayList<Post>> response) {
                if(response.isSuccessful() && response.body() != null) {

                    generateDataList(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Post>> call,@NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }

    private String getIdx() {
        sqlite = new SQLite_User(view.getContext(),SQLite_User.tableName,null,SQLite_User.VERSION);

        db = sqlite.getReadableDatabase();

        return sqlite.getIdx(db);
    }

    private void generateDataList(ArrayList<Post> list) {
        rv = view.findViewById(R.id.frag_main_post);

        manager = new LinearLayoutManager(view.getContext());

        adapter = new PostAdapter(view.getContext(), list);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
    }
}
