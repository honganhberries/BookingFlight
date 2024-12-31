package com.example.bookingflight;

import android.content.Context;

import com.example.bookingflight.inteface.ApiService;
import com.example.bookingflight.interceptors.AuthInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.Executors;

public class ApiServiceClient {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    private static ApiService apiService;

    public static ApiService getApiService(Context context) {
        if (apiService == null) {
            // Khởi tạo AuthInterceptor với context
            AuthInterceptor authInterceptor = new AuthInterceptor(context);

            // Khởi tạo OkHttpClient với AuthInterceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)  // Thêm interceptor vào client
                    .build();

            // Cấu hình Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.14/TTCS/app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .client(client)  // Sử dụng OkHttpClient với interceptor
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}

