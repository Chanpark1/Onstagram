package com.example.onstagram.NewPost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.provider.MediaStore;

import android.widget.TextView;
import android.widget.Toast;


import com.example.onstagram.Adapter.GalleryAdapter;
import com.example.onstagram.R;
import com.example.onstagram.SQLite.SQLite_User;


public class NewPost_image extends AppCompatActivity {

    RecyclerView recyclerView;
    GalleryAdapter adapter;
    TextView next;

    boolean hasCamPerm;
    boolean hasWritePerm;

    SQLite_User sqLite_user;

    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        recyclerView = findViewById(R.id.gallery_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        adapter = new GalleryAdapter(this,null);


        recyclerView.setAdapter(adapter);

        loadImage();

        setNext();
    }


    private void loadImage() {
//        hasCamPerm = checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        hasWritePerm = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(!hasWritePerm) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        LoaderManager.getInstance(this).initLoader(0, null, mLoaderCallbacks);
    }

    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            String[] projection = {
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME
            };

            return new CursorLoader(getApplicationContext(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            adapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            adapter.swapCursor(null);
        }
    };
    
    private void setNext() {
        next = findViewById(R.id.gallery_next);
        
        next.setOnClickListener(v -> {
            System.out.println(String.valueOf(adapter.path_list.size()));

            if(adapter.path_list.size() == 0) {
                Toast.makeText(this, "이미지나 동영상을 최소 한장 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(this, NewPost_text.class);
                intent.putExtra("image", adapter.path_list);
                intent.putExtra("idx", getIdx());

                startActivity(intent);
            }


        });
    }

    private String getIdx() {
        sqLite_user = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);

        db = sqLite_user.getReadableDatabase();

        return sqLite_user.getIdx(db);
    }

}