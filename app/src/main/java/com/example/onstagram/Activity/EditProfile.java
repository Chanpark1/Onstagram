package com.example.onstagram.Activity;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onstagram.DTO.Profile;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    TextView tv_name;
    TextView tv_username;
    TextView tv_intro;
    TextView tv_info;

    TextView tv_quit;
    TextView tv_submit;

    ImageView imageView;

    ConstraintLayout cons_name;
    ConstraintLayout cons_username;
    ConstraintLayout cons_intro;

    SQLite_User sqLite_user;
    SQLiteDatabase db;

    String name;
    String username;
    String intro;
    String image;
    final String BASIC = "http://43.200.84.107/basic/basic.jpg";

    boolean hasCamPerm;
    boolean hasWritePerm;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();

        setProfile();

        setOnclick();

        setSubmit();

        changeImage();
    }
    private void init() {
        tv_name = findViewById(R.id.edit_profile_name);
        tv_username = findViewById(R.id.edit_profile_username);
        tv_intro = findViewById(R.id.edit_profile_intro);
        tv_info = findViewById(R.id.edit_profile_info);

        tv_quit = findViewById(R.id.edit_profile_quit);
        tv_submit = findViewById(R.id.edit_profile_submit);

        imageView = findViewById(R.id.edit_profile_image);

        cons_name = findViewById(R.id.edit_profile_linear1);
        cons_username = findViewById(R.id.edit_profile_linear2);
        cons_intro = findViewById(R.id.edit_profile_linear3);
    }

    private void setPrivacy() {
        tv_info.setOnClickListener(v -> {

        });
    }

    private void setProfile() {
        RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

        Call<Profile> call = retrofitAPI.get_my_profile(getIdx());

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Profile profile = response.body();

                    name = profile.getName();
                    username = profile.getUsername();
                    intro = profile.getContent();
                    image = profile.getProfile();

                    if(name == null) {
                        tv_name.setText("");
                    } else {
                        tv_name.setText(name);
                    }

                    if(intro == null) {
                        tv_intro.setText("");
                    } else {
                        tv_intro.setText(intro);
                    }

                    if(image == null) {
                        Glide.with(EditProfile.this)
                                .load(BASIC)
                                .into(imageView);
                    } else {
                        Glide.with(EditProfile.this)
                                .load(image)
                                .into(imageView);
                    }

                    tv_username.setText(username);


                }

            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void changeImage() {
        imageView.setOnClickListener(v -> {
            hasCamPerm = checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            hasWritePerm = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            if(!hasCamPerm || !hasWritePerm) {
                ActivityCompat.requestPermissions(EditProfile.this,new String[]{Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            setDialog();
        });
    }

    private void setOnclick() {

        Intent intent = new Intent(this, EditDetails.class);

        cons_name.setOnClickListener(v -> {

            intent.putExtra("name", name);
            intent.putExtra("type", "name");

            startActivity(intent);
        });

        cons_username.setOnClickListener(v -> {

            intent.putExtra("username", username);
            intent.putExtra("type", "username");

            startActivity(intent);
        });

        cons_intro.setOnClickListener(v -> {

            intent.putExtra("intro", intro);
            intent.putExtra("type", "intro");

            startActivity(intent);
        });

    }

    private void setSubmit() {
        tv_submit.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfile.this, MainActivity.class);

            startActivity(intent);
            finish();
        });
    }

    private void setDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("프로필 이미지 변경");

        if(image == null) {
            builder.setItems(R.array.image_options1, (dialog,which) -> {
               switch (which) {
                   case 0 :
                       setSingleImage();
                       break;
                   case 1:
                       takePhoto();
                       break;
               }
            });
        } else {
            builder.setItems(R.array.image_options2, (dialog, which) -> {
               switch (which) {
                   case 0 :
                       setSingleImage();
                       break;
                   case 1 :
                       takePhoto();
                       break;

                   case 2 :
                       imageView.setImageBitmap(null);
                       uri = null;
                       image = null;

                       uploadImage(null);

                       Glide.with(EditProfile.this)
                               .load(BASIC)
                               .into(imageView);

                       dialog.dismiss();

                       break;
               }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void setSingleImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        imageLauncher.launch(intent);

    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        takePhotoResult.launch(intent);
    }

    ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();

                        try {
                            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                            imageView.setImageBitmap(bm);

                            uri = getUri(EditProfile.this, bm);

                            image = getAbsolutePath(EditProfile.this, uri);

                            uploadImage(image);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
    );

    ActivityResultLauncher<Intent> takePhotoResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Bundle extras = result.getData().getExtras();

                        Bitmap bm = (Bitmap) extras.get("data");

                        uri = getUri(EditProfile.this, bm);

                        image = getAbsolutePath(EditProfile.this, uri);

                        imageView.setImageBitmap(bm);

                        uploadImage(image);

                    }

                }
            }
    );

    private void uploadImage(@Nullable String path) {

        RetrofitAPI retrofitAPI  = getApiClient().create(RetrofitAPI.class);

        HashMap<String, RequestBody> hashMap = new HashMap<>();

        RequestBody idx_request = RequestBody.create(MediaType.parse("text/plain"), getIdx());

        hashMap.put("idx", idx_request);

        if(path != null) {
            File file = new File(path);

            RequestBody file_request = RequestBody.create(MediaType.parse("image/jpeg"),file);
            MultipartBody.Part multipart = MultipartBody.Part.createFormData("uploaded_file", file.getName(), file_request);

            Call<String> call = retrofitAPI.update_profile_image(multipart, hashMap);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
        } else {
            RequestBody deleted_body = RequestBody.create(MediaType.parse("text/plain"), "deleted");

            hashMap.put("deleted",deleted_body);

            Call<String> call = retrofitAPI.update_profile_image(null, hashMap);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {

                }

                @Override
                public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {

                }
            });


        }


    }

    private Uri getUri(Context context, Bitmap bitmap)
     {
         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
         String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "IMG_" + Calendar.getInstance().getTime(), null);

         try {
             stream.close();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }

         return Uri.parse(path);
    }

    private String getAbsolutePath(Context context, Uri uri) {

        String result;

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if(cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    private String getIdx() {
        String idx = null;

        sqLite_user = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);
        db = sqLite_user.getReadableDatabase();

        idx = sqLite_user.getIdx(db);

        return idx;
    }
}