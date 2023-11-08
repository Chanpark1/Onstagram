package com.example.onstagram.Signup;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class Verification_phone extends AppCompatActivity {

    EditText input_code;
    TextView tv_phrase;

    Button next;
    Button re_send;

    ImageView back;

    private String getCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_phone);

        Verification();

        Resend();

        onClick();

        OnBackPressed();

    }

    @SuppressLint("SetTextI18n")
    private void Verification() {


        re_send = findViewById(R.id.verification_re);
        tv_phrase = findViewById(R.id.verification_phrase);
        input_code = findViewById(R.id.verification_input);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
           getCode = getIntent().getStringExtra("verification");
           String phrase = getIntent().getStringExtra("phone");

           tv_phrase.setText(phrase + "로 인증 코드를 전송 했습니다.");
        }
    }

    private void onClick() {
        next = findViewById(R.id.verification_next);

        input_code = findViewById(R.id.verification_input);


        next.setOnClickListener(v -> {
            String str_code = input_code.getText().toString();
            if(!str_code.equals(getCode)) {
                Toast.makeText(this, "인증 번호를 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
            } else {

                RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);
                Call<String> call = retrofitAPI.signup_duplicatePhone(getIntent().getStringExtra("phone"));

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                        if(response.isSuccessful()) {
                            assert response.body() != null;
                            System.out.println(response.body());
                            if (response.body().equals("O")) {
                                Toast.makeText(Verification_phone.this, "이미 가입 된 회원 입니다.", Toast.LENGTH_SHORT).show();
                            } else {

                                Intent intent = new Intent(Verification_phone.this, Signup_profile_name.class);

                                intent.putExtra("phone",getIntent().getStringExtra("phone"));

                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {

                    }
                });

            }
        });
    }


    //인증 번호 재전송 메서드
    private void Resend() {
        re_send = findViewById(R.id.verification_re);

        re_send.setOnClickListener(v -> {

            Toast.makeText(this, "인증 번호가 재전송 되었습니다.", Toast.LENGTH_SHORT).show();

            RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);
            Call<String> call = retrofitAPI.verification(getIntent().getStringExtra("phone"));

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        getCode = response.body();
                        System.out.println("재전송 코드 : " + getCode);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });

        });

    }

    private void OnBackPressed() {
        back = findViewById(R.id.verification_back);

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