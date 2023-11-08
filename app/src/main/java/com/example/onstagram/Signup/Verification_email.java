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
import android.widget.TextView;
import android.widget.Toast;

import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verification_email extends AppCompatActivity {

    EditText input_code;
    TextView tv_phrase;

    Button next;
    Button re_send;

    ImageView back;

    private String getCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_email);

        Verification();

        onClick();

        setRe_send();

    }

    private void Verification() {
        re_send = findViewById(R.id.email_verification_re);
        tv_phrase = findViewById(R.id.email_verification_phrase);
        input_code = findViewById(R.id.email_verification_input_number);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            getCode = getIntent().getStringExtra("verification");
            String phrase = getIntent().getStringExtra("email");

            tv_phrase.setText(phrase + "로 인증 코드를 전송 했습니다.");
        }
    }

    private void onClick() {
        input_code = findViewById(R.id.email_verification_input_number);
        next = findViewById(R.id.email_verification_next);

        next.setOnClickListener(v -> {
            Bundle extras = getIntent().getExtras();

            if(extras != null) {
                getCode = getIntent().getStringExtra("code");

                String code = input_code.getText().toString();

                if(!getCode.equals(code)) {
                    Toast.makeText(this, "인증번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String idx = getIntent().getStringExtra("idx");
                    String email = getIntent().getStringExtra("email");

                    Retrofit_update(idx, email);
                }
            }
        });
    }

    private void setRe_send() {
        re_send = findViewById(R.id.email_verification_re);

        re_send.setOnClickListener(v -> {
            Retrofit_resend();
        });

    }

    private void Retrofit_resend() {
        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);
        Call<String> call = retrofitAPI.verification_email(getIntent().getStringExtra("email"));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Verification_email.this, "인증번호가 재전송 되었습니다.", Toast.LENGTH_SHORT).show();
                    
                    getCode = response.body();
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }


    private void Retrofit_update(String idx, String email) {
        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<String> call = retrofitAPI.signup_email(idx,email);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().equals("Success")) {
                        Toast.makeText(Verification_email.this, "인증이 완료 되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Verification_email.this, Signup_profile_image.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("idx",idx);

                        startActivity(intent);
                    } else if (response.body().equals("Duplicate")) {
                        Toast.makeText(Verification_email.this, "이미 사용중인 이메일 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }


}