package com.example.bookingflight.activity;

import static com.example.bookingflight.inteface.ApiService.searchFlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingflight.R;
import com.example.bookingflight.adapter.TicketAdapter;
import com.example.bookingflight.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginProfile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    CardView thongtincanhan, vedadat, changepass;
    ImageView logoutButton, notifyBell ;
    TextView fullname ;
    private List<User> mListUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profile);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        logoutButton = findViewById(R.id.logoutButton);
        thongtincanhan = findViewById(R.id.thongtincanhan);
        vedadat = findViewById(R.id.vedadat);
        changepass = findViewById(R.id.changepass);
        fullname = findViewById(R.id.fullname);
        mListUser = new ArrayList<>();
        getListUser();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.ticket) {
                    Intent myintent = new Intent(LoginProfile.this, YourSearchActivity.class);
                    startActivity(myintent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.home) {
                    Intent myintent1 = new Intent(LoginProfile.this, Home.class);
                    startActivity(myintent1);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.profile) {
                    return true;
                } else if (itemId == R.id.club) {

                    Intent myintent2 = new Intent(LoginProfile.this, chatActivity.class);
                    startActivity(myintent2);
                    overridePendingTransition(0, 0);
                    return true;
                }
                return true ;
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogout();
            }
        });
        thongtincanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickThongtin();
            }
        });
        vedadat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickVeDaDat();
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickChangePass();
            }
        });

    }
    private void clickChangePass() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginProfile.this, Editpassword.class);
            intent.putExtra("object_user", currentUser);
            startActivity(intent);
            finish();
        }
    }

    private void clickVeDaDat() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginProfile.this, Booked.class);
            intent.putExtra("user_maKH", currentUser.getMaKH());
            startActivity(intent);
        }
    }

    private void clickThongtin() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginProfile.this, Profile.class);
            intent.putExtra("object_user", currentUser);
            startActivity(intent);
            finish();
        }

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

    private void getListUser(){
        Map<String, String> options = new HashMap<>();
        searchFlight.getListUser(options)
                .enqueue(new Callback<ApiResponse<List<User>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
                        ApiResponse<List<User>> apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getData() != null) {
                            mListUser = apiResponse.getData();
                            User  currentUser = getCurrentUser();
                            fullname.setText(currentUser.getFullname());
                            Log.e("List User", mListUser.size() + "");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<User>>> call, Throwable t) {
                        Toast.makeText(LoginProfile.this, "Call Api error ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void performLogout() {
        // Xóa các thông tin đăng nhập (nếu có) từ SessionManager hoặc nơi lưu trữ thông tin đăng nhập
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.logout();

        // Chuyển đến màn hình đăng nhập
        Intent intent = new Intent(LoginProfile.this, Login.class);
        startActivity(intent);
        finish(); // Đóng màn hình hiện tại để ngăn người dùng quay lại nếu họ nhấn nút Back
    }
}