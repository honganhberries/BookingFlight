package com.example.bookingflight.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.bookingflight.ApiServiceClient;
import com.example.bookingflight.R;
import com.example.bookingflight.inteface.ApiService;
import com.example.bookingflight.model.LoginRequest;
import com.example.bookingflight.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText editemail, editpassword;
    private Button btnLogin;
    private TextView signupText;
    private SessionManager sessionManager;
    private static final String TAG = "LOGIN_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo VideoView
        VideoView videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(mp -> mp.setLooping(true)); // Lặp video tự động
        videoView.start();

        // Ánh xạ các thành phần UI
        editemail = findViewById(R.id.email);
        editpassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);
        sessionManager = new SessionManager(getApplicationContext());
        // Xử lý sự kiện nút Login
        btnLogin.setOnClickListener(v -> attemptLogin());

        // Xử lý sự kiện nút đăng ký
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String strEmail = editemail.getText().toString().trim();
        String strPassword = editpassword.getText().toString().trim();

        // Kiểm tra email và password
        if (strEmail.isEmpty() || strPassword.isEmpty()) {
            Toast.makeText(Login.this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng LoginRequest
        LoginRequest loginRequest = new LoginRequest(strEmail, strPassword);
        ApiService apiService = ApiServiceClient.getApiService(this);
        // Gọi API login
        apiService.login(loginRequest).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    User user = apiResponse.getData();
                    String token = apiResponse.getToken();
                    sessionManager.saveToken(token);

                    if (user != null && token != null) {
                        String maKH = user.getMaKH();
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("jwt_token", token);
                        editor.putString("maKH", maKH);
                        editor.apply();

                        runOnUiThread(() -> {
                            Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                            finish(); // Đóng màn hình Login
                        });
                    } else {
                        runOnUiThread(() ->
                                Toast.makeText(Login.this, "Sai email hoặc mật khẩu. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else if (response.code() == 401) {
                    // Email hoặc mật khẩu sai
                    runOnUiThread(() ->
                            Toast.makeText(Login.this, "Lỗi 401", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Login.this, "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                runOnUiThread(() ->
                        Toast.makeText(Login.this, "Lỗi kết nối đến API: " + t.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
