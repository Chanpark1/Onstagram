package com.example.onstagram.Activity;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;
import static com.example.onstagram.Service.Service.IP;
import static com.example.onstagram.Service.Service.PORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onstagram.Adapter.MessageAdapter;
import com.example.onstagram.DTO.Message;
import com.example.onstagram.DTO.Profile;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;
import com.example.onstagram.Service.Service;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chatting_private extends AppCompatActivity {

    ImageView iv_back;
    ImageView iv_profile;
    ImageView iv_cam;
    ImageView iv_camera;
    ImageView iv_send;

    TextView tv_name;
    TextView tv_username;

    EditText et_input;

    private RecyclerView rv;

    MessageAdapter adapter;
    LinearLayoutManager manager;

    Service service;

    private String data;

    public static String userImage;
    String sendMsg;

    RetrofitAPI retrofitAPI;

    SQLite_User sqLite_user;
    SQLiteDatabase db;
    Socket socket;
    String to_idx;
    Handler handler;

    List<Message> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_private);

        init();

        setProfile();

        setSocket();

        Message_init();

        sendMsg();

        Message_paging();
    }



    private void init() {


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        iv_back = findViewById(R.id.chat_back);
        iv_profile = findViewById(R.id.chat_profile);
        iv_cam = findViewById(R.id.chat_video_cam);
        iv_camera = findViewById(R.id.chat_camera);
        iv_send = findViewById(R.id.chat_send);

        tv_name = findViewById(R.id.chat_name);
        tv_username = findViewById(R.id.chat_username);

        et_input = findViewById(R.id.chat_input_text);

        rv = findViewById(R.id.chat_rv);

        retrofitAPI = getApiClient().create(RetrofitAPI.class);

        service = new Service();
        to_idx = getIntent().getStringExtra("idx");

        handler = new Handler();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("data"));
    }


    private void setProfile() {
        Call<Profile> call = retrofitAPI.get_my_profile(to_idx);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Profile profile = response.body();

                    userImage = profile.getProfile();

                    tv_username.setText(profile.getUsername());
                    tv_name.setText(profile.getName());

                    if(profile.getProfile() == null) {
                        Glide.with(Chatting_private.this)
                                .load("http://43.200.84.107/basic/basic.jpg")
                                .into(iv_profile);
                    } else {
                        Glide.with(Chatting_private.this)
                                .load(profile.getProfile())
                                .into(iv_profile);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {

            }
        });

    }

    // 페이징 처리
    private void Message_init() {
        Call<List<Message>> call = retrofitAPI.load_message(to_idx, getIdx());

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NonNull Call<List<Message>> call,@NonNull Response<List<Message>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    list = response.body();
                    generateMessageList(response.body());

                    System.out.println(String.valueOf(list.size()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }

    private void Message_paging() {
        rv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!v.canScrollVertically(-1)) {

                    int count = list.size() - 1;

                    if(0 < count) {
                        String idx = list.get(count).getIdx();
                        String room_idx = list.get(count).getRoom_idx();

                        LoadMoreMessage(idx, room_idx, to_idx, getIdx());

                    }
                }
            }
        });
    }

    private void LoadMoreMessage(String idx, String room_idx, String to_idx, String from_idx) {
        Call<List<Message>> call = retrofitAPI.load_more_message(idx, room_idx, to_idx, from_idx);

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NonNull Call<List<Message>> call,@NonNull Response<List<Message>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    for(int i = 0; i < response.body().size(); i++) {
                        String idx = response.body().get(i).getIdx();
                        String userImage = response.body().get(i).getUserImage();
                        String content = response.body().get(i).getContent();
                        int vt = response.body().get(i).getViewType();
                        String room_idx = response.body().get(i).getRoom_idx();

                        list.add(new Message(idx, null, null, content, userImage, vt, null, room_idx));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {

            }
        });


    }

    private void generateMessageList(List<Message> list) {

        adapter = new MessageAdapter(Chatting_private.this,list);
        manager = new LinearLayoutManager(Chatting_private.this, LinearLayoutManager.VERTICAL, true);
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);

        rv.scrollToPosition(0);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            data = intent.getStringExtra("message");
            String[] filter = data.split("@");

            list.add(0, new Message(null,null,null, filter[0],userImage,1,null, null));

            adapter = new MessageAdapter(Chatting_private.this,list);
            manager = new LinearLayoutManager(Chatting_private.this, LinearLayoutManager.VERTICAL, true);
            rv.setAdapter(adapter);
            rv.setLayoutManager(manager);

            rv.scrollToPosition(0);

            System.out.println(String.valueOf(list.size()));
        }
    };

    private void sendMsg() {
        iv_send.setOnClickListener(v -> {
            sendMsg = et_input.getText().toString();
            System.out.println("sendMsg : " + sendMsg);

            if(!sendMsg.equals("")) {
                Call<String> call = retrofitAPI.upload_chat_private(getIdx(), to_idx, sendMsg);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if(response.isSuccessful() && response.body() != null) {

                            Thread main_thread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();

                                    try {
                                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                                        dos.writeUTF(sendMsg + "@" + "CHATTING_PRIVATE" + "@" + getIdx() + "@" + to_idx);
                                        handler.post(new msgUpdate(sendMsg));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            main_thread.start();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d(TAG, t.getMessage());

                    }
                });
            }
        });

    }

    private void setSocket() {

        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    InetAddress serverAddress = InetAddress.getByName(IP);
                    socket = new Socket(serverAddress, PORT);
                    service.setSocket(socket);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private String getIdx() {
        sqLite_user = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);

        db = sqLite_user.getReadableDatabase();

        return sqLite_user.getIdx(db);
    }

    class msgUpdate implements Runnable {

        private String msg;

        public msgUpdate(String str) {
            this.msg = str;
        }


        @Override
        public void run() {

            RecyclerView rv = findViewById(R.id.chat_rv);

            list.add(0,new com.example.onstagram.DTO.Message(null, null,null,msg,null,0,null, null));

            adapter = new MessageAdapter(Chatting_private.this, list);

            manager = new LinearLayoutManager(Chatting_private.this, LinearLayoutManager.VERTICAL, true);
            rv.setAdapter(adapter);
            rv.setLayoutManager(manager);

            rv.scrollToPosition(0);

            System.out.println(String.valueOf(list.size()));

        }
    }


}