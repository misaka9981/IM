package com.cxy.im4cxy.adapter;

import android.content.Context;
import android.view.View;

import java.util.Collection;

import com.cxy.im4cxy.R;
import com.cxy.im4cxy.adapter.base.BaseRecyclerAdapter;
import com.cxy.im4cxy.adapter.base.BaseRecyclerHolder;
import com.cxy.im4cxy.adapter.base.IMutlipleItem;
import com.cxy.im4cxy.bean.Friend;
import com.cxy.im4cxy.bean.User;
import com.cxy.im4cxy.db.NewFriendManager;


public class ContactAdapter extends BaseRecyclerAdapter<Friend> {

    public static final int TYPE_NEW_FRIEND = 0;
    public static final int TYPE_ITEM = 1;

    public ContactAdapter(Context context, IMutlipleItem<Friend> items, Collection<Friend> datas) {
        super(context, items, datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, Friend friend, int position) {
        if (holder.layoutId == R.layout.item_contact) {
            User user = friend.getFriendUser();
            //好友头像
            holder.setImageView(user == null || user.getAvatar() == null ? null : user.getAvatar().getFileUrl(), R.mipmap.icon_message_press, R.id.iv_recent_avatar);
            //好友名称
            holder.setText(R.id.tv_recent_name, user == null ? "未知" : user.getUsername());
        } else if (holder.layoutId == R.layout.header_new_friend) {
            if (NewFriendManager.getInstance(context).hasNewFriendInvitation()) {
                holder.setVisible(R.id.iv_msg_tips, View.VISIBLE);
            } else {
                holder.setVisible(R.id.iv_msg_tips, View.GONE);
            }
        }
    }

}
