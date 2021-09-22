package com.cxy.im4cxy.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxy.im4cxy.R;

import com.cxy.im4cxy.base.ParentWithNaviFragment;

import com.cxy.im4cxy.ui.PublishPostActivity;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DiscoverFragment extends ParentWithNaviFragment {




    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);

        return rootView;
    }



    public DiscoverFragment() {
    }

    @Override
    protected String title() {
        return "论坛";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.tv_error, R.id.fb_add_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_error:
                break;
            case R.id.fb_add_post:
                startActivity(new Intent(getActivity(), PublishPostActivity.class));
                break;
        }
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
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }
}
