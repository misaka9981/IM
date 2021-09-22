package com.cxy.im4cxy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.cxy.im4cxy.R;
import com.cxy.im4cxy.adapter.base.BaseViewHolder;
import com.cxy.im4cxy.base.ImageLoaderFactory;
import com.cxy.im4cxy.bean.User;
import com.cxy.im4cxy.ui.UserInfoActivity;

public class SearchUserHolder extends BaseViewHolder {

    @BindView(R.id.avatar)
    public ImageView avatar;
    @BindView(R.id.name)
    public TextView name;
    @BindView(R.id.btn_add)
    public Button btn_add;

    public SearchUserHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_search_user, onRecyclerViewListener);
    }

    @Override
    public void bindData(Object o) {
        final User user = (User) o;
        ImageLoaderFactory.getLoader().loadAvator(avatar, user.getAvatar() == null ? null : user.getAvatar().getFileUrl(), R.mipmap.icon_message_press);
        name.setText(user.getUsername());
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//查看个人详情
                Bundle bundle = new Bundle();
                bundle.putSerializable("u", user);
                startActivity(UserInfoActivity.class, bundle);
            }
        });
    }
}