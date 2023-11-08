package com.example.onstagram.Adapter;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;
import static com.example.onstagram.Service.Service.IP;
import static com.example.onstagram.Service.Service.PORT;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.onstagram.DTO.ImagePath;
import com.example.onstagram.DTO.Post;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;
import com.example.onstagram.SQLite.SQLite_User;
import com.example.onstagram.Service.Service;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    final String LIKE = "LIKE";
    ArrayList<Post> list = new ArrayList<>();

    ArrayList<ImagePath> image_list;

    Context context;

    PathSliderAdapter adapter;

    Service service;

    Socket socket;

    String val;

    SQLiteDatabase db;
    SQLite_User sqLite_user;

    private final String BASIC = "http://43.200.84.107/basic/basic.jpg";

    public PostAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_main_post, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {

        holder.val = list.get(position).getIsLike();

        setProfile(holder, position, list);

        setUsername(holder, position, list);

        setImage(holder, list, position);

        setContent(holder, position, list);

        setLike_num(holder, list, position);

        setLike(holder, position);

        System.out.println("sdfnjksdfnjk");
    }

    private void setLike(@NonNull PostAdapter.PostViewHolder holder, int position) {

            holder.like_btn.setOnClickListener(v -> {

                RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

                if(list.get(position).getIsLike().equals("X")) {

                    Call<String> call = retrofitAPI.update_like(getIdx(holder.itemView.getContext()), list.get(position).getPostIdx(), "+");

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                            if(response.isSuccessful() && response.body() != null) {

                                service = new Service();

                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();

                                        try {
                                            InetAddress serverAddress = InetAddress.getByName(IP);

                                            socket = new Socket(serverAddress, PORT);

                                            // Foreground Service 최초 실행시 생성된 소켓과 연결 해준다.
                                            service.setSocket(socket);

                                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                                            dos.writeUTF(getIdx(holder.itemView.getContext()) + "@" + LIKE + "@" + list.get(position).getPostIdx() + "@" + list.get(position).getUserIdx());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                thread.start();

                                list.get(position).setIsLike("O");

                                holder.like_num.setVisibility(View.VISIBLE);
                                holder.like_btn.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.baseline_favorite_24));
                                holder.like_num.setText("좋아요 " + response.body() + "개");
                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                            Log.d(TAG, t.getMessage());

                        }
                    });

                    
                } else {

                    Call<String> call = retrofitAPI.update_like(getIdx(holder.itemView.getContext()), list.get(position).getPostIdx(),"-");

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull  Response<String> response) {
                            if(response.isSuccessful() && response.body() != null) {

                                holder.like_btn.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.baseline_favorite_border_24));

                                list.get(position).setIsLike("X");
                                if(response.body().equals("0")) {
                                    holder.like_num.setVisibility(View.GONE);
                                } else {
                                    holder.like_num.setText("좋아요 " + response.body() + "개");
                                }

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


    private void setProfile(@NonNull PostAdapter.PostViewHolder holder, int position, ArrayList<Post> list) {
        String profile_path = list.get(position).getUserImage();

        if(profile_path == null) {
            Glide.with(holder.itemView.getContext())
                    .load(BASIC)
                    .into(holder.profile_image);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(profile_path)
                    .into(holder.profile_image);
        }
    }

    private void setUsername(@NonNull PostAdapter.PostViewHolder holder, int position, ArrayList<Post> list) {
        String username = list.get(position).getUsername();

        holder.tv_username.setText(username);
        holder.tv_username2.setText(username);
    }

    private void setContent(@NonNull PostAdapter.PostViewHolder holder, int position, ArrayList<Post> list) {
        String content = list.get(position).getContent();

        holder.tv_content.setText(content);
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    private void setIndicator(@NonNull PostAdapter.PostViewHolder holder, int count) {
        ImageView[] indicators = new ImageView[count];

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(16, 8, 16, 8);

        for(int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(context);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_inactive));
            indicators[i].setLayoutParams(params);
            holder.indicator.addView(indicators[i]);
        }

        setCurrentIndicator(holder, 0);

    }

    private void setCurrentIndicator(@NonNull PostAdapter.PostViewHolder holder, int position) {

        int childCount = holder.indicator.getChildCount();

        for(int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) holder.indicator.getChildAt(i);

            if(i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_inactive));
            }
        }
    }

    private void setLike_num(@NonNull PostAdapter.PostViewHolder holder, ArrayList<Post> list, int position) {
        String like_num = list.get(position).getLike_num();
        String isLike = list.get(position).getIsLike();

        if(like_num.equals("0")) {
            holder.like_num.setVisibility(View.GONE);
        } else {

            holder.like_num.setText("좋아요 " + like_num + "개");
        }

        if(isLike.equals("O")) {
            holder.like_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24));
        } else {
            holder.like_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_border_24));
        }
    }
    private String getIdx(Context context) {

        sqLite_user = new SQLite_User(context,SQLite_User.tableName, null, SQLite_User.VERSION);

        db = sqLite_user.getReadableDatabase();

        return sqLite_user.getIdx(db);
    }

    private void setImage(@NonNull PostAdapter.PostViewHolder holder, ArrayList<Post> list, int position) {

        image_list = list.get(position).getImage_path();

        adapter = new PathSliderAdapter(context, image_list);

        holder.viewPager.setOffscreenPageLimit(1);
        holder.viewPager.setAdapter(adapter);

        holder.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                setCurrentIndicator(holder, position);
            }
        });

        setIndicator(holder, image_list.size());
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        protected ImageView profile_image;
        protected TextView tv_username;
        protected ImageButton dehaze;
        protected ImageView mainImage;
        protected ImageView like_btn;
        protected ImageView comment_btn;
        protected ImageView bookmark_btn;
        protected LinearLayout indicator;
        protected TextView like_num;
        protected TextView tv_username2;

        protected TextView tv_content;
        protected TextView more_comment;
        protected FrameLayout frameLayout;
        protected ViewPager2 viewPager;

        protected String val;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            this.profile_image = itemView.findViewById(R.id.rv_main_post_profile);
            this.tv_username = itemView.findViewById(R.id.rv_main_post_username);
            this.dehaze = itemView.findViewById(R.id.rv_main_post_dehaze);
            this.mainImage = itemView.findViewById(R.id.rv_main_post_image);
            this.like_btn = itemView.findViewById(R.id.rv_main_post_like);
            this.comment_btn = itemView.findViewById(R.id.rv_main_post_comment);
            this.bookmark_btn = itemView.findViewById(R.id.rv_main_post_bookmark);
            this.indicator = itemView.findViewById(R.id.rv_main_post_indicator);
            this.like_num = itemView.findViewById(R.id.rv_main_post_like_num);
            this.tv_username2 = itemView.findViewById(R.id.rv_main_post_content_username);
            this.more_comment = itemView.findViewById(R.id.rv_main_post_reply);
            this.frameLayout = itemView.findViewById(R.id.rv_main_post_frame);
            this.viewPager = itemView.findViewById(R.id.rv_main_post_viewpager);
            this.tv_content = itemView.findViewById(R.id.rv_main_post_content_text);
            this.val = val;


        }
    }
}
