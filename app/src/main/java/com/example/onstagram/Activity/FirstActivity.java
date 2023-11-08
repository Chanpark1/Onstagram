package com.example.onstagram.Activity;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;
import com.example.onstagram.Signup.CreateAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstActivity extends AppCompatActivity {

    EditText input_email;
    EditText input_pw;

    Button login;
    Button signup;

    TextView lost_pw;

    SQLite_User sqLite;
    SQLiteDatabase db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        sqLite = new SQLite_User(FirstActivity.this, SQLite_User.tableName, null, 1);
        db = sqLite.getWritableDatabase();

        sqLite.createTable(db);

//        db.execSQL("DROP TABLE " + SQLite_User.tableName);
        Login();

        lost_password();

        Signup();

        autoLogin();


    }

    private void Login() {

        login = (Button) findViewById(R.id.first_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_email = findViewById(R.id.first_input_email);

                input_pw = findViewById(R.id.first_input_pw);

                String email = input_email.getText().toString();
                String pw = input_pw.getText().toString();

                if(email.equals("")) {
                    Toast.makeText(FirstActivity.this, "이메일, 유저네임 또는 휴대폰 번호를 입력 해주세요.", Toast.LENGTH_SHORT).show();
                } else if (pw.equals("")) {
                    Toast.makeText(FirstActivity.this, "비밀번호를 입력 해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    RetrofitAPI retrofit = getApiClient().create(RetrofitAPI.class);

                    Call<String> call = retrofit.login(email, pw);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if(response.isSuccessful() && response.body() != null) {
                                if(response.body().equals("X")) {
                                    Toast.makeText(FirstActivity.this, "이메일 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                } else {
                                    sqLite = new SQLite_User(FirstActivity.this, SQLite_User.tableName, null, SQLite_User.VERSION);

                                    Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                                    setSqLite(response.body());
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Log.d(TAG, t.getMessage());
                        }
                    });

                }
            }
        });

    }

    private void autoLogin() {
        sqLite = new SQLite_User(FirstActivity.this, SQLite_User.tableName, null, SQLite_User.VERSION);
        db = sqLite.getReadableDatabase();

        String sql = "SELECT * FROM User ";

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.getCount() != 0) {
            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        cursor.close();

    }

    private void lost_password() {
        lost_pw = findViewById(R.id.first_lost_pw);

        lost_pw.setOnClickListener(v -> {
//            Intent intent = new Intent();
            setContentView(R.layout.activity_main);
        });

    }

    private void Signup() {
        signup = findViewById(R.id.first_create_account);

        signup.setOnClickListener(v -> {
            Intent intent = new Intent(FirstActivity.this, CreateAccount.class);

            startActivity(intent);
        });
    }

    private void setSqLite(String idx) {
        sqLite = new SQLite_User(FirstActivity.this, SQLite_User.tableName,null,SQLite_User.VERSION);
        db = sqLite.getReadableDatabase();
        sqLite.createTable(db);

        String sql_duplicate = "SELECT id FROM " + SQLite_User.tableName + " WHERE idx = '" + idx + "'";

        Cursor cursor_duplicate = db.rawQuery(sql_duplicate, null);

        String sql_check = "SELECT id FROM " + SQLite_User.tableName;

        Cursor cursor_check =  db.rawQuery(sql_check, null);

        if(cursor_duplicate.getCount() != 0) {
            cursor_duplicate.close();
            cursor_check.close();
        } else if (cursor_check.getCount() != 0) {

            db.execSQL("DELETE FROM " + SQLite_User.tableName);

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