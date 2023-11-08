package com.example.onstagram.Signup;

import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup_profile_username extends AppCompatActivity {

    ImageView back;

    EditText input_username;

    Button next;

    TextView isThere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_profile_username);

        OnClick();

        OnBackPressed();

    }


    private void OnClick() {
        input_username = findViewById(R.id.profile_username_input);
        next =  findViewById(R.id.profile_username_next);

        next.setOnClickListener(v -> {
            String phone  = getIntent().getStringExtra("phone");
            String name = getIntent().getStringExtra("name");
            String username = input_username.getText().toString();



            if(username.equals("")) {
                Toast.makeText(this, "사용자 이름을 입력 해주세요.", Toast.LENGTH_SHORT).show();
            } else if(username.length() < 4) {
                Toast.makeText(this, "사용자 이름은 최소 4글자 이상으로 만들어주세요.", Toast.LENGTH_SHORT).show();
            } else {

                RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);
                Call<String> call = retrofitAPI.signup_duplicateUsername(username);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            if(response.body().equals("X")) {
                                Intent intent = new Intent(Signup_profile_username.this, Signup_profile_pw.class);

                                intent.putExtra("phone",phone);
                                intent.putExtra("name",name);
                                intent.putExtra("username",username);

                                startActivity(intent);
                            } else {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }

        });
    }


    private void OnBackPressed() {
        back = findViewById(R.id.profile_username_back);

        back.setOnClickListener(v -> {
            finish();
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}