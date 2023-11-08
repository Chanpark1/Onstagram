package com.example.onstagram.Activity;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;
import static com.example.onstagram.Service.Service.IP;
import static com.example.onstagram.Service.Service.PORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onstagram.Adapter.ProfilePostAdapter;
import com.example.onstagram.DTO.Profile;
import com.example.onstagram.DTO.ProfilePost;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.google.android.material.tabs.TabLayout;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileActivity extends AppCompatActivity {
    ImageView iv_isLocked;
    ImageView iv_profile_image;
    ImageView iv_profile_setting;

    ImageView iv_setting;

    TextView tv_username;
    TextView tv_post_num;
    TextView tv_follower_num;
    TextView tv_following_num;

    ImageView locked;
    TextView tv_locked;

    ImageView none_iv;
    TextView none_tv;

    TextView tv_name;
    TextView tv_comment;
    ViewPager2 viewPager2;
    TabLayout tab;

    Button message;
    Button follow;
    Button follow2;

    Button following_status;

    RecyclerView rv;

    ProfilePostAdapter adapter;

    final String BASIC = "http://43.200.84.107/basic/basic.jpg";

    Handler mHandler;

    Socket socket;

    public final String FOLLOW_ = "FOLLOW";

    String idx_master;
    String idx_visitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        setProfile();


        // FOLLOW
        setFollow_unlocked();

        setMessage();
    }


    private void init() {

        iv_profile_image = findViewById(R.id.Profile_image);
        iv_profile_setting = findViewById(R.id.Profile_dehaze);

        tv_username = findViewById(R.id.Profile_username);
        tv_post_num = findViewById(R.id.Profile_post_num);
        tv_follower_num = findViewById(R.id.Profile_follower_num);
        tv_following_num = findViewById(R.id.Profile_following_num);

        tv_name = findViewById(R.id.Profile_name);
        tv_comment = findViewById(R.id.Profile_introduce);

        locked = findViewById(R.id.Profile_locked);
        tv_locked = findViewById(R.id.Profile_locked_tv);

        none_iv = findViewById(R.id.Profile_none_iv);
        none_tv = findViewById(R.id.Profile_none_tv);

        message = findViewById(R.id.Profile_message);

        // 비공개 팔로우
        follow = findViewById(R.id.Profile_follow);

        // 공개 팔로우
        follow2 = findViewById(R.id.Profile_follow2);
        following_status = findViewById(R.id.Profile_follow_status);

        mHandler = new Handler();

        idx_master = getIntent().getStringExtra("idx_master");
        idx_visitor = getIntent().getStringExtra("idx_visitor");
    }

    private Socket socket_follow() {

        try {
            InetAddress serverAddr = InetAddress.getByName(IP);
            socket = new Socket(serverAddr, PORT);

        } catch (Exception e){
            e.printStackTrace();
        }

        return socket;
    }
    private void setProfile() {


        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);
        Call<Profile> call = retrofitAPI.get_others_profile(idx_master, idx_visitor);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if(response.body() != null && response.isSuccessful()) {
                    String username = response.body().getUsername();
                    String name = response.body().getName();
                    String profileImage = response.body().getProfile();
                    String content = response.body().getContent();
                    String follower_num = response.body().getFollower_num();
                    String following_num = response.body().getFollowing_num();
                    String post_num = response.body().getPost_num();
                    String isLocked = response.body().getIsLocked();
                    String isFollower = response.body().getIsFollower();

                    setData(username, name, profileImage, content, follower_num, following_num, post_num, isLocked, isFollower);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }
    private void setData(String username, String name, String profileImage, String content, String follower_num,
                         String following_num, String post_num, String isLocked, String isFollower) {

        tv_username.setText(username);

        if(profileImage == null) {
            Glide.with(this)
                    .load(BASIC)
                    .into(iv_profile_image);
        } else {
            Glide.with(this)
                    .load(profileImage)
                    .into(iv_profile_image);
        }
        tv_name.setText(name);

        if(content == null) {
            tv_comment.setVisibility(View.GONE);
        } else {
            tv_comment.setText(content);
        }

        tv_follower_num.setText(follower_num);
        tv_following_num.setText(following_num);
        tv_post_num.setText(post_num);

        if(isLocked.equals("0") && isFollower.equals("X")) {
            // 공개이고 팔로워가 아닐 때

            following_status.setVisibility(View.GONE);

            follow2.setVisibility(View.VISIBLE);

            setPost(idx_visitor, idx_master);

        } else if (isLocked.equals("1") && isFollower.equals("X")){
            // 비공개이고 팔로워가 아닐 때


            locked.setVisibility(View.VISIBLE);
            tv_locked.setVisibility(View.VISIBLE);

            message.setVisibility(View.GONE);
            following_status.setVisibility(View.GONE);

            follow.setVisibility(View.VISIBLE);
        } else if (isFollower.equals("O")) {
            //팔로워일 때

            locked.setVisibility(View.GONE);
            follow.setVisibility(View.GONE);
            follow2.setVisibility(View.GONE);

            following_status.setVisibility(View.VISIBLE);

            setPost(idx_visitor, idx_master);
        }

    }
    private void setSetting() {
        iv_setting.setOnClickListener(v -> {

        });

    }

    private void setPost(String idx_visitor, String idx_master) {

            RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

            Call<List<ProfilePost>> call = retrofitAPI.get_others_post(idx_master, idx_visitor);

            call.enqueue(new Callback<List<ProfilePost>>() {
                @Override
                public void onResponse(@NonNull Call<List<ProfilePost>> call, @NonNull Response<List<ProfilePost>> response) {

                    if(response.isSuccessful() && response.body() != null) {
                        generateDataList(response.body());
                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<ProfilePost>> call, @NonNull Throwable t) {
                    Log.d(TAG, t.getMessage());

                    none_iv.setVisibility(View.VISIBLE);
                    none_tv.setVisibility(View.VISIBLE);

                }
            });
    }

    private void setMessage() {
        message.setOnClickListener(v -> {
            Intent intent = new Intent(this, Chatting_private.class);
            intent.putExtra("idx",idx_master);
            startActivity(intent);
        });

    }
    private void generateDataList(List<ProfilePost> list) {

        GridLayoutManager manager = new GridLayoutManager(this, 3);

        rv = findViewById(R.id.Profile_rv);
        rv.setLayoutManager(manager);

        adapter = new ProfilePostAdapter(this, list);
        rv.setAdapter(adapter);
    }

    // 공개 계정 팔로우
    private void setFollow_unlocked() {
        follow2.setOnClickListener(v -> {

            new Thread() {
                @Override
                public void run() {
                    super.run();

                    try {

                        DataOutputStream dos = new DataOutputStream(socket_follow().getOutputStream());

                        // 소켓 서버에 팔로우 요청
                        // 보낸 계정과 받은 계정의 idx 값을 같이 보내줌
                        dos.writeUTF(idx_visitor + "@" + FOLLOW_ + "@" + idx_master);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        });

    }

}