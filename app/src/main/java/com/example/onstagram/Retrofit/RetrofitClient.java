package com.example.onstagram.Retrofit;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    public static final String SERVER_ADDRESS = "http://43.200.84.107/";


    public static HttpLoggingInterceptor httpLoggingInterceptor() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Log.d(TAG,message));

        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static Retrofit getApiClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
    }
    // static 으로 정의 되는 이유

    //메서드 내에서 인스턴스 변수를 사용하지 않음: 주어진 코드에서는 getApiClient() 메서드 내에서 인스턴스 변수를 사용하지 않고, 모두 로컬 변수로만 처리되고 있습니다. 따라서 이 메서드를 static으로 정의하여 객체 생성 없이 호출 가능하도록 하는 것이 더 효율적입니다.
    //
    //독립적인 기능 제공: getApiClient() 메서드는 독립적으로 기능을 제공하며, 객체의 상태를 변경하지 않습니다. 이런 경우에는 static 메서드로 정의하는 것이 객체지향적인 설계 원칙과도 일치합니다.
    //
    //외부 의존성 없음: getApiClient() 메서드 내에서 사용하는 모든 의존성들은 메서드 내에서 직접 생성되고 사용되고 있습니다. 따라서 이 메서드를 static으로 정의하여 외부 의존성을 제거하고 메서드를 독립적으로 사용 가능하게 하는 것이 더 적합합니다.


}
