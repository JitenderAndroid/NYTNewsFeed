package com.jitenderkumar.newsfeed.ui.activity.newsfeed;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jitenderkumar.newsfeed.models.FeedListResponse;
import com.jitenderkumar.newsfeed.models.Result;
import com.jitenderkumar.newsfeed.network.DataManager;

import java.util.ArrayList;

public class NewsFeedViewModel extends ViewModel {

    NewsFeedCallback newsFeedCallback;
    MutableLiveData<ArrayList<Result>> mutableList;

    public NewsFeedViewModel(NewsFeedCallback newsFeedCallback) {
        this.newsFeedCallback = newsFeedCallback;
        mutableList= new MutableLiveData<>();
    }

    private void getNewsFeed() {
        newsFeedCallback.showProgress();
        DataManager.getInstance().getFeedList(new DataManager.DataManagerListener() {
            @Override
            public void onSuccess(Object response) {
                newsFeedCallback.dismissProgress();
                if (response instanceof FeedListResponse) {
                    FeedListResponse feedListResponse = (FeedListResponse) response;
                    mutableList.setValue((ArrayList<Result>) feedListResponse.getResults());
                }
            }

            @Override
            public void onError(Object error) {
                newsFeedCallback.showProgress();
            }
        });
    }

    MutableLiveData<ArrayList<Result>> getResults() {
          return mutableList;
    }

    public interface NewsFeedCallback {
       void showProgress();
       void dismissProgress();
    }
}
