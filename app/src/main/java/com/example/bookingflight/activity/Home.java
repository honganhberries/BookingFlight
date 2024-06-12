package com.example.bookingflight.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.bookingflight.R;
import com.example.bookingflight.adapter.ImageSliderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    CardView locationCard, hanhliCard;
    private SessionManager sessionManager;
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        locationCard = findViewById(R.id.locationCard);
        hanhliCard = findViewById(R.id.hanhliCard);
        ViewPager2 viewPager2 = findViewById(R.id.imageView);
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.paner1);
        imageList.add(R.drawable.paner2);
        imageList.add(R.drawable.paner3);
        imageList.add(R.drawable.paner4);

        ImageSliderAdapter adapter = new ImageSliderAdapter(imageList);
        viewPager2.setAdapter(adapter);
        handler.postDelayed(runnable = () -> {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
            handler.postDelayed(runnable, 100);
        }, 100);

        sessionManager = new SessionManager(getApplicationContext()); // Khởi tạo SessionManager
        hanhliCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị Dialog khi CardView được bấm
                openDialog();
            }
        });
        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this , LocationActivity.class) ;
                startActivity(intent);
                finish();
            }
        });
        // Kiểm tra đăng nhập
        if (!sessionManager.isLoggedIn()) {
            // Chuyển đến trang đăng nhập nếu chưa đăng nhập
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish();
            return;
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.ticket) {
                    Intent myintent = new Intent(Home.this, YourSearchActivity.class);
                    startActivity(myintent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.home) {
                    // Xử lý sự kiện khi chọn mục "home"
                    return true;
                } else if (itemId == R.id.club) {
                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    String maKH = sessionManager.getMaKH();
                    Intent myIntent1 = new Intent(Home.this, chatActivity.class);
                    myIntent1.putExtra("maKH", maKH); // Truyền maKH qua Intent
                    startActivity(myIntent1);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.profile) {
                    Intent myintent2 = new Intent(Home.this, LoginProfile.class);
                    startActivity(myintent2);
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Sử dụng layoutInflater để nạp giao diện từ tệp XML
        View dialogView = getLayoutInflater().inflate(R.layout.activity_luggage, null);

        // Thiết lập nội dung cho Dialog
        builder.setView(dialogView);

        // Tạo Dialog
        AlertDialog dialog = builder.create();

        // Thiết lập sự kiện đóng Dialog
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Xử lý sự kiện khi Dialog được đóng
                // (Bạn có thể thực hiện các hành động khác ở đây nếu cần)
            }
        });

        // Hiển thị Dialog
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}