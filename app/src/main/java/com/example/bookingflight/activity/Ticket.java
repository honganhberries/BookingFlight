package com.example.bookingflight.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingflight.R;
import com.example.bookingflight.inteface.ApiService;
import com.example.bookingflight.model.BookTicket;
import com.example.bookingflight.model.PostTicket;
import com.example.bookingflight.model.Result;
import com.example.bookingflight.model.User;
import com.example.bookingflight.model.detailTicket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ticket extends AppCompatActivity {
    private ArrayList<detailTicket> detailTicketList = new ArrayList<>();
    private Spinner spinnerLoaiVe;
    private detailTicket selectedTicket;
    private EditText txSoLuong;
    private Button btnBooking;
    private int currentGiaVe;
    private PostTicket tongGiaVe;
    private TextView descriptionTextView2;
    private String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        if (getIntent().hasExtra("order_id")) {
            order_id = getIntent().getStringExtra("order_id");
            Log.d("TicketActivity", "Giá trị của order_id từ Intent: " + order_id);
        } else {

            Toast.makeText(Ticket.this, "Không tìm thấy order_id trong Intent", Toast.LENGTH_SHORT).show();
            finish();
        }
        tongGiaVe = new PostTicket();
        TextView txHovaTen = findViewById(R.id.txHovaTen);
        TextView txNgaySinh = findViewById(R.id.txNgaySinh);
        TextView txDiaChi = findViewById(R.id.txDiaChi);
        TextView txSoDienThoai = findViewById(R.id.txSoDienThoai);
        TextView placeNameTextView = findViewById(R.id.placeNameTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        txSoLuong = findViewById(R.id.txSoLuong);
        btnBooking = findViewById(R.id.btnBooking);
        descriptionTextView2 = findViewById(R.id.descriptionTextView2);
        spinnerLoaiVe = findViewById(R.id.spinnerLoaiVe);
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostTicket();
                redirectToPaymentPage(order_id); // Sử dụng order_id thay vì maVeDaDat
            }
        });
        User user = getIntent().getParcelableExtra("object_user");
        Result selectedFlight = getIntent().getParcelableExtra("object_flight");
        if (user != null && selectedFlight != null) {
            currentGiaVe = Integer.parseInt(selectedFlight.getGiaVe());
            txHovaTen.setText(user.getFullname());
            txNgaySinh.setText(user.getNgaysinh());
            txDiaChi.setText(user.getDiaChi());
            txSoDienThoai.setText(user.getSoDT());
            placeNameTextView.setText("Từ: " + selectedFlight.getDiaDiemDi() + " - Đến: " + selectedFlight.getDiaDiemDen());
            descriptionTextView.setText("Ngày đi: " + selectedFlight.getNgayDi() + " - Ngày đến: " + selectedFlight.getNgayDen());
        } else {
            Toast.makeText(Ticket.this, "Không thể lấy dữ liệu người dùng hoặc chuyến bay", Toast.LENGTH_SHORT).show();
            finish();
        }

        ApiService.searchFlight.getTicket().enqueue(new Callback<ApiResponse<List<detailTicket>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<detailTicket>>> call, Response<ApiResponse<List<detailTicket>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<detailTicket>> apiResponse = response.body();
                    detailTicketList = new ArrayList<>(apiResponse.getData());
                    setupSpinner(detailTicketList);
                } else {
                    Toast.makeText(Ticket.this, "Failed khi gọi data từ API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<detailTicket>>> call, Throwable t) {
                Toast.makeText(Ticket.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner(ArrayList<detailTicket> detailTicketList) {
        List<String> loaiVeList = new ArrayList<>();
        loaiVeList.add("Chọn hạng vé");

        for (detailTicket ticket : detailTicketList) {
            loaiVeList.add(ticket.getHangVe());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiVeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLoaiVe.setAdapter(adapter);
        spinnerLoaiVe.setSelection(0, false);

        spinnerLoaiVe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) {
                    String selectedLoaiVe = (String) parentView.getItemAtPosition(position);
                    selectedTicket = getDetailTicketByHangVe(selectedLoaiVe);
                    updateTicketWithSelectedLoaiVe(selectedTicket);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private detailTicket getDetailTicketByHangVe(String hangVe) {
        for (detailTicket ticket : detailTicketList) {
            if (hangVe.equals(ticket.getHangVe())) {
                return ticket;
            }
        }
        return null;
    }

    private void updateTicketWithSelectedLoaiVe(detailTicket selectedTicket) {
        if (selectedTicket != null) {
            if (txSoLuong.getText().toString().isEmpty()) {
                Toast.makeText(Ticket.this, "Chưa nhập số lượng", Toast.LENGTH_SHORT).show();
                return;
            }
            int soLuong = Integer.parseInt(txSoLuong.getText().toString());

            int tongThanhToan = 0;

            if ("Phổ thông".equals(selectedTicket.getHangVe())) {
                tongThanhToan = currentGiaVe + (int) (soLuong * currentGiaVe * 0.1);
            } else if ("Thương gia".equals(selectedTicket.getHangVe())) {
                tongThanhToan = currentGiaVe + (int) (soLuong * currentGiaVe * 0.3);
            } else if ("Cao cấp".equals(selectedTicket.getHangVe())) {
                tongThanhToan = currentGiaVe + (int) (soLuong * currentGiaVe * 0.5);
            }
            tongGiaVe.setTongThanhToan(String.valueOf(tongThanhToan));
            descriptionTextView2.setText(tongGiaVe.getTongThanhToan());
        }
    }

    private void sendPostTicket() {
        User user = getIntent().getParcelableExtra("object_user");
        Result selectedFlight = getIntent().getParcelableExtra("object_flight");

        if (selectedFlight != null && selectedTicket != null && user != null) {
            PostTicket postTicket = new PostTicket(
                    order_id,
                    selectedTicket.getMaVe(),
                    selectedFlight.getMaCB(),
                    user.getMaKH(),
                    txSoLuong.getText().toString(),
                    descriptionTextView2.getText().toString()
            );
//            Log.d("TicketActivity", "Giá trị của order_id trước khi gọi API: " + order_id);
            ApiService.searchFlight.sendPostTicket(postTicket).enqueue(new Callback<ApiResponse<List<PostTicket>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<PostTicket>>> call, Response<ApiResponse<List<PostTicket>>> response) {
                    if (response.isSuccessful()) {
//                        Log.d("TicketActivity", "Chuyển hướng thanh toán với order_id: " + order_id);
                        redirectToPaymentPage(order_id);
                    } else {
                        Toast.makeText(Ticket.this, "Đặt vé thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<List<PostTicket>>> call, Throwable t) {
                    Log.e("TicketActivity", "Gặp lỗi khi gửi ticket lên csdl: " + t.getMessage());
                    Toast.makeText(Ticket.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void redirectToPaymentPage(String order_id) {
        String paymentUrl = "http://192.168.1.8/TTCS/vnpay_php/vnpay_pay.php?order_id=" + order_id;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
        startActivity(browserIntent);
    }
}
