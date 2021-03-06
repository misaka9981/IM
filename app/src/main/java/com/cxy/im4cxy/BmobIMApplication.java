package com.cxy.im4cxy;

import android.app.Application;
import android.content.Context;

import com.cxy.im4cxy.base.UniversalImageLoader;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import androidx.multidex.MultiDex;
import cn.bmob.newim.BmobIM;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;


//TODO 集成：1.7、自定义Application，并在AndroidManifest.xml中配置
public class BmobIMApplication extends Application {

    private static BmobIMApplication INSTANCE;

    public static BmobIMApplication INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //TODO 集成：1.8、初始化IM SDK，并注册消息接收器，只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {

            Bmob.resetDomain("http://imsdk.zhixiangshu.top/8/");
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
            super.onCreate();
            /**
             * 保存设备信息，用于推送功能
             */
            BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
                @Override
                public void done(BmobInstallation bmobInstallation, BmobException e) {
                    if (e == null) {
                        Logger.i(bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
                    } else {
                        Logger.e(e.getMessage());
                    }
                }
            });
            /**
             * 启动推送服务
             */
            BmobPush.startWork(this);
        }
        Logger.addLogAdapter(new AndroidLogAdapter());
        UniversalImageLoader.initImageLoader(this);
    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
