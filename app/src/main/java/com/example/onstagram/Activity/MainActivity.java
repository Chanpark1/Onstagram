package com.example.onstagram.Activity;

import static com.example.onstagram.Service.Service.IP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.onstagram.MainFragment.Fragment_main;
import com.example.onstagram.MainFragment.Fragment_profile;
import com.example.onstagram.MainFragment.Fragment_search;
import com.example.onstagram.NewPost.NewPost_image;
import com.example.onstagram.R;
import com.example.onstagram.SQLite.SQLite_User;
import com.example.onstagram.Service.Service;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Fragment frag_main;
    Fragment frag_search;
    Fragment frag_profile;

    SQLite_User user;

    SQLiteDatabase db;

    public String idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(IP +  " 씨발씨발씨발씨발");

        startService();

        frag_main = new Fragment_main();
        frag_profile = new Fragment_profile();
        frag_search = new Fragment_search();


        bottomNavigationView = findViewById(R.id.main_nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, frag_main).commitAllowingStateLoss();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,frag_main).commitAllowingStateLoss();
                        return true;
                    case R.id.search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, frag_search).commitAllowingStateLoss();
                        return true;

                    case R.id.new_post:
                        Intent intent = new Intent(MainActivity.this, NewPost_image.class);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,frag_profile).commitAllowingStateLoss();
                        return true;

                }
                return false;
            }
        });
    }

    private void startService() {

        Intent PostIntent = new Intent(this, Service.class)
                .setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startForegroundService(PostIntent);
    }

    public String idx() {

        user  = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);
        db = user.getReadableDatabase();

        idx =  user.getIdx(db);

        return idx;
    }

    // 서비스로부터 받아온 데이터 처리
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


}