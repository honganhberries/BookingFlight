package com.example.bookingflight.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.bookingflight.R;
import com.example.bookingflight.adapter.LocationAdapter;
import com.example.bookingflight.adapter.TravelAdapter;
import com.example.bookingflight.model.Location;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placefamous);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);

        List<Location> locationList = new ArrayList<>();
        List<Location> locationList2 = new ArrayList<>();
        locationList.add(new Location("Hà Nội", R.drawable.hanoi));
        locationList.add(new Location("Đà Nẵng", R.drawable.danang1));
        locationList.add(new Location("Thành phố Hồ Chí Minh", R.drawable.place02));
        locationList.add(new Location("Cần Thơ", R.drawable.cantho));


        locationList2.add(new Location("Hà Nội", R.drawable.hanoi2));
        locationList2.add(new Location("Quảng Ninh", R.drawable.quangninh1));
        locationList2.add(new Location("Thành phố Hồ Chí Minh", R.drawable.place02));
        locationList2.add(new Location("Phú Quốc", R.drawable.phuquoc));
        // Thêm các địa điểm khác tương tự

        LocationAdapter locationAdapter = new LocationAdapter(locationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(locationAdapter);

        TravelAdapter travelAdapter = new TravelAdapter(locationList2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setAdapter(travelAdapter);


    }


}