package com.example.onstagram.NewPost;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onstagram.Adapter.ImageSliderAdapter;
import com.example.onstagram.DTO.User;
import com.example.onstagram.Activity.MainActivity;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPost_text extends AppCompatActivity {

    ImageView profile;

    EditText input_text;

    private LinearLayout indicator;

    ViewPager2 viewPager2;

    ArrayList<String> list = new ArrayList<>();

    FrameLayout frameLayout;

    ImageSliderAdapter adapter;

    TextView submit;

    TextView tv_username;


    String text = null;

    RetrofitAPI retrofit = getApiClient().create(RetrofitAPI.class);

    final String BASIC = "http://43.200.84.107/basic/basic.jpg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_text);

        setProfile();

        generateImage();

        setSubmit();
    }




    private void setCurrentIndicator(int position) {

        int childCount = indicator.getChildCount();

        for(int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) indicator.getChildAt(i);

            if(i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_inactive));
            }
        }
    }

    private void setIndicator(int count) {
        ImageView[] indicators = new ImageView[count];

        indicator = findViewById(R.id.new_post_text_indicator);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(16,8,16,8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_inactive));
            indicators[i].setLayoutParams(params);
            indicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);

    }

    private void generateImage() {

        Bundle extras = getIntent().getExtras();

        if(extras != null) {

            frameLayout = findViewById(R.id.new_post_text_frame);
            viewPager2 = findViewById(R.id.new_post_text_pager);

            list = getIntent().getStringArrayListExtra("image");

            adapter = new ImageSliderAdapter(this,list);
            viewPager2.setOffscreenPageLimit(1);
            viewPager2.setAdapter(adapter);

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    setCurrentIndicator(position);
                }
            });

            setIndicator(list.size());
        }
    }

    private void setSubmit() {
        input_text = findViewById(R.id.new_post_text_input);

        submit = findViewById(R.id.new_post_text_submit);

        submit.setOnClickListener(v -> {
            text = input_text.getText().toString();

            String count = String.valueOf(list.size());

            String idx = getIntent().getStringExtra("idx");


            HashMap<String, RequestBody> hashMap = new HashMap<>();

            RequestBody content_body = RequestBody.create(MediaType.parse("text/plain"), text);
            RequestBody pk_body = RequestBody.create(MediaType.parse("text/plain"), idx);
            RequestBody count_body = RequestBody.create(MediaType.parse("text/plain"),count);

            hashMap.put("idx", pk_body);
            hashMap.put("content", content_body);
            hashMap.put("count",count_body);

            ArrayList<MultipartBody.Part> files = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                String path = list.get(i);

                File file = new File(path);

                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"),file);

                MultipartBody.Part filePart = MultipartBody.Part.createFormData("uploaded_file" + i, file.getName(), fileBody);

                files.add(filePart);
            }

            Call<String> call = retrofit.upload_post(files, hashMap);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                    if(response.body() != null && response.isSuccessful()) {
                            Intent intent = new Intent(NewPost_text.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });

        });

    }

    private void setProfile() {

        profile = findViewById(R.id.new_post_text_profile);
        tv_username = findViewById(R.id.new_post_text_username);
//
//        db = sqLite_user.getReadableDatabase();

        String idx = getIntent().getStringExtra("idx");

        Call<User> call = retrofit.get_profile(idx);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,@NonNull Response<User> response) {
                if(response.body() != null && response.isSuccessful()) {
                    String path = response.body().getPath();
                    String username = response.body().getUsername();

                    tv_username.setText(username);

                    if(path == null) {
                        Glide.with(NewPost_text.this)
                                .load(BASIC)
                                .into(profile);
                    } else {
                        Glide.with(NewPost_text.this)
                                .load(path)
                                .into(profile);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

}