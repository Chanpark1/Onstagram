package com.example.onstagram.Signup;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup_profile_email extends AppCompatActivity {

    Button next;
    Button skip;

    ImageView back;

    EditText input_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_profile_email);

        next_onClick();

        skip_onClick();

    }

    private void next_onClick() {

        input_email = findViewById(R.id.profile_email_input);

        next = findViewById(R.id.profile_email_next);

        next.setOnClickListener(v -> {
            String email = input_email.getText().toString();

            if(email.equals("")) {
                Toast.makeText(this, "이메일 주소를 입력 해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Retrofit_code();
            }
        });
    }

    private void Retrofit_code() {
        input_email = findViewById(R.id.profile_email_input);

        String email = input_email.getText().toString();

        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<String> call = retrofitAPI.verification_email(email);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(Signup_profile_email.this, Verification_email.class);

                    intent.putExtra("email",email);
                    intent.putExtra("idx", getIntent().getStringExtra("idx"));
                    intent.putExtra("code",response.body());

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }


    private void skip_onClick() {
        skip = findViewById(R.id.profile_email_skip);

        skip.setOnClickListener(v -> {
            Intent intent = new Intent(Signup_profile_email.this, Signup_profile_image.class);
            intent.putExtra("idx", getIntent().getStringExtra("idx"));

            startActivity(intent);
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}