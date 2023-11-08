package com.example.onstagram.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onstagram.Activity.FirstActivity;
import com.example.onstagram.R;
import com.example.onstagram.SQLite.SQLite_User;

public class AccountSetting extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_logout;

    ConstraintLayout const_lock;
    ConstraintLayout const_block;
    ConstraintLayout const_saved;
    Intent intent;

    SQLite_User sqLite_user;
    SQLiteDatabase db;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        init();

        setOnClick();

        setLogout();

    }

    private void init() {
        iv_back = findViewById(R.id.account_setting_back);
        tv_logout = findViewById(R.id.account_setting_logout);

        const_lock = findViewById(R.id.account_setting_lock);
        const_block = findViewById(R.id.account_setting_block);
        const_saved = findViewById(R.id.account_setting_saved);

        sqLite_user = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);

        db = sqLite_user.getReadableDatabase();
    }

    private void setOnClick() {
        iv_back.setOnClickListener(v -> {

        });

        tv_logout.setOnClickListener(v -> {

        });

        const_lock.setOnClickListener(v -> {
            intent = new Intent(this, Setting_lock.class);

            intent.putExtra("idx", getIdx());

            startActivity(intent);

        });

        const_block.setOnClickListener(v -> {
            intent = new Intent(this, Setting_blocked.class);

            intent.putExtra("idx",getIdx());

            startActivity(intent);
        });

        const_saved.setOnClickListener(v -> {
        });

    }

    private void setLogout() {
        tv_logout.setOnClickListener(v -> {
            setDialog();
        });
    }

    private void setDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);

        dialog.show();

        Button logout_btn = dialog.findViewById(R.id.dialog_logout_logout);
        Button cancel_btn = dialog.findViewById(R.id.dialog_logout_cancel);

        logout_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, FirstActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            db.execSQL("DROP TABLE " + SQLite_User.tableName);

            startActivity(intent);

        });

        cancel_btn.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }


    private String getIdx() {

        return sqLite_user.getIdx(db);
    }

    @Override
    protected void onDestroy() {
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}