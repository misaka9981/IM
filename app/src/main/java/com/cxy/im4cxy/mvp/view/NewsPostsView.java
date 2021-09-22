package com.cxy.im4cxy.mvp.view;


import com.cxy.im4cxy.mvp.bean.NewsBean;

import java.util.List;

public interface NewsPostsView extends BmobView {
    void showPosts(List<NewsBean.NewslistBean> posts);
}
