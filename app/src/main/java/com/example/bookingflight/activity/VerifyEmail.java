package com.example.bookingflight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingflight.R;
import com.example.bookingflight.model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VerifyEmail extends AppCompatActivity {
    private EditText emailInput;
    private Button btnSendOtp;
    private String generatedOtp;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enteremail);

        emailInput = findViewById(R.id.emailInput);
        btnSendOtp = findViewById(R.id.btnSendOtp);

        currentUser = getIntent().getParcelableExtra("object_user");

        btnSendOtp.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (validateEmail(email)) {
                sendOtp(email);

                Intent intent = new Intent(VerifyEmail.this, VerifyOtp.class);
                intent.putExtra("email", email);
                intent.putExtra("object_user", currentUser);
                intent.putExtra("generatedOtp", generatedOtp);
                intent.putExtra("new_password", getIntent().getStringExtra("new_password"));
                startActivity(intent);
            } else {
                Toast.makeText(VerifyEmail.this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void sendOtp(String email) {
        generatedOtp = String.valueOf((int) (Math.random() * 900000) + 100000);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                sendEmail(email, "Your OTP Code", "Your OTP code is: " + generatedOtp);
                runOnUiThread(() -> {
                    Toast.makeText(this, "OTP đã được gửi đến " + email, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerifyEmail.this, VerifyOtp.class);
                    intent.putExtra("generatedOtp", generatedOtp);
                    intent.putExtra("maKH", currentUser.getMaKH());
                    startActivity(intent);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Log.e("Email Error", "Lỗi khi gửi email: " + e.getMessage(), e);
                    Toast.makeText(this, "Lỗi khi gửi email.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void sendEmail(String to, String subject, String message) {
        new Thread(() -> {
            try {
                // API Key của bạn
                String apiKey = getResources().getString(R.string.api_key_otp);

                // URL endpoint của SendGrid API
                URL url = new URL("https://api.sendgrid.com/v3/mail/send");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Thiết lập phương thức HTTP là POST
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Tạo nội dung JSON cho email
                String jsonPayload = "{" +
                        "\"personalizations\": [{" +
                        "\"to\": [{" +
                        "\"email\": \"" + to + "\"" +
                        "}]," +
                        "\"subject\": \"" + subject + "\"" +
                        "}]," +
                        "\"from\": {" +
                        "\"email\": \"nguyenhonganh152002@gmail.com\"" +
                        "}," +
                        "\"content\": [{" +
                        "\"type\": \"text/html\"," +
                        "\"value\": \"" + message + "\"" +
                        "}]" +
                        "}";

                // Gửi yêu cầu POST
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Xử lý phản hồi
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                    // Gửi thành công
                    runOnUiThread(() -> {
                        Toast.makeText(this, "OTP đã được gửi đến " + to, Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Lỗi khi gửi email
                    runOnUiThread(() -> {
                        Log.e("Email Error", "Lỗi khi gửi email: " + responseCode);
                        Toast.makeText(this, "Lỗi khi gửi email.", Toast.LENGTH_SHORT).show();
                    });
                }

                // Đóng kết nối
                connection.disconnect();
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Log.e("Email Error", "Lỗi khi gửi email: " + e.getMessage(), e);
                    Toast.makeText(this, "Lỗi khi gửi email.", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

}
