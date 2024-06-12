package com.example.bookingflight.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookingflight.R;
import com.example.bookingflight.model.User;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class Editpassword extends AppCompatActivity {
    private EditText txOldPassword, txNewPassword, txConfirmPassword;
    Button btnSave, btnCancel;
    ImageView backButton;
    private List<User> mListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);
        txOldPassword = findViewById(R.id.txOldPassword);
        txNewPassword = findViewById(R.id.txNewPassword);
        txConfirmPassword = findViewById(R.id.txConfirmPassword);
        backButton = findViewById(R.id.backButton);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        mListUser = new ArrayList<>();

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
                    // Kiểm tra mật khẩu cũ có đúng không
                    if (checkOldPassword(oldPassword)) {
                        // Băm và salt mật khẩu mới trước khi lưu trữ hoặc cập nhật
                        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                        // Cập nhật mật khẩu mới trong danh sách người dùng
                        updatePassword(hashedNewPassword);

                        // Hiển thị thông báo cập nhật thành công
                        Toast.makeText(Editpassword.this, "Cập nhật mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Hiển thị thông báo mật khẩu cũ không đúng
                        Log.d("Debug", "hashedOldPassword: " + oldPassword);
                        Log.d("Debug", "user.getPassword(): " + getCurrentUser().getPassword());
                        Toast.makeText(Editpassword.this, "Mật khẩu cũ không đúng.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Hiển thị thông báo về lỗi nhập liệu
                    Toast.makeText(Editpassword.this, "Vui lòng nhập đầy đủ thông tin và xác nhận mật khẩu đúng.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePassword(String hashedNewPassword) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            currentUser.setPassword(hashedNewPassword);
            // Cập nhật danh sách người dùng
            updatePasswordOnServer(currentUser);
        }
    }

    private void updatePasswordOnServer(User updatedUser) {
        for (int i = 0; i < mListUser.size(); i++) {
            if (mListUser.get(i).getEmail().equals(updatedUser.getEmail())) {
                // Cập nhật thông tin người dùng trong danh sách
                mListUser.set(i, updatedUser);
                break;
            }
        }
    }

    private boolean checkOldPassword(String oldPassword) {
        User user = getCurrentUser();
        return user != null && BCrypt.checkpw(oldPassword, user.getPassword());
    }

    private User getCurrentUser() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String currentUserEmail = sessionManager.getEmail();

        // Tìm người dùng trong danh sách
        for (User user : mListUser) {
            if (currentUserEmail.equals(user.getEmail())) {
                return user;
            }
        }

        return null; // Trả về null nếu không tìm thấy người dùng
    }

    private boolean validateInput(String oldPassword, String newPassword, String confirmPassword) {
        return !oldPassword.isEmpty() && !newPassword.isEmpty() && newPassword.equals(confirmPassword);
    }
}
