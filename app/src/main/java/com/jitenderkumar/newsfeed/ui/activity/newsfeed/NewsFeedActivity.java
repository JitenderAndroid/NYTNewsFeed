package com.jitenderkumar.newsfeed.ui.activity.newsfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.jitenderkumar.newsfeed.R;
import com.jitenderkumar.newsfeed.models.FeedListResponse;
import com.jitenderkumar.newsfeed.models.Result;
import com.jitenderkumar.newsfeed.network.DataManager;
import com.jitenderkumar.newsfeed.ui.activity.NewsDetailActivity;
import com.jitenderkumar.newsfeed.ui.adapter.FeedAdapter;

import java.util.ArrayList;

public class NewsFeedActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    public static String INTENT_DATA = "intent_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        ViewModel  viewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);
        initViews();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.recyler_view);
        mProgressBar = findViewById(R.id.progress_circular);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNewsFeed();
    }

    private void getNewsFeed() {
        mProgressBar.setVisibility(View.VISIBLE);
        DataManager.getInstance().getFeedList(new DataManager.DataManagerListener() {
            @Override
            public void onSuccess(Object response) {
                mProgressBar.setVisibility(View.GONE);
                if (response instanceof FeedListResponse) {
                    FeedListResponse feedListResponse = (FeedListResponse) response;
                    setAdapter((ArrayList<Result>) feedListResponse.getResults());
                }
            }

            @Override
            public void onError(Object error) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setAdapter(ArrayList<Result> listFeeds) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(NewsFeedActivity.this));
        FeedAdapter feedAdapter = new FeedAdapter(this, listFeeds, new FeedAdapter.IFeedCallback() {
            @Override
            public void onItemClicked(Result result) {
                Intent intent = new Intent(NewsFeedActivity.this,
                        NewsDetailActivity.class);
                intent.putExtra(INTENT_DATA, result);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(feedAdapter);
    }
}
