package com.example.deedo.background_service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.deedo.HOME_ETC.Home_activity;
import com.example.deedo.R;

import java.util.Calendar;

public class BackgroundService extends Service {
        private Thread mainThread;
        public static Intent serviceIntent = null;


        public BackgroundService() {
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            serviceIntent = intent;
            mainThread = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });
            mainThread.start();

            return START_NOT_STICKY;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            serviceIntent = null;
            setAlarmTimer();
            Thread.currentThread().interrupt();

            if (mainThread != null) {
                mainThread.interrupt();
                mainThread = null;
            }
        }

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public boolean onUnbind(Intent intent) {
            return super.onUnbind(intent);
        }



        protected void setAlarmTimer() {
            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.SECOND, 1);
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

            AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
        }

        private void sendNotification(String messageBody) {
            Intent intent = new Intent(this, Home_activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

            String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                            .setContentTitle("Service test")
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }