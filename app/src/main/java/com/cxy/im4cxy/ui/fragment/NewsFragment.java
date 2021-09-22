package com.cxy.im4cxy.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxy.im4cxy.R;
import com.cxy.im4cxy.adapter.base.NewsAdapter;
import com.cxy.im4cxy.base.ParentWithNaviFragment;
import com.cxy.im4cxy.event.RefreshPostEvent;
import com.cxy.im4cxy.mvp.bean.NewsBean;
import com.cxy.im4cxy.mvp.presenter.NewsPresenter;
import com.cxy.im4cxy.mvp.view.NewsPostsView;
import com.cxy.im4cxy.widget.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class NewsFragment extends ParentWithNaviFragment implements NewsPostsView {


    @BindView(R.id.v_top)
    View mVTop;
    @BindView(R.id.tv_left)
    ImageView mTvLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.swipe_recycle_post)
    SwipeRecyclerView mSwipeRecyclePost;
    @BindView(R.id.tv_error)
    TextView mTvError;

    private List<NewsBean.NewslistBean> mPosts;
    private NewsAdapter mPostAdapter;
    private NewsPresenter mShowPostPresenter;
    private int page = 1;
    private final int COUNT = 10;
    private final int PAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        mPosts = new ArrayList<>();
        mShowPostPresenter = new NewsPresenter(this);
        mShowPostPresenter.showPosts(PAGE);

        mSwipeRecyclePost.getRecyclerView().setHasFixedSize(true);
        mSwipeRecyclePost.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRecyclePost.setOnLoadListener(new SwipeRecyclerView.OnLoadListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        return rootView;
    }

    /**
     *
     */
    private void loadMore() {
        page = page + 1;
        mSwipeRecyclePost.setRefreshEnable(false);
        mShowPostPresenter.showPosts(page);
    }

    /**
     *
     */
    private void refresh() {
        page = PAGE;
        mSwipeRecyclePost.setLoadMoreEnable(false);
        mShowPostPresenter.showPosts(page);
    }

    public NewsFragment() {
    }

    @Override
    protected String title() {
        return "新闻";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.tv_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_error:
                break;
        }
    }

    @Override
    public void showDialog() {
    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void showError(Throwable throwable) {
        //TODO 弹出toast的时候 fragment已经死掉
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPosts(List<NewsBean.NewslistBean> posts) {
        //TODO 显示数据
      runOnMain(new Runnable() {
          @Override
          public void run() {
              mSwipeRecyclePost.setLoadMoreEnable(true);
              mSwipeRecyclePost.setRefreshEnable(true);
              if (page == PAGE) {
                  mPosts.clear();
                  mPosts.addAll(posts);
                  mSwipeRecyclePost.setRefreshing(false);
                  if (mPosts.size() < 1) {
                      mSwipeRecyclePost.setEmptyView(mTvError);
                  } else {
                      if (mPosts.size() < COUNT) {
                          mSwipeRecyclePost.complete();
                          mSwipeRecyclePost.setLoadMoreEnable(false);
                      }
                      if (mPostAdapter == null) {
                          mPostAdapter = new NewsAdapter(getContext(), mPosts);
                          mSwipeRecyclePost.setAdapter(mPostAdapter);
                      } else {
                          mPostAdapter.notifyDataSetChanged();
                      }
                  }
              } else {
                  if (posts.size() < COUNT) {
                      mSwipeRecyclePost.complete();
                      mSwipeRecyclePost.setLoadMoreEnable(false);
                  }
                  mSwipeRecyclePost.stopLoadingMore();
                  mPosts.addAll(posts);
                  mPostAdapter.notifyDataSetChanged();
              }
          }
      });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshPostEvent(RefreshPostEvent event) {
        refresh();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
