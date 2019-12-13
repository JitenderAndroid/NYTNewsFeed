package com.jitenderkumar.newsfeed.ui.activity.newsfeed;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jitenderkumar.newsfeed.R;
import com.jitenderkumar.newsfeed.models.Result;
import com.jitenderkumar.newsfeed.ui.activity.newsdetail.NewsDetailActivity;
import com.jitenderkumar.newsfeed.ui.adapter.FeedAdapter;

import java.util.ArrayList;

public class NewsFeedActivity extends AppCompatActivity {

    public static String INTENT_DATA = "intent_data";
    private NewsFeedViewModel mNewsFeedViewModel;
    private ActivityNewsFeedBinding mActivityNewsFeedBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViewModel();
        observeViews();
    }

    private void initViewModel() {
        mActivityNewsFeedBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_feed);
        mNewsFeedViewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);
        mActivityNewsFeedBinding.setNewsFeedModel(mNewsFeedViewModel);
        mActivityNewsFeedBinding.setLifecycleOwner(this);
    }

    private void observeViews() {
        mNewsFeedViewModel.getResults().observe(this, new Observer<ArrayList<Result>>() {
            @Override
            public void onChanged(ArrayList<Result> results) {
                setAdapter(results);
            }
        });
    }

    private void setAdapter(ArrayList<Result> listFeeds) {
        mActivityNewsFeedBinding.recylerView.setLayoutManager(new LinearLayoutManager(NewsFeedActivity.this));
        FeedAdapter feedAdapter = new FeedAdapter(this, listFeeds, new FeedAdapter.IFeedCallback() {
            @Override
            public void onItemClicked(Result result) {
                Intent intent = new Intent(NewsFeedActivity.this, NewsDetailActivity.class);
                intent.putExtra(INTENT_DATA, result);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_next_slide_in, R.anim.anim_next_slide_out);
            }
        });
        mActivityNewsFeedBinding.recylerView.setAdapter(feedAdapter);
    }
}
