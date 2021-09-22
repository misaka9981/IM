package com.cxy.im4cxy.mvp.presenter;


import android.util.Log;

import com.cxy.im4cxy.mvp.bean.NewsBean;
import com.cxy.im4cxy.mvp.model.BmobModel;
import com.cxy.im4cxy.mvp.view.NewsPostsView;
import com.cxy.im4cxy.util.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class NewsPresenter {
    private NewsPostsView mShowPostsView;
    private BmobModel mBmobModel;

    public NewsPresenter(NewsPostsView showPostsView) {
        mShowPostsView = showPostsView;
        mBmobModel = new BmobModel();
    }


    public void showPosts(int page) {
        mShowPostsView.showDialog();
        Request request = new Request.Builder().url("http://api.tianapi.com/caijing/index?key=d2cc63c733a7a76dd4f0c53e1562c95b&num=10&page=" + page).build();

        HttpUtils.buildHttpClient().newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                Log.i("=====", result);
                NewsBean newsBean = new Gson().fromJson(result, NewsBean.class);
                if (newsBean.getCode() == 200) {
                    mShowPostsView.showPosts(newsBean.getNewslist());
                } else {
                    mShowPostsView.showError(new Throwable(newsBean.getMsg()));
                }
            }

            public void onFailure(Call call, IOException e) {
                mShowPostsView.showError(e);
            }
        });
    }
}
