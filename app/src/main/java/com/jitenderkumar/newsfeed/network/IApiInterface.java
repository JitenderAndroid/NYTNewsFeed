package com.jitenderkumar.newsfeed.network;

import com.jitenderkumar.newsfeed.models.FeedListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IApiInterface {

   @GET("viewed/1.json")
   Call<FeedListResponse> getNewsFeed(@Query("api-key") String apiKey);
}