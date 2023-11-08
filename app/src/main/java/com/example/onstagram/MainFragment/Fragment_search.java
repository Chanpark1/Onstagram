package com.example.onstagram.MainFragment;

import static android.content.ContentValues.TAG;
import static com.example.onstagram.Retrofit.RetrofitClient.getApiClient;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onstagram.Adapter.SearchAdapter;
import com.example.onstagram.DTO.Profile;
import com.example.onstagram.R;
import com.example.onstagram.Retrofit.RetrofitAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_search extends Fragment {

    EditText input;

    RecyclerView rv;

    SearchAdapter adapter;

    LinearLayoutManager manager;

    View view;

    List<Profile> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);

        generateDataList();

        return view;
    }

    private void generateDataList() {
        input = view.findViewById(R.id.frag_search_input);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String data = input.getText().toString();

                if(data.isEmpty()) {
                    list.clear();


                } else {
                    RetrofitAPI retrofitAPI = getApiClient().create(RetrofitAPI.class);

                    Call<List<Profile>> call = retrofitAPI.get_search_profile(data);

                    call.enqueue(new Callback<List<Profile>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Profile>> call,@NonNull Response<List<Profile>> response) {
                            if(response.isSuccessful() && response.body() != null) {
                                list = response.body();
                                setData(response.body());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Profile>> call,@NonNull Throwable t) {
                            Log.d(TAG, t.getMessage());

                        }
                    });
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void setData(List<Profile> list)  {

        rv = view.findViewById(R.id.frag_search_rv);
        adapter = new SearchAdapter(view.getContext(), list);
        manager = new LinearLayoutManager(view.getContext());

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);


    }
}
