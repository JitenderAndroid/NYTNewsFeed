package com.jitenderkumar.newsfeed.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jitenderkumar.newsfeed.R;
import com.jitenderkumar.newsfeed.models.MediaMetaDatum;
import com.jitenderkumar.newsfeed.models.Result;
import com.jitenderkumar.newsfeed.ui.activity.newsfeed.NewsFeedActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView imagePost;
    private TextView textTitle;
    private TextView textDescription;
    private TextView textTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        imagePost = findViewById(R.id.image_feed);
        textTitle = findViewById(R.id.text_title);
        textDescription = findViewById(R.id.text_description);
        textTime = findViewById(R.id.text_time);
        getIntentData();
    }

    void getIntentData() {
        Result result = (Result) getIntent().getSerializableExtra(NewsFeedActivity.INTENT_DATA);
        setData(result);
    }

    void setData(Result result) {
        if (!TextUtils.isEmpty(result.getTitle()))
            textTitle.setText(result.getTitle());

        if (!TextUtils.isEmpty(result.getAbstract()))
            textDescription.setText(result.getAbstract());

        textTime.setText(result.getPublishedDate());

        if (result.getMedia()!=null && result.getMedia().size()>0) {
            if (result.getMedia().get(0).getMediaMetadata().size() > 0) {
                List<MediaMetaDatum> metadata =
                        result.getMedia().get(0).getMediaMetadata();
                if (metadata.size() > 2) {
                    Picasso.get().load(metadata.get(2).getUrl())
                            .error(R.color.colorLightGrey).into(imagePost);
                }
            }
        }
    }
}
