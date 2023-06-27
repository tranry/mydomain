package com.example.mydomain;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Application extends android.app.Application {
    public static final String chanelID ="Id";

    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }

    private void createChannelNotification() {
        //Kiem tra phien ban code,api android >8
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            //tao chanel id cho phien ban android
            NotificationChannel channel= new NotificationChannel(chanelID,"Thong bao", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
