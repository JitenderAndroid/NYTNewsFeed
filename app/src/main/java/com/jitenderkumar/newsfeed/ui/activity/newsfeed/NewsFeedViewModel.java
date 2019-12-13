package com.jitenderkumar.newsfeed.ui.activity.newsfeed;

import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jitenderkumar.newsfeed.models.FeedListResponse;
import com.jitenderkumar.newsfeed.models.Result;
import com.jitenderkumar.newsfeed.network.DataManager;

import java.util.ArrayList;

public class NewsFeedViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Result>> mutableList;
    public MutableLiveData<Boolean> mutableViewProgress;

    public NewsFeedViewModel() {
        if (mutableList == null) {
            mutableList= new MutableLiveData<>();
            mutableViewProgress = new MutableLiveData<>();
        }
        getNewsFeed();
    }

    private void getNewsFeed() {
        mutableViewProgress.setValue(true);
        DataManager.getInstance().getFeedList(new DataManager.DataManagerListener() {
            @Override
            public void onSuccess(Object response) {
                mutableViewProgress.setValue(false);
                if (response instanceof FeedListResponse) {
                    FeedListResponse feedListResponse = (FeedListResponse) response;
                    mutableList.setValue((ArrayList<Result>) feedListResponse.getResults());
                }
            }

            @Override
            public void onError(Object error) {
                mutableViewProgress.setValue(false);
            }
        });
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    MutableLiveData<ArrayList<Result>> getResults() {
          return mutableList;
    }

    MutableLiveData<Boolean> getProgressResult() {
        return mutableViewProgress;
    }
}
