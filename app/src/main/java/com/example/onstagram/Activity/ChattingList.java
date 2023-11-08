package com.example.onstagram.Activity;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onstagram.Adapter.ChattingListAdapter;
import com.example.onstagram.DTO.User;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingList extends AppCompatActivity {

    ImageView iv_back;
    ImageView iv_add;

    TextView tv_username;

    EditText et_search;

    RecyclerView rv;

    ChattingListAdapter adapter;

    LinearLayoutManager manager;

    ArrayList<com.example.onstagram.DTO.ChattingList> list;

    RetrofitAPI retrofitAPI;

    SQLite_User sqLite_user;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);

        init();

        get_my_profile();

//        load_chatting_list();
//
        setIv_add();
    }

    private void init() {

        iv_add = findViewById(R.id.chatting_add);
        iv_back = findViewById(R.id.chatting_back);
        tv_username = findViewById(R.id.chatting_username);
        et_search = findViewById(R.id.chatting_search);
        rv = findViewById(R.id.chatting_list_rv);

        adapter = new ChattingListAdapter(this, list);

        manager = new LinearLayoutManager(this);

        retrofitAPI = getApiClient().create(RetrofitAPI.class);

        sqLite_user = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);

        db = sqLite_user.getReadableDatabase();
    }

    private void setIv_add() {

        iv_add.setOnClickListener(v -> {

            Intent intent = new Intent(this, Chatting_create_group.class);

            startActivity(intent);
        });

    }



    private void get_my_profile() {
        Call<User> call = retrofitAPI.get_profile(getIdx());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.body() != null && response.isSuccessful()) {
                    tv_username.setText(response.body().getUsername());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

            }
        });
    }

    private void load_chatting_list() {
        Call<ArrayList<com.example.onstagram.DTO.ChattingList>> call = retrofitAPI.load_chatting_list(getIdx());

        call.enqueue(new Callback<ArrayList<com.example.onstagram.DTO.ChattingList>>() {
            @Override
            public void onResponse( @NonNull Call<ArrayList<com.example.onstagram.DTO.ChattingList>> call, @NonNull Response<ArrayList<com.example.onstagram.DTO.ChattingList>> response) {
                if(response.isSuccessful() && response.body() != null) generateDataList(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<com.example.onstagram.DTO.ChattingList>> call, @NonNull  Throwable t) {
                Log.d(TAG, t.getMessage());

            }
        });

    }

    private void generateDataList(ArrayList<com.example.onstagram.DTO.ChattingList> list) {

        adapter = new ChattingListAdapter(this, list);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
    }

    private String getIdx() {
        return sqLite_user.getIdx(db);
    }
}