package com.example.bookingflight.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingflight.R;
import com.example.bookingflight.inteface.ApiService;
import com.example.bookingflight.model.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtp extends AppCompatActivity {
    private EditText otpInput;
    private Button btnVerifyOtp;
    private String receivedOtp;
    private User currentUser;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterotp);

        otpInput = findViewById(R.id.etOtpCode);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        currentUser = getIntent().getParcelableExtra("object_user");
        receivedOtp = getIntent().getStringExtra("generatedOtp");
        String maKH = getIntent().getStringExtra("maKH");
        if (currentUser != null) {
            Log.d("User Info", "User maKH: " + currentUser.getMaKH());
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
            finish();
        }
        btnVerifyOtp.setOnClickListener(v -> {
            String otp = otpInput.getText().toString().trim();
            if (validateOtp(otp)) {
                String newPassword = getIntent().getStringExtra("new_password");
                updatePasswordOnServer(currentUser.getMaKH(), newPassword);
            } else {
                Toast.makeText(VerifyOtp.this, "OTP không hợp lệ.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateOtp(String otp) {
        return otp.equals(receivedOtp);
    }
    private void updatePasswordOnServer(String maKH, String newPassword) {
        // Băm mật khẩu mới và tạo salt
        String salt = BCrypt.gensalt();
        String hashedNewPassword = BCrypt.hashpw(newPassword, salt);
        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("password", hashedNewPassword); // Mật khẩu mới đã băm
        updateParams.put("salt", salt);
        updateParams.put("maKH", maKH);

        Call<ApiResponse<List<User>>> callUpdate = ApiService.searchFlight.updatePassw(maKH, updateParams);
        callUpdate.enqueue(new Callback<ApiResponse<List<User>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
                if (response.isSuccessful()) {
                    // Cập nhật mật khẩu thành công
                    Toast.makeText(VerifyOtp.this, "Cập nhật mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                    // Xóa jwt_token khỏi SharedPreferences sau khi thay đổi mật khẩu
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    sharedPreferences.edit().remove("jwt_token").apply();  // Xóa jwt_token

                    startActivity(new Intent(VerifyOtp.this, Login.class));
                    finish();  // Đóng màn hình hiện tại nếu cần
                } else {
                    Toast.makeText(VerifyOtp.this, "Cập nhật dữ liệu thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<User>>> call, Throwable t) {
                Toast.makeText(VerifyOtp.this, "Có lỗi khi kết nối đến server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

