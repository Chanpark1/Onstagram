package com.example.onstagram.MainFragment;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onstagram.Activity.EditProfile;
import com.example.onstagram.Adapter.ProfilePostAdapter;
import com.example.onstagram.DTO.Profile;
import com.example.onstagram.DTO.ProfilePost;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;
import com.example.onstagram.Setting.AccountSetting;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_profile extends Fragment {

    ImageView iv_isLocked;
    ImageView iv_profile;
    ImageView iv_setting;

    TextView tv_username;
    TextView tv_post_num;
    TextView tv_follower_num;
    TextView tv_following_num;

    TextView tv_name;
    TextView tv_comment;

    Button edit_profile;

    private View view;

    SQLite_User sqLite_user;

    SQLiteDatabase db;
    final String BASIC = "http://43.200.84.107/basic/basic.jpg";

    String idx;

    RecyclerView rv;

    ProfilePostAdapter adapter;
    GridLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        init();

        setProfile();

        setEdit_profile();

        setSetting();

        setPost();

        return view;
    }

    private void init() {

        iv_isLocked = view.findViewById(R.id.myProfile_lock);
        iv_profile = view.findViewById(R.id.myProfile_image);

        tv_username = view.findViewById(R.id.myProfile_username);
        tv_post_num = view.findViewById(R.id.myProfile_post_num);
        tv_follower_num = view.findViewById(R.id.myProfile_follower_num);
        tv_following_num = view.findViewById(R.id.myProfile_following_num);

        tv_name = view.findViewById(R.id.myProfile_name);
        tv_comment = view.findViewById(R.id.myProfile_introduce);
        iv_setting = view.findViewById(R.id.myProfile_dehaze);

        sqLite_user = new SQLite_User(view.getContext(), SQLite_User.tableName, null, SQLite_User.VERSION);
        db = sqLite_user.getReadableDatabase();

        idx = sqLite_user.getIdx(db);

    }

    private void setProfile() {

        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<Profile> call = retrofitAPI.get_my_profile(idx);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call,@NonNull Response<Profile> response) {
                if(response.body() != null && response.isSuccessful()) {
                    Profile profile = response.body();

                    String username = profile.getUsername();
                    String name = profile.getName();
                    String profileImage = profile.getProfile();
                    String content = profile.getContent();
                    String follower_num = profile.getFollower_num();
                    String following_num = profile.getFollowing_num();
                    String post_num = profile.getPost_num();
                    String isLocked = profile.getIsLocked();

                    setData(username, name, profileImage, content, follower_num, following_num,post_num, isLocked);

                    db.close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call,@NonNull Throwable t) {
                Log.d(TAG, t.getMessage());

            }
        });
    }

    private void setData(String username, String name, String profileImage, String content, String follower_num, String following_num, String post_num, String isLocked) {


        if(isLocked.equals("0")) {
            iv_isLocked.setVisibility(View.GONE);
        }

        if(profileImage == null) {
            Glide.with(view.getContext())
                    .load(BASIC)
                    .into(iv_profile);
        } else {
            Glide.with(view.getContext())
                    .load(profileImage)
                    .into(iv_profile);
        }

        if(content == null) {
            tv_comment.setVisibility(View.GONE);
        } else {
            tv_comment.setText(content);
        }

        tv_username.setText(username);
        tv_name.setText(name);

        tv_post_num.setText(post_num);

        tv_following_num.setText(following_num);
        tv_follower_num.setText(follower_num);

    }

    private void setPost() {

        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<List<ProfilePost>> call = retrofitAPI.get_profile_post(idx);

        call.enqueue(new Callback<List<ProfilePost>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProfilePost>> call, @NonNull Response<List<ProfilePost>> response) {
                if(response.body() != null && response.isSuccessful()) {

                    generateDataList(view.getContext(), response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProfilePost>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });


    }


    private void generateDataList(Context context, List<ProfilePost> list) {

        rv = view.findViewById(R.id.myProfile_rv);

        adapter = new ProfilePostAdapter(context, list);

        manager = new GridLayoutManager(context, 3);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
    }


    private void setEdit_profile() {
        edit_profile = view.findViewById(R.id.myProfile_edit_profile);

        edit_profile.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), EditProfile.class);

            startActivity(intent);
        });
    }

    private void setSetting() {
        iv_setting.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), AccountSetting.class);

            startActivity(intent);
        });
    }


}
