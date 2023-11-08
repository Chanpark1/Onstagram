package com.example.onstagram.Setting;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setting_lock extends AppCompatActivity {

    SwitchCompat switchCompat;
    boolean isLocked;
    String val;
    String origin;
    String idx;
    ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_lock);

        init();

        setSwitchCompat();

        onBack();
    }

    private void init() {
        switchCompat = findViewById(R.id.setting_lock_switch);
        iv_back = findViewById(R.id.setting_lock_back);

        idx = getIntent().getStringExtra("idx");

    }

    private void setSwitchCompat() {

        String idx = getIntent().getStringExtra("idx");

        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<String> call = retrofitAPI.check_lock(idx);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.body() != null && response.isSuccessful()) {
                    val = response.body();
                    origin = response.body();

                    if(val.equals("1")) {
                        isLocked = true;
                    } else {
                        isLocked = false;
                    }

                    switchCompat.setChecked(isLocked);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 1 == locked ? 0 == unLocked
            if(isChecked) {
                val = "1";

            } else {
                val = "0";

            }

        });
    }

    private void onBack() {
        iv_back.setOnClickListener(v -> {
            if(origin.equals(val)) {
                finish();
            } else {
                RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

                Call<String> call = retrofitAPI.update_lock(idx, val);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d(TAG, t.getMessage());
                    }
                });
            }

        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onBack();
    }
}