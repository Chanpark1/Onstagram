package com.example.onstagram.Signup;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup_profile_pw extends AppCompatActivity {

    ImageView back;

    EditText input_pw;

    Button next;

    SQLite_User sqLite;


    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_profile_pw);

        OnClick();
    }
    private void OnClick() {
        input_pw = findViewById(R.id.profile_pw_input);
        next = findViewById(R.id.profile_pw_next);

        next.setOnClickListener(v -> {
            String username = getIntent().getStringExtra("username");
            String name = getIntent().getStringExtra("name");
            String phone = getIntent().getStringExtra("phone");

            String pw = input_pw.getText().toString();

            if(pw.equals("")) {
                Toast.makeText(this, "비밀번호를 입력 해주세요.", Toast.LENGTH_SHORT).show();
            } else if(pw.length() < 6) {
                Toast.makeText(this, "비밀번호는 6자 이상으로 만들어주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Retrofit_update(phone, name , username, pw);
            }
        });
    }

    private void Retrofit_update(String phone, String name, String username, String pw) {

        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<String> call = retrofitAPI.signup_phone(phone, name, username, pw);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Signup_profile_pw.this, "가입이 완료 되었습니다!", Toast.LENGTH_SHORT).show();

                    setSqLite(response.body());

                    Intent intent  = new Intent(Signup_profile_pw.this, Signup_profile_email.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra("idx",response.body());

                    startActivity(intent);
                    finish();

                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
    private void setSqLite(String idx) {
        sqLite = new SQLite_User(Signup_profile_pw.this, SQLite_User.tableName, null, SQLite_User.VERSION);
        db = sqLite.getWritableDatabase();
        sqLite.createTable(db);

        String sql_duplicate = "SELECT id FROM " + SQLite_User.tableName + " WHERE idx = '" + idx + "'";

        Cursor cursor_duplicate = db.rawQuery(sql_duplicate, null);

        String sql_check = "SELECT id FROM " + SQLite_User.tableName;

        Cursor cursor_check = db.rawQuery(sql_check, null);

        if(cursor_duplicate.getCount() != 0) {
            cursor_duplicate.close();
            cursor_check.close();

           // 중복되는 키 값이라면 아무것도 하지 않음.

        } else if(cursor_check.getCount() != 0){

            db.execSQL("DELETE FROM " + SQLite_User.tableName);

            // 다른 키 값이 저장되어 있으면 테이블을 비우고 다시 INSERT 함
            // 혹시 모른 예외 처리
            sqLite.insertUser(db, idx);

            cursor_check.close();
            cursor_duplicate.close();
        } else {
            sqLite.insertUser(db, idx);

            cursor_check.close();
            cursor_duplicate.close();

        }

    }


}