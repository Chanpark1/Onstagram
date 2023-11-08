package com.example.onstagram.Activity;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDetails extends AppCompatActivity {

    String type;

    String value;

    TextView tv_cancel;
    TextView tv_logo;
    TextView tv_submit;
    TextView tv_var;
    TextView tv_info;

    EditText input_text;

    ImageView iv_clear;

    SQLite_User sqLite_user;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        init();

        setUI();

        clearText();

        setSubmit(getIdx());

        setQuit();
    }

    private void init() {
        tv_cancel = findViewById(R.id.edit_details_quit);
        tv_logo = findViewById(R.id.edit_details_logo);
        tv_submit = findViewById(R.id.edit_details_submit);
        tv_var = findViewById(R.id.edit_details_guide);
        tv_info = findViewById(R.id.edit_details_info);

        input_text = findViewById(R.id.edit_details_input);

        iv_clear = findViewById(R.id.edit_details_clear);
    }

    private void setUI() {
        Intent intent = getIntent();

        type = intent.getStringExtra("type");

        switch (type) {
            case "name":
                tv_logo.setText("이름");
                tv_var.setText("이름");

                tv_info.setText("이름은 7일에 한 번만 변경할 수 있습니다.");

                value = intent.getStringExtra("name");

                input_text.setText(value);

                break;
            case "username":
                tv_logo.setText("사용자 이름");
                tv_var.setText("사용자 이름");

                tv_info.setText("사용자 이름은 7일 안에 한 번만 변경할 수 있습니다.");

                value = intent.getStringExtra("username");

                input_text.setText(value);

                break;
            case "intro":
                tv_logo.setText("소개");
                tv_var.setVisibility(View.GONE);
                tv_info.setVisibility(View.GONE);

                value = intent.getStringExtra("intro");

                input_text.setText(value);
                break;
        }

    }

    private void clearText() {
        iv_clear.setOnClickListener(v -> {
            if(input_text.getText() != null) {
                input_text.setText("");
            }
        });
    }

    private void setSubmit(String idx) {
        tv_submit.setOnClickListener(v -> {

            value = input_text.getText().toString();

            type = getIntent().getStringExtra("type");

            RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

            Call<String> call = retrofitAPI.update_profile(idx, value, type);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(EditDetails.this, EditProfile.class);

                        startActivity(intent);
                        finish();
                    } else if (response.isSuccessful() && response.body().equals("X")) {
                        Toast.makeText(EditDetails.this, "이름은 일주일에 한번 변경할 수 있습니다.", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
        });

    }

    private void setQuit() {
        tv_cancel.setOnClickListener(v -> {
            Intent intent = new Intent(EditDetails.this, EditProfile.class);

            startActivity(intent);
            finish();

        });
    }


    private String getIdx() {
        sqLite_user = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);

        db = sqLite_user.getReadableDatabase();

        return sqLite_user.getIdx(db);
    }


}