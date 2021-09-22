package com.cxy.im4cxy.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;


import butterknife.BindView;
import butterknife.ButterKnife;
import com.cxy.im4cxy.R;
import com.cxy.im4cxy.base.BaseActivity;



public class PublishPostActivity extends BaseActivity{


    @BindView(R.id.tool_bar)
    Toolbar mToolBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_post);
        ButterKnife.bind(this);
        mToolBar.setTitle("发布帖子");
        mToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color_theme));

    }
}
