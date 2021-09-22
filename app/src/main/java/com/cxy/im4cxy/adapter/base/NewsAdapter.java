package com.cxy.im4cxy.adapter.base;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cxy.im4cxy.R;
import com.cxy.im4cxy.mvp.bean.NewsBean.NewslistBean;
import com.cxy.im4cxy.ui.NewsDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<NewslistBean> mPosts;


    public NewsAdapter(Context context, List<NewslistBean> posts) {
        mContext = context;
        mPosts = posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news, null, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final NewslistBean post = mPosts.get(position);
        PostHolder postHolder = (PostHolder) holder;
        if (post.getPicUrl() == null)
            Glide.with(mContext).load(R.mipmap.icon_message_press).into(postHolder.mIvUserAvatar);
        else
            Glide.with(mContext).load(post.getPicUrl()).into(postHolder.mIvUserAvatar);
        postHolder.mTvPostContent.setText(post.getDescription());
        postHolder.mTvPostTime.setText(post.getCtime());
        postHolder.mTvUserName.setText(post.getTitle());
        postHolder.itemView.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, NewsDetailActivity.class).putExtra("url", post.getUrl())));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    class PostHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user_avatar)
        ImageView mIvUserAvatar;
        @BindView(R.id.tv_user_name)
        TextView mTvUserName;
        @BindView(R.id.tv_post_content)
        TextView mTvPostContent;
        @BindView(R.id.tv_post_time)
        TextView mTvPostTime;

        public PostHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);//不能使用mContext需要使用this
        }
    }
}
