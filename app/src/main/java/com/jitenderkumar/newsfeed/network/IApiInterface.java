package com.jitenderkumar.newsfeed.network;

import com.jitenderkumar.newsfeed.models.FeedListResponse;
import com.jitenderkumar.newsfeed.models.Price;
import com.jitenderkumar.newsfeed.models.Ticket;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IApiInterface {

   @GET("viewed/1.json")
   Call<FeedListResponse> getNewsFeed(@Query("api-key") String apiKey);

   @GET("airline-tickets.php")
   Single<List<Ticket>> searchTickets(@Query("from") String from, @Query("to") String to);

   @GET("airline-tickets-price.php")
   Single<Price> getPrice(@Query("flight_number") String flightNumber, @Query("from") String from, @Query("to") String to);
}