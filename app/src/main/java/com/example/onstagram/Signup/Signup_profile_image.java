package com.example.onstagram.Signup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onstagram.Activity.MainActivity;
import com.example.onstagram.R;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class Signup_profile_image extends AppCompatActivity {

    private Uri uri = null;

    private boolean doubleBack = false;
    Button add_image;
    Button skip;

    Button submit;
    Button change_image;

    ImageView image;

    boolean hasCamPerm;
    boolean hasWritePerm;

    final static String BASIC = "http://43.200.84.107/basic/basic.jpg";

    final static String SERVER_IMAGE = "http://43.200.84.107/upload_profile.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_profile_image);

        System.out.println("onCreatedDddddd");

        image = findViewById(R.id.unloaded_image);

        Glide.with(Signup_profile_image.this)
                .load(BASIC)
                .into(image);

        setImage();

        ChangeImage();

        upload();
    }

    private void upload() {

        submit = findViewById(R.id.loaded_next);

        submit.setOnClickListener(v -> new Thread(() -> {
            try {

                String path = getAbsolutePath(uri);
                URL url = new URL(SERVER_IMAGE);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                String boundary = "*****";
                String lineEnd = "\r\n";
                String hyphens = "--";

                // Request Method 설정
                connection.setRequestMethod("POST");

                // Content-Type 설정
                connection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);

                File imageFile = new File(path);
                FileInputStream fis = new FileInputStream(imageFile);
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

                String idx = getIntent().getStringExtra("idx");

                dos.writeBytes(hyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"idx\"" + lineEnd + lineEnd);
                dos.writeBytes( idx + lineEnd);

                //파일 전송 시작
                dos.writeBytes(hyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""+imageFile.getName() + "\"" + lineEnd);
                dos.writeBytes("Content-Type: image/jpeg" + lineEnd);
                dos.writeBytes(lineEnd);

                //파일 본문 작성
                byte[] buffer = new byte[1024];
                int bytesRead;
                while((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer,0,bytesRead);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(hyphens + boundary + hyphens + lineEnd);

                int responseCode = connection.getResponseCode();

                try (InputStream in = connection.getInputStream();
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                    byte[] buff = new byte[1024 * 8];
                    int length = 0;
                    while((length = in.read(buff)) != -1) {
                        out.write(buff,0,length);
                    }
                    System.out.println(new String(out.toByteArray(), StandardCharsets.UTF_8));
                }

                fis.close();
                dos.flush();
                dos.close();
                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Signup_profile_image.this, MainActivity.class);

            startActivity(intent);

            finish();


        }).start());


    }

    private void setImage() {

        add_image = findViewById(R.id.unloaded_set_image);

        add_image.setOnClickListener(v -> {

            hasCamPerm = checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            hasWritePerm = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            if(!hasCamPerm || !hasWritePerm) {
                ActivityCompat.requestPermissions(Signup_profile_image.this,new String[]{Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }

            setDialog();
        });

    }
    private void ChangeImage() {

        change_image = findViewById(R.id.loaded_change_image);

        change_image.setOnClickListener(v -> setDialog());

    }
    private void setDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Signup_profile_image.this);
        builder.setTitle("프로필 사진 업로드");

        if(uri == null) {
            builder.setItems(R.array.image_options1, (dialog, which) -> {
                switch (which) {
                    case 0 :
                        setSingleImage();
                        break;

                    case 1 :
                        takePhoto();
                        break;

                }

            });
        } else  {
            builder.setItems(R.array.image_options2, (dialog, which) -> {
                switch (which) {
                    case 0 :
                        setSingleImage();
                        break;

                    case 1 :
                        takePhoto();
                        break;

                    case 2 :
                        image.setImageBitmap(null);
                        uri = null;

                        Glide.with(Signup_profile_image.this)
                                .load(BASIC)
                                .into(image);

                        Loaded();

                        dialog.dismiss();

                        break;
                }

            });

        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    private Uri getUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "IMG_" + Calendar.getInstance().getTime(), null);
        // 이 과정에서 업로드 하고자 하는 이미지가 갤러리에 저장 됨.

        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Uri.parse(path);
    }

    private void Loaded() {

        submit = findViewById(R.id.loaded_next);
        change_image = findViewById(R.id.loaded_change_image);

        add_image = findViewById(R.id.unloaded_set_image);
        skip = findViewById(R.id.signup_image_skip);
        if(uri != null) {
            submit.setVisibility(View.VISIBLE);
            change_image.setVisibility(View.VISIBLE);

            add_image.setVisibility(View.GONE);
            skip.setVisibility(View.GONE);
        } else {
            submit.setVisibility(View.GONE);
            change_image.setVisibility(View.GONE);

            add_image.setVisibility(View.VISIBLE);
            skip.setVisibility(View.VISIBLE);

        }
    }
    ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    image = findViewById(R.id.unloaded_image);

                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {

                        uri = result.getData().getData();

//                        content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F1000000018/ORIGINAL/NONE/image%2Fjpeg/74712736

                        if(uri != null) Loaded();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                            image.setImageBitmap(bitmap);

                            uri = getUri(Signup_profile_image.this,bitmap);

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

                    image = findViewById(R.id.unloaded_image);

                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Bundle extras = result.getData().getExtras();

                        Bitmap bm = (Bitmap) extras.get("data");

                        uri = getUri(Signup_profile_image.this, bm);

//                        content://media/external/images/media/1000000027
                        image.setImageBitmap(bm);

                        if(uri != null) Loaded();

                    }

                }
            }
    );

    private String getAbsolutePath(Uri uri) {

        String result;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
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

    // 유일한 TASK 이기 때문에 뒤로가기 버튼을 누르면 앱이 종료되는 걸 방지하기 위해 핸들러로 방지해줌
    @Override
    public void onBackPressed() {
        if(doubleBack) {
            super.onBackPressed();
            return;
        }

        this.doubleBack = true;
        Toast.makeText(this, "뒤로 가기 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBack = false, 2000);
    }


}

