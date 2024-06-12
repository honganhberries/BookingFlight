package com.example.bookingflight.inteface;

import com.example.bookingflight.activity.ApiResponse;
import com.example.bookingflight.model.Airport;
import com.example.bookingflight.model.BookTicket;
import com.example.bookingflight.model.Mess;
import com.example.bookingflight.model.PostTicket;
import com.example.bookingflight.model.Result;
import com.example.bookingflight.model.User;
import com.example.bookingflight.model.detailTicket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService searchFlight = new Retrofit.Builder()
            .baseUrl("http://192.168.1.8/TTCS/app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("api/createAccount.php")
    Call<ApiResponse<List<User>>> postUser(@Body Map<String, String> userData);

    @GET("api/readFlights.php")
    Call<ApiResponse<List<Result>>> searchPlace(@QueryMap Map<String, String> option);

    @GET("api/readPassengerAccount.php")
    Call<ApiResponse<List<User>>> getListUser(@QueryMap Map<String, String> option);

    @GET("api/readTicket.php")
    Call<ApiResponse<List<detailTicket>>> getTicket();

    @GET("api/readDetailTicket.php")
    Call<ApiResponse<List<BookTicket>>> getBookedTickets();

    static List<BookTicket> filterBookedTicketsByMaKH(List<BookTicket> allBookedTickets, String maKH) {
        List<BookTicket> userBookedTickets = new ArrayList<>();

        for (BookTicket ticket : allBookedTickets) {
            if (maKH.equals(ticket.getMaKH())) {
                userBookedTickets.add(ticket);
            }
        }

        return userBookedTickets;
    }

    @POST("api/createDetailTicket.php")
    Call<ApiResponse<List<PostTicket>>> sendPostTicket(@Body PostTicket postTicket);

    @GET("api/readAirport.php")
    Call<ApiResponse<List<Airport>>> getAirport();

    @PUT("api/updatePassenger.php")
    Call<ApiResponse<List<User>>> updateInfo(@Query("maKH") String maKH, @Body Map<String, String> updateParams);
    @GET("api/readMess.php")
    Call<ApiResponse<List<Mess>>> getListMess(@Query("maKH") String maKH);
    @POST("api/createMess.php")
    Call<ApiResponse<List<Mess>>> storeMess(@Body Mess requestData);
}
