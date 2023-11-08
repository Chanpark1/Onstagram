package com.example.onstagram.Activity;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;
import static com.example.onstagram.Service.Service.IP;
import static com.example.onstagram.Service.Service.PORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onstagram.Adapter.ChatProfileAdapter;
import com.example.onstagram.Adapter.GroupMessageAdapter;
import com.example.onstagram.DTO.GroupMessage;
import com.example.onstagram.DTO.User;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;
import com.example.onstagram.Service.Service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chatting_group extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_username;
    ImageView iv_video;
    EditText et_text;
    ImageView iv_send;

    GroupMessageAdapter adapter;

    RecyclerView rv;

    LinearLayoutManager manager;

    String sendMsg;

    Handler handler;

    List<GroupMessage> list;

    List<User> user_list;
    ArrayList<String> idx_list;

    RetrofitAPI retrofitAPI;

    SQLite_User sqLite_user;
    SQLiteDatabase db;
    Socket socket;
    Service service;
    String room_idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_group);

        init();

        set_socket();

        send_msg();

        set_user_info();

        message_init();

    }

    private void init() {
        iv_back = findViewById(R.id.chat_group_back);
        tv_username = findViewById(R.id.chat_group_username);
        iv_video = findViewById(R.id.chat_group_video);
        et_text = findViewById(R.id.chat_group_input_text);
        iv_send = findViewById(R.id.chat_group_send);

        rv = findViewById(R.id.chat_group_rv);

        retrofitAPI = getApiClient().create(RetrofitAPI.class);

        service = new Service();

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            room_idx = getIntent().getStringExtra("room_idx");
            idx_list = getIntent().getStringArrayListExtra("idx_list");

            System.out.println("idx_list 받음 : " + idx_list.size());
        }


    }

    private void message_init() {

        if(room_idx != null) {
            Call<List<GroupMessage>> call = retrofitAPI.load_group_message(idx_list, room_idx, getIdx());

            call.enqueue(new Callback<List<GroupMessage>>() {
                @Override
                public void onResponse(@NonNull Call<List<GroupMessage>> call,@NonNull Response<List<GroupMessage>> response) {
                    if(response.body() != null && response.isSuccessful()) {

                        list = response.body();

                        generateMessageList(list);

                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<GroupMessage>> call, @NonNull Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });

        }
    }

    private void generateMessageList(List<GroupMessage> list) {

        adapter = new GroupMessageAdapter(this, list);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);

        rv.scrollToPosition(0);

    }

    private void set_user_info() {
        Call<List<User>> call = retrofitAPI.load_group_chat_user(idx_list);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if(response.body() != null && response.isSuccessful()) {
                    user_list = response.body();

                    StringBuilder builder = new StringBuilder();

                    for (int i = 0; i < user_list.size(); i++) {
                        String name = user_list.get(i).getName();
                        String username = user_list.get(i).getUsername();

                        builder.append(username).append(", ");
                    }

                    if(builder.length() > 0) {
                        builder.setLength(builder.length() - 2);
                    }
                    tv_username.setText(builder.toString());

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }


    private String getIdx() {
        sqLite_user = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);
        db = sqLite_user.getReadableDatabase();

        return sqLite_user.getIdx(db);
    }

    private void send_msg() {
        iv_send.setOnClickListener(v -> {
            sendMsg = et_text.getText().toString();

            Call<String> call;

            if(!sendMsg.equals("")) {

                if(room_idx == null) {

                    call = retrofitAPI.upload_group_chat(idx_list, null, getIdx(), sendMsg);

                    System.out.println(idx_list.size());

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if(response.isSuccessful() &&  response.body() != null) {

                                System.out.println(response.body());

                                room_idx = response.body();

                            Thread main_thread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();

                                    try {
                                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                        ObjectOutputStream oos = new ObjectOutputStream(bos);

                                        oos.writeObject(idx_list);

                                        byte[] serialized_list = bos.toByteArray();

                                        dos.writeUTF(sendMsg + "@" + "CHATTING_GROUP" + "@" + room_idx + "@" + getIdx());

                                        dos.writeInt(serialized_list.length);

                                        dos.write(serialized_list);


                                        handler.post(new msgUpdate_group(sendMsg));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            main_thread.start();

                            }
                        }

                        @Override
                        public void onFailure( @NonNull Call<String> call, @NonNull Throwable t) {
                            Log.d(TAG, t.getMessage());
                        }
                    });
                } else {
                    call = retrofitAPI.upload_group_chat(null, room_idx, getIdx(),sendMsg);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if(response.isSuccessful()) {

                            Thread main_thread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();

                                    try {
                                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                        ObjectOutputStream oos = new ObjectOutputStream(bos);

                                        oos.writeObject(idx_list);

                                        byte[] serialized_list = bos.toByteArray();

                                        dos.writeUTF(sendMsg + "@" + "CHATTING_GROUP" + "@" + room_idx + "@" + getIdx());

                                        dos.writeInt(serialized_list.length);

                                        dos.write(serialized_list);

                                        handler.post(new msgUpdate_group(sendMsg));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            main_thread.start();

                            }
                        }

                        @Override
                        public void onFailure( @NonNull Call<String> call, @NonNull Throwable t) {
                            Log.d(TAG, t.getMessage());
                        }
                    });
                }


            }
        });

    }

    private void set_socket() {

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

    class msgUpdate_group implements Runnable {

        private String msg;

        public msgUpdate_group(String str) {this.msg = str;}

        @Override
        public void run() {

            RecyclerView rv = findViewById(R.id.chat_group_rv);

            list.add(new GroupMessage(null, getIdx(), msg, null, 0, null, null, null));

            adapter = new GroupMessageAdapter(Chatting_group.this, list);

            manager = new LinearLayoutManager(Chatting_group.this, LinearLayoutManager.VERTICAL, true);

            rv.setAdapter(adapter);
            rv.setLayoutManager(manager);

            rv.scrollToPosition(0);

        }
    }


}