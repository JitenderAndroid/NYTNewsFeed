package com.jitenderkumar.newsfeed.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jitenderkumar.newsfeed.R;
import com.jitenderkumar.newsfeed.models.Result;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Result> mFeedList;
    public  IFeedCallback mIFeedCallback;

    public FeedAdapter(Context mContext, ArrayList<Result> mFeedList, IFeedCallback mIFeedCallback) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mFeedList = mFeedList;
        this.mIFeedCallback = mIFeedCallback;
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFeedBinding itemFeedBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.item_feed,
                parent,
                false);
        return new ViewHolder(itemFeedBinding);
    }

    @Override
    public void onBindViewHolder(FeedAdapter.ViewHolder holder, final int position) {
        holder.bind(mFeedList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemFeedBinding itemFeedBinding;
        public ViewHolder(ItemFeedBinding itemFeedBinding) {
            super(itemFeedBinding.getRoot());
            this.itemFeedBinding = itemFeedBinding;
            itemView.setOnClickListener(this);
        }

        void bind(Result result) {
            itemFeedBinding.setResultModel(result);
            itemFeedBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            mIFeedCallback.onItemClicked(mFeedList.get(getAdapterPosition()));
        }
    }

    public interface IFeedCallback {
        public void onItemClicked(Result result);
    }
}
