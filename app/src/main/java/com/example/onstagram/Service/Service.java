package com.example.onstagram.Service;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.onstagram.Activity.Chatting_group;
import com.example.onstagram.Activity.Chatting_private;
import com.example.onstagram.Activity.PostActivity;
import com.example.onstagram.Activity.ProfileActivity;
import com.example.onstagram.DTO.GroupChatUser;
import com.example.onstagram.DTO.Profile;
import com.example.onstagram.DTO.User;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Service extends android.app.Service {

    public static final String IP = "192.168.6.171";
    public static final int PORT = 4462;

    private final String ACCESS_ = "ACCESS";
    private final String FOLLOW_ = "FOLLOW";
    private final String LIKE = "LIKE";
    private final String POST_ = "POST";

    public Socket socket;
    String username;
    boolean dup = false;

    ArrayList<String> idx_list;

    SQLite_User sqLite_user;
    SQLiteDatabase db;

    String read;

    NotificationManager manager;

    int id = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("POST SERVICE ON CREATED");

        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(Service.this, "222")
                .setContentText("dd")
                .setContentTitle("sss")
                .setSmallIcon(R.drawable.baseline_favorite_24)
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                .build();

        startForeground(222, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread main_thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    InetAddress serverAddress = InetAddress.getByName(IP);
                    socket = new Socket(serverAddress, PORT);

                    System.out.println("Socket Thread Run");

                    if(!dup) {

                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                        out.writeUTF(getIdx() + "@" + ACCESS_);

                        dup = true;
                    }
                    DataInputStream input = null;

                    input = new DataInputStream(socket.getInputStream());

                    while(input != null) {

                        read = input.readUTF();

                        System.out.println("READ : " + read);

                        String[] filter = read.split("@");

                        /*
                        팔로우 요청
                         */
                        if(filter[1].equals(FOLLOW_)) {

                            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

                            Call<User> call = retrofitAPI.get_profile(filter[0]);

                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    if(response.isSuccessful() && response.body() != null) {
                                        username = response.body().getUsername();

                                        Call<String> call_profile = retrofitAPI.update_follow(filter[0],filter[2]);

                                        call_profile.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                                if(response.isSuccessful() && response.body() != null) {



                                                        // 해당 알림을 클릭하면 팔로우를 한 사용자의 계정으로 이동하기 위해 상대 사용자의 pk값을 넣어주기 위한 인텐트
                                                        Intent intent = new Intent(Service.this, ProfileActivity.class);

                                                        intent.putExtra("idx_visitor", filter[2]);
                                                        intent.putExtra("idx_master", filter[0]);
                                                        intent.putExtra("isFollower", response.body());


                                                        PendingIntent pendingIntent = PendingIntent.getActivity(Service.this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                                                        Notification notification = new NotificationCompat.Builder(Service.this, "noti")
                                                                .setSmallIcon(R.drawable.baseline_favorite_24)
                                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                                .setContentTitle("Onstagram")
                                                                .setContentText(username + "님이 회원님의 계정을 팔로우 하기 시작했습니다.")
                                                                .setContentIntent(pendingIntent)
                                                                .setDefaults(NotificationCompat.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                                                .setAutoCancel(true)
                                                                .build();

                                                        NotificationChannel channel = new NotificationChannel("noti","chat", NotificationManager.IMPORTANCE_HIGH);

                                                        channel.enableVibration(true);
                                                        channel.enableLights(true);
                                                        channel.setDescription("Set");

                                                        assert manager != null;
                                                        manager.createNotificationChannel(channel);

                                                        manager.notify(id, notification);

                                                        id++;

                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {

                                            }
                                        });


                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

                                }
                            });

                        }
                          /*
                        게시글 좋아요
                         */
                        else if (filter[1].equals(LIKE)) {

                            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

                            Call<Profile> call = retrofitAPI.get_my_profile(filter[0]);

                            call.enqueue(new Callback<Profile>() {
                                @Override
                                public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                                    if(response.isSuccessful() && response.body() != null) {

                                        //본인 게시글 좋아요는 알림 안보내줌
                                        if(!getIdx().equals(filter[3])) {
                                            username = response.body().getUsername();

                                            Intent intent = new Intent(Service.this, PostActivity.class);
                                            intent.putExtra("postIdx", filter[2]);

                                            PendingIntent pendingIntent = PendingIntent.getActivity(Service.this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                                            Notification notification = new NotificationCompat.Builder(Service.this, "noti")
                                                    .setSmallIcon(R.drawable.baseline_favorite_24)
                                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                    .setContentTitle("Onstagram")
                                                    .setContentText(username + "님이 회원님의 게시글을 좋아합니다.")
                                                    .setContentIntent(pendingIntent)
                                                    .setDefaults(NotificationCompat.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                                    .setAutoCancel(true)
                                                    .build();

                                            NotificationChannel channel = new NotificationChannel("noti", "chat", NotificationManager.IMPORTANCE_HIGH);

                                            channel.enableVibration(true);
                                            channel.enableLights(true);
                                            channel.setDescription("Set");

                                            assert manager != null;
                                            manager.createNotificationChannel(channel);

                                            manager.notify(id, notification);

                                            id++;
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                                    Log.d(TAG, t.getMessage());
                                }
                            });
                        } else if(filter[1].equals("CHATTING_PRIVATE")) {
                            String Activity = null;
                            ComponentName componentName = null;

                            sendMessage(read);

                            Intent intent = new Intent(Service.this, Chatting_private.class);

                            ActivityManager acManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

                            List<ActivityManager.RunningTaskInfo> info = acManager.getRunningTasks(1);

                            if(info.size() != 0) {
                                componentName = info.get(0).topActivity;
                                Activity = componentName.getShortClassName().substring(1);

                                // 채팅창에 머물고 있지 않다면 알림을 받는다.
                                if(!Activity.equals("Chatting")) {

                                    intent.putExtra("idx", filter[2]);

                                    PendingIntent p_intent = PendingIntent.getActivity(Service.this, (int) System.currentTimeMillis(), intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


                                    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Service.this,"noti")
                                            .setSmallIcon(R.drawable.baseline_add_24)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setContentTitle("Chatting")
                                            .setContentIntent(p_intent)
                                            .setContentText(filter[0])
                                            .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                                            .setAutoCancel(true);

                                    NotificationChannel channel = new NotificationChannel("noti", "chat", NotificationManager.IMPORTANCE_HIGH);

                                    channel.enableVibration(true);
                                    channel.enableLights(true);
                                    channel.setDescription("set");

                                    assert manager != null;
                                    manager.createNotificationChannel(channel);

                                    manager.notify(id,builder.build());

                                    id++;

                                }

                            }

                        } else if (filter[1].equals("CHATTING_GROUP")) {

                            RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

                            send_group_message(read);

                            Intent intent = new Intent(Service.this, Chatting_group.class);
                            // 해당 유저가 room_idx로 채팅방에 속해 있는지 확인 후 sender_idx를 통해 전송자의 정보를 가져와서 알림을 띄워준다.

                            Call<GroupChatUser> call = retrofitAPI.check_room_idx(filter[2], filter[3]);

                            call.enqueue(new Callback<GroupChatUser>() {
                                @Override
                                public void onResponse(@NonNull Call<GroupChatUser> call, @NonNull Response<GroupChatUser> response) {
                                    if(response.isSuccessful() && response.body() != null) {
                                        String username = response.body().getUsername();
                                        idx_list = response.body().getIdx_list();

                                        intent.putExtra("room_idx", filter[2]);

                                        PendingIntent p_intent = PendingIntent.getActivity(Service.this, (int) System.currentTimeMillis(), intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                                        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(Service.this,"noti")
                                                .setSmallIcon(R.drawable.baseline_add_24)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setContentTitle("Chatting")
                                                .setContentIntent(p_intent)
                                                .setContentText(username + " :" + " " + filter[0])
                                                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                                                .setAutoCancel(true);

                                        NotificationChannel channel = new NotificationChannel("noti", "chat", NotificationManager.IMPORTANCE_HIGH);

                                        channel.enableVibration(true);
                                        channel.enableLights(true);
                                        channel.setDescription("set");

                                        assert manager != null;
                                        manager.createNotificationChannel(channel);

                                        manager.notify(id,builder.build());

                                        id++;
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<GroupChatUser> call, @NonNull Throwable t) {
                                    Log.d(TAG, t.getMessage());
                                }
                            });

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        main_thread.start();

        return START_STICKY;
    }

    private void createNotificationChannel() {

        NotificationChannel channel = new NotificationChannel("222", "PostChannel", NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

    }

    private String getIdx() {
        sqLite_user = new SQLite_User(this, SQLite_User.tableName, null, SQLite_User.VERSION);
        db = sqLite_user.getReadableDatabase();

        return sqLite_user.getIdx(db);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println("POST SERVICE ON DESTROY");
        Toast.makeText(this, "POST SERVICE DESTROYED", Toast.LENGTH_SHORT).show();

        dup = false;
    }

    public void sendMessage(String msg)  {
        Intent intent = new Intent("data");
        intent.putExtra("message", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void send_group_message(String msg) {
        Intent intent = new Intent("data");
        intent.putExtra("message", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void setSocket(Socket _socket) {
        socket = _socket;
    }
}
