package com.sinoautodiagnoseos.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Lanye on 2017/2/18.
 * 开启一个私有的服务进程进行接受推送消息，应用退出后依旧可以接受
 */

public class PushService extends Service {
    //获取消息线程
    private MessageThread messageThread = null;
    //点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;
    //通知栏消息
    private int messageNotificationID = 1000;
    private Notification messageNotification = null;
    private NotificationManager messageNotificationManager = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("=======onCreate");
    }

    public PushService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //YunTXSDKUtil.initSDK(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        Intent intent = new Intent("com.dbjtech.waiqin.destroy");
        sendBroadcast(intent);
        super.onDestroy();
    }

    /***
     * 从服务端获取消息
     *
     * @author zhanglei
     */
    class MessageThread extends Thread {
        //运行状态
        public boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {

                for (int j = 0; j < 86400; j++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(j);
                }

            }
        }
    }

    /***
     * 模拟了服务端的消息。实际应用中应该去服务器拿到message
     *
     * @return
     */
    public String getServerMessage() {
        return "yes";
    }
}
