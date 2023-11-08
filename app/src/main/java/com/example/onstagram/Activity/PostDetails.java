package com.example.onstagram.Activity;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onstagram.Adapter.PostAdapter;
import com.example.onstagram.DTO.Post;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetails extends AppCompatActivity {

    ImageView iv_back;

    TextView tv_logo;

    RecyclerView rv;
    LinearLayoutManager manager;
    PostAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        init();

        setPost();

    }
    private void init() {
        iv_back = findViewById(R.id.post_details_back);
        tv_logo = findViewById(R.id.post_details_logo);
        rv = findViewById(R.id.post_details_rv);
    }
    private void setPost() {
        String idx = getIntent().getStringExtra("idx");

        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<ArrayList<Post>> call = retrofitAPI.get_post(idx);

        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Post>> call, @NonNull Response<ArrayList<Post>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    generateDataList(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Post>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
    private void generateDataList(ArrayList<Post> list) {

        adapter = new PostAdapter(this, list);
        manager = new LinearLayoutManager(this);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
    }
}