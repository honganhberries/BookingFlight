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

    private void sendEmail(String to, String subject, String message) throws Exception {
        // Cấu hình JavaMail API
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(currentUser.getEmail(), "vupewdwuuybexrga"); // Email và mật khẩu ứng dụng
            }
        });

        javax.mail.Message msg = new javax.mail.internet.MimeMessage(session);
        msg.setFrom(new javax.mail.internet.InternetAddress(currentUser.getEmail(), false));
        msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(to));
        msg.setSubject(subject);
        msg.setContent(message, "text/html");
        msg.setSentDate(new java.util.Date());

        javax.mail.Transport.send(msg);
    }
}
