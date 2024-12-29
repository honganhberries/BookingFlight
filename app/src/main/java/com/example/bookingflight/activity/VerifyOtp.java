package com.example.bookingflight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingflight.R;

public class VerifyOtp extends AppCompatActivity {
    private EditText otpInput;
    private Button btnVerifyOtp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterotp);

        otpInput = findViewById(R.id.etOtpCode);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpInput.getText().toString().trim();
                if (validateOtp(otp)) {
                    // OTP hợp lệ, chuyển sang màn hình đổi mật khẩu
                    Intent intent = new Intent(VerifyOtp.this, Editpassword.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(VerifyOtp.this, "OTP không hợp lệ.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateOtp(String otp) {
        return otp.equals("123456");
    }
}

