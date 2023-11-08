package com.example.onstagram.Retrofit;

import com.example.onstagram.DTO.ChattingList;
import com.example.onstagram.DTO.GroupChatUser;
import com.example.onstagram.DTO.GroupMessage;
import com.example.onstagram.DTO.Message;
import com.example.onstagram.DTO.Others;
import com.example.onstagram.DTO.Post;
import com.example.onstagram.DTO.Profile;
import com.example.onstagram.DTO.ProfilePost;
import com.example.onstagram.DTO.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import okhttp3.MultipartBody;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RetrofitAPI {

    @FormUrlEncoded
    @POST("login.php")
    Call<String> login (
      @Field("account") String account,
      @Field("password") String pw
    );

    @FormUrlEncoded
    @POST("signup_verification.php")
    Call<String> verification (
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("signup_verification_email.php")
    Call<String> verification_email(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("signup_email.php")
    Call<String> signup_email(
            @Field("idx") String idx,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("signup_phone.php")
    Call<String> signup_phone (
            @Field("phone") String phone,
            @Field("name") String name,
            @Field("username") String username,
            @Field("password") String pw
    );

    @FormUrlEncoded
    @POST("signup_duplicatePhone.php")
    Call<String> signup_duplicatePhone (
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("signup_duplicateUsername.php")
    Call<String> signup_duplicateUsername(
            @Field("username") String username
    );
    @Multipart
    @POST("upload_post.php")
    Call<String> upload_post (
            @Part List<MultipartBody.Part> files,
            @PartMap HashMap<String,RequestBody> params
    );
    @FormUrlEncoded
    @POST("load_profile_info.php")
    Call<User> get_profile (
            @Field("idx") String idx
    );
    @FormUrlEncoded
    @POST("load_my_profile.php")
    Call<Profile> get_my_profile(
            @Field("idx_master") String idx
    );
    @FormUrlEncoded
    @POST("load_post.php")
    Call<ArrayList<Post>> get_post(
            @Field("idx") String idx
    );

    @FormUrlEncoded
    @POST("load_main_post.php")
    Call<ArrayList<Post>> get_main_post(
            @Field("idx") String idx
    );
    @FormUrlEncoded
    @POST("load_profile_post.php")
    Call<List<ProfilePost>> get_profile_post(
            @Field("idx") String idx
    );
    @FormUrlEncoded
    @POST("load_search_profile.php")
    Call<List<Profile>> get_search_profile(
            @Field("data") String data
    );
    @FormUrlEncoded
    @POST("load_others_post.php")
    Call<List<ProfilePost>> get_others_post(
            @Field("idx_master") String idx_master,
            @Field("idx_visitor") String idx_visitor
    );
    @FormUrlEncoded
    @POST("load_profile_others.php")
    Call<Profile> get_others_profile(
            @Field("idx_master") String idx_master,
            @Field("idx_visitor") String idx_visitor
    );
    @FormUrlEncoded
    @POST("check_others_profile.php")
    Call<Others> check_others(
            @Field("idx_master") String idx_master,
            @Field("idx_visitor") String idx_visitor
    );
    @FormUrlEncoded
    @POST("update_profile.php")
    Call<String> update_profile(
            @Field("idx") String idx,
            @Field("value") String value,
            @Field("type") String type
    );
    @Multipart
    @POST("update_profile_image.php")
    Call<String> update_profile_image(
            @Part MultipartBody.Part file,
            @PartMap HashMap<String, RequestBody> params
    );
    @FormUrlEncoded
    @POST("check_locked.php")
    Call<String> check_lock(
            @Field("idx") String idx
    );
    @FormUrlEncoded
    @POST("update_lock.php")
    Call<String> update_lock(
            @Field("idx") String idx,
            @Field("lock") String lock
    );
    @FormUrlEncoded
    @POST("update_follow.php")
    Call<String> update_follow(
            @Field("idx_visitor") String idx_visitor,
            @Field("idx_master") String idx_master
    );

    @FormUrlEncoded
    @POST("update_like_num.php")
    Call<String> update_like(
            @Field("idx_visitor") String idx_visitor,
            @Field("postIdx") String postIdx,
            @Field("isLike") String isLIke
    );

    @FormUrlEncoded
    @POST("upload_chat_private.php")
    Call<String> upload_chat_private(
            @Field("from_idx") String from_idx,
            @Field("to_idx") String to_idx,
            @Field("message") String message
    );
    @FormUrlEncoded
    @POST("load_message.php")
    Call<List<Message>> load_message(
            @Field("to_idx") String to_idx,
            @Field("from_idx") String from_idx
    );

    @FormUrlEncoded
    @POST("load_more_message.php")
    Call<List<Message>> load_more_message(
            @Field("idx") String idx,
            @Field("room_idx") String room_idx,
            @Field("to_idx") String to_idx,
            @Field("from_idx") String from_idx
    );

    @FormUrlEncoded
    @POST("load_chat_list.php")
    Call<ArrayList<ChattingList>> load_chatting_list(
            @Field("idx") String idx
    );

    @FormUrlEncoded
    @POST("load_chat_profile.php")
    Call<List<User>> load_chat_profile(
            @Field("idx") String idx
    );

    @FormUrlEncoded
    @POST("upload_chat_group.php")
    Call<String> upload_group_chat(
            @Field("idx_list[]") ArrayList<String> idx_list,
            @Field("room_idx") String room_idx,
            @Field("sender_idx") String sender_idx,
            @Field("message") String message
    );

    @FormUrlEncoded
    @POST("load_group_chat_user.php")
    Call<List<User>> load_group_chat_user(
            @Field("idx_list[]") ArrayList<String> idx_list
    );

    @FormUrlEncoded
    @POST("load_group_chat_message.php")
    Call<List<GroupMessage>> load_group_message(
            @Field("idx_list[]") ArrayList<String> idx_list,
            @Field("room_idx") String room_idx,
            @Field("sender_idx") String sender_idx
    );

    @FormUrlEncoded
    @POST("check_room_idx.php")
    Call<String> check_room_idx(
            @Field("room_idx") String room_idx,
            @Field("sender_idx") String sender_idx
    );
}
