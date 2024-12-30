package com.example.bookingflight.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class Editpassword extends AppCompatActivity {
    private EditText txOldPassword, txNewPassword, txConfirmPassword;
    private Button btnSave, btnCancel;
    private ImageView backButton;
    private User currentUser;
    private String maKH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);

        txOldPassword = findViewById(R.id.txOldPassword);
        txNewPassword = findViewById(R.id.txNewPassword);
        txConfirmPassword = findViewById(R.id.txConfirmPassword);
        backButton = findViewById(R.id.backButton);
        btnSave = findViewById(R.id.btnSave);

        currentUser = getIntent().getParcelableExtra("object_user");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Editpassword.this, LoginProfile.class);
                startActivity(intent);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = txOldPassword.getText().toString().trim();
                String newPassword = txNewPassword.getText().toString().trim();
                String confirmPassword = txConfirmPassword.getText().toString().trim();

                if (validateInput(oldPassword, newPassword, confirmPassword)) {
                    if (checkOldPassword(oldPassword)) {
                        // Lưu thông tin mật khẩu mới để xử lý sau
                        Intent intent = new Intent(Editpassword.this, VerifyEmail.class);
                        intent.putExtra("new_password", newPassword);
//                        intent.putExtra("confirm_password", confirmPassword);
                        intent.putExtra("object_user", currentUser);
//                        intent.putExtra("maKH", currentUser.getMaKH());
                        startActivity(intent);
                    } else {
                        Toast.makeText(Editpassword.this, "Mật khẩu cũ không đúng.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Editpassword.this, "Vui lòng nhập đầy đủ thông tin và xác nhận mật khẩu đúng.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean checkOldPassword(String oldPassword) {
        if (currentUser == null) {
            Toast.makeText(Editpassword.this, "Không tìm thấy người dùng.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return BCrypt.checkpw(oldPassword, currentUser.getPassword());
    }

    private boolean validateInput(String oldPassword, String newPassword, String confirmPassword) {
        return !oldPassword.isEmpty() && !newPassword.isEmpty() && newPassword.equals(confirmPassword);
    }
}
