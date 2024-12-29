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
        Toast.makeText(this, "OTP đã được gửi " + email, Toast.LENGTH_SHORT).show();
    }
}
