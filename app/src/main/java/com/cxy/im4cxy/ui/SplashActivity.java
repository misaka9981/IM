package com.cxy.im4cxy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.cxy.im4cxy.R;
import com.cxy.im4cxy.bean.User;
import com.cxy.im4cxy.base.BaseActivity;
import com.cxy.im4cxy.model.UserModel;

/*
启动
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(LogActivity.class,null,true);
                }else{
                    startActivity(MainActivity.class,null,true);
                }
            }
        },1000);

    }
}
