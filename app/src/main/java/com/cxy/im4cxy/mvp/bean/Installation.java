package com.cxy.im4cxy.mvp.bean;

import com.cxy.im4cxy.bean.User;
import cn.bmob.v3.BmobInstallation;


public class Installation extends BmobInstallation {

    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
