package com.cxy.im4cxy.bean;

import java.io.File;

import com.cxy.im4cxy.db.NewFriend;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


public class User extends BmobUser {

    private BmobFile avatar;

    public User() {
    }

    public User(NewFriend friend) {
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(new BmobFile(new File(friend.getAvatar())));
    }


    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }
}
