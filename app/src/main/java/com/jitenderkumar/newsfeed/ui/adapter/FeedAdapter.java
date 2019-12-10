package com.jitenderkumar.newsfeed.ui.adapter;

import android.content.Context;
import android.media.MediaMetadata;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jitenderkumar.newsfeed.R;
import com.jitenderkumar.newsfeed.models.MediaMetaDatum;
import com.jitenderkumar.newsfeed.models.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Result> mFeedList;
    public IFeedCallback mIFeedCallback;

    public FeedAdapter(Context mContext, ArrayList<Result> mFeedList, IFeedCallback mIFeedCallback) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mFeedList = mFeedList;
        this.mIFeedCallback = mIFeedCallback;
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_feed, parent, false);
        return new FeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedAdapter.ViewHolder holder, final int position) {
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imagePost;
        private TextView textTitle;
        private TextView textDescription;
        private TextView textTime;

        public ViewHolder(View itemView) {
            super(itemView);

            imagePost = itemView.findViewById(R.id.image_feed);
            textTitle = itemView.findViewById(R.id.text_title);
            textDescription = itemView.findViewById(R.id.text_description);
            textTime = itemView.findViewById(R.id.text_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mIFeedCallback.onItemClicked(mFeedList.get(getAdapterPosition()));
        }

        void bindData() {
            Result result = mFeedList.get(getAdapterPosition());

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

    public interface IFeedCallback {
        public void onItemClicked(Result result);
    }
}
