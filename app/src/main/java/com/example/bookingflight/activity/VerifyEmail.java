package com.example.bookingflight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingflight.R;

public class VerifyEmail extends AppCompatActivity {
    private EditText emailInput;
    private Button btnSendOtp;
    private String generatedOtp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enteremail);
        emailInput = findViewById(R.id.emailInput);
        btnSendOtp = findViewById(R.id.btnSendOtp);

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                if (validateEmail(email)) {
                    // Gửi OTP qua email
                    sendOtp(email);

                    // Chuyển sang màn hình nhập OTP
                    Intent intent = new Intent(VerifyEmail.this, VerifyOtp.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(VerifyEmail.this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void sendOtp(String email) {
        generatedOtp = String.valueOf((int) (Math.random() * 900000) + 100000);
        try {
//            sendEmail(email, "Your OTP Code", "Your OTP code is: " + generatedOtp);
            Toast.makeText(this, "OTP đã được gửi đến " + email, Toast.LENGTH_SHORT).show();

            // Chuyển sang màn hình VerifyOtp và truyền OTP
            Intent intent = new Intent(VerifyEmail.this, VerifyOtp.class);
            intent.putExtra("generatedOtp", generatedOtp);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi gửi email.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
//    private void sendEmail(String to, String subject, String message) throws Exception {
//        // Cấu hình JavaMail API
//        java.util.Properties props = new java.util.Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
//            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//                return new javax.mail.PasswordAuthentication("your_email@gmail.com", "your_password"); // Email và mật khẩu ứng dụng
//            }
//        });
//
//        javax.mail.Message msg = new javax.mail.internet.MimeMessage(session);
//        msg.setFrom(new javax.mail.internet.InternetAddress("your_email@gmail.com", false));
//        msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(to));
//        msg.setSubject(subject);
//        msg.setContent(message, "text/html");
//        msg.setSentDate(new java.util.Date());
//
//        javax.mail.Transport.send(msg);
//    }
}
