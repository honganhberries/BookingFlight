package com.example.bookingflight.interceptors;

import android.content.Context;
import android.content.SharedPreferences;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Lấy JWT token từ SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", "");

        // Kiểm tra token và thêm vào header
        if (!token.isEmpty()) {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token) // Thêm Bearer token vào header
                    .build();
            return chain.proceed(newRequest);
        }

        // Nếu không có token, tiếp tục với request gốc
        return chain.proceed(chain.request());
    }
}
