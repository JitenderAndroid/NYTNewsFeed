package com.jitenderkumar.newsfeed.network;
import com.jitenderkumar.newsfeed.models.FeedListResponse;
import com.jitenderkumar.newsfeed.models.Price;
import com.jitenderkumar.newsfeed.models.Ticket;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataManager {

    //https://api.nytimes.com/svc/mostpopular/v2/ viewed/7.json?api-key=uSJNhFoWrYHYG1cZXS4zBrX8Gl0oSV8y

    private static Retrofit retrofit = null;

    private static String API_KEY = "uSJNhFoWrYHYG1cZXS4zBrX8Gl0oSV8y";
    private String BASE_URL = "https://api.nytimes.com/svc/mostpopular/v2/";

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .build();

    public static DataManager getInstance() {
        return ApiClientSingleton.INSTANCE;
    }

    private IApiInterface getDataManager() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(IApiInterface.class);
    }

    public void getFeedList(final DataManagerListener dataManagerListener) {
        Call<FeedListResponse> call = getDataManager().getNewsFeed(API_KEY);
        call.enqueue(new Callback<FeedListResponse>() {
            @Override
            public void onResponse(Call<FeedListResponse> call, Response<FeedListResponse> response) {
                if (!response.isSuccessful()) {
                    dataManagerListener.onError(response.errorBody());
                    return;
                }

                if (response.body() != null && response.body().getStatus() != null &&
                        response.body().getStatus().equalsIgnoreCase("OK"))
                    dataManagerListener.onSuccess(response.body());
                else
                    dataManagerListener.onError(response.body().getStatus());
            }

            @Override
            public void onFailure(Call<FeedListResponse> call, Throwable t) {
                dataManagerListener.onError(t);
            }
        });
    }

    public Observable<List<Ticket>> getSearchTicket(String from , String to) {
         return getDataManager()
                 .searchTickets(from, to)
                .toObservable()
                 .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Ticket> getPriceObservable(final Ticket ticket) {
        return getDataManager()
                .getPrice(ticket.getFlightNumber(), ticket.getFrom(), ticket.getTo())
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Price, Ticket>() {
                    @Override
                    public Ticket apply(Price price) throws Exception {
                        ticket.setPrice(price);
                        return ticket;
                    }
                });
    }

    public interface DataManagerListener {
        void onSuccess(Object response);

        void onError(Object error);
    }

    private static class ApiClientSingleton {
        private static final DataManager INSTANCE = new DataManager();
    }
}