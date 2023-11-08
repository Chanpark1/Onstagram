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
import android.widget.TextView;
import android.widget.Toast;

import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccount extends AppCompatActivity {

    EditText input_phone;

    Button next;
    Button signup_email;

    TextView isThere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Verification();
    }

    private void Verification() {
        input_phone = findViewById(R.id.account_input_phone);

        next =  findViewById(R.id.account_next);

        next.setOnClickListener(v -> {
            String phone = input_phone.getText().toString();

            if(phone.equals("")) {
                Toast.makeText(this, "휴대폰 번호를 입력 해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Retrofit_call(phone);
            }
        });
    }

    private void Retrofit_call(String phone) {
        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<String> call = retrofitAPI.verification(phone);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(CreateAccount.this, Verification_phone.class);

                    intent.putExtra("verification",response.body());
                    intent.putExtra("phone",phone);

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
    }

    private void Signup_email() {
        signup_email = findViewById(R.id.account_email);

        signup_email.setOnClickListener(v -> {
            Intent intent = new Intent();
        });


    }
}