package com.example.onstagram.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onstagram.R;

public class Signup_profile_name extends AppCompatActivity {

    ImageView back;
    Button next;

    EditText input_name;

    TextView isThere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_profile_name);

        onClickNext();

        OnBackPressed();
    }

    private void onClickNext() {

        next = findViewById(R.id.profile_name_next);
        input_name = findViewById(R.id.profile_name_input);

        next.setOnClickListener(v -> {
            String phone = getIntent().getStringExtra("phone");
            String name = input_name.getText().toString();

            if(name.equals("")) {
                Toast.makeText(this, "이름을 입력 해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Signup_profile_name.this, Signup_profile_username.class);

                intent.putExtra("name",name);
                intent.putExtra("phone",phone);

                startActivity(intent);
            }

        });
    }

    private void OnBackPressed() {
        back = findViewById(R.id.profile_name_back);

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