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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.onstagram.Adapter.ChatProfileAdapter;
import com.example.onstagram.DTO.User;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chatting_create_group extends AppCompatActivity {

    ImageView iv_back;
    EditText et_search;
    Button btn_submit;
    RecyclerView rv;
    LinearLayoutManager manager;

    ChatProfileAdapter adapter;

    List<User> list;

    SQLiteDatabase db;
    SQLite_User sqLite_user;

    RetrofitAPI retrofitAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_create_group);

        init();

        getProfile();

        set_submit();
    }

    private void init() {
        iv_back = findViewById(R.id.create_group_back);
        et_search = findViewById(R.id.create_group_search);
        btn_submit = findViewById(R.id.create_group_button);
        rv = findViewById(R.id.create_group_rv);

        ChatProfileAdapter.idx_list.add(getIdx());
    }

    private void getProfile() {
        retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<List<User>> call = retrofitAPI.load_chat_profile(getIdx());

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    generateDataList(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });


    }

    private void set_submit() {
        btn_submit.setOnClickListener(v -> {
            Intent intent;

            int size = ChatProfileAdapter.idx_list.size();

            if(size < 3) {
                intent = new Intent(this, Chatting_private.class);

                String idx = ChatProfileAdapter.idx_list.get(1);

                intent.putExtra("idx", idx);

                startActivity(intent);

                finish();
            } else {
                intent = new Intent(this, Chatting_group.class);

                intent.putStringArrayListExtra("idx_list", ChatProfileAdapter.idx_list);

                startActivity(intent);

                finish();
            }
        });
    }


    private void generateDataList(List<User> list) {
        manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new ChatProfileAdapter(this, list);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);

    }

    private String getIdx() {
        sqLite_user = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);
        db = sqLite_user.getReadableDatabase();

        return sqLite_user.getIdx(db);
    }


}