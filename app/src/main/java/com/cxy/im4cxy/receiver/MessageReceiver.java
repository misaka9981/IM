package com.cxy.im4cxy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import com.cxy.im4cxy.R;
import com.cxy.im4cxy.util.Notify;
import cn.bmob.push.PushConstants;



public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Logger.i("客户端收到推送消息：" + msg);
            Notify notify = new Notify(context);
            notify.setId(msg.hashCode());
            notify.setTitle(msg);
            notify.setAutoCancel(true);
            notify.setSmallIcon(R.mipmap.icon_message_press);
            notify.notification();
            notify.show();
        }
    }
}
