package com.cxy.im4cxy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import com.cxy.im4cxy.R;
import com.cxy.im4cxy.adapter.base.BaseViewHolder;
import com.cxy.im4cxy.base.ImageLoaderFactory;
import com.cxy.im4cxy.bean.User;
import com.cxy.im4cxy.ui.UserInfoActivity;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 接收到的文本类型
 */
public class ReceiveTextHolder extends BaseViewHolder {

    @BindView(R.id.iv_avatar)
    protected ImageView iv_avatar;

    @BindView(R.id.tv_time)
    protected TextView tv_time;

    @BindView(R.id.tv_message)
    protected TextView tv_message;

    public ReceiveTextHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_message, onRecyclerViewListener);
    }

    @OnClick({R.id.iv_avatar})
    public void onAvatarClick(View view) {

    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        tv_time.setText(time);
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        ImageLoaderFactory.getLoader().loadAvator(iv_avatar, info != null ? info.getAvatar() : null, R.mipmap.icon_message_press);
        String content = message.getContent();
        tv_message.setText(content);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + info.getName() + "的头像");
                Bundle bundle = new Bundle();
                User user = new User();
                BmobFile bmobFile = new BmobFile();
                bmobFile.setUrl(info.getAvatar());
                user.setAvatar(bmobFile);
                user.setUsername(info.getName());
                user.setObjectId(info.getUserId());
                bundle.putSerializable("u", user);
                startActivity(UserInfoActivity.class, bundle);
            }
        });
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + message.getContent());
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        tv_message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });
    }

    public void showTime(boolean isShow) {
        tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}