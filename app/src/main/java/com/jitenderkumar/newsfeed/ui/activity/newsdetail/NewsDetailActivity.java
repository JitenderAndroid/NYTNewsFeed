package com.jitenderkumar.newsfeed.ui.activity.newsdetail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.jitenderkumar.newsfeed.R;
import com.jitenderkumar.newsfeed.databinding.ActivityNewsDetailBinding;
import com.jitenderkumar.newsfeed.models.Result;
import com.jitenderkumar.newsfeed.ui.activity.newsfeed.NewsFeedActivity;

public class NewsDetailActivity extends AppCompatActivity {

    private Result mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();
        setDataBinding();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_prev_slide_in, R.anim.anim_prev_slide_out);
    }

    private void getIntentData() {
        mResult = (Result) getIntent().getParcelableExtra(NewsFeedActivity.INTENT_DATA);
    }

    private void setDataBinding() {
        ActivityNewsDetailBinding activityNewsDetailBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        activityNewsDetailBinding.setResultModel(mResult);
    }
}
