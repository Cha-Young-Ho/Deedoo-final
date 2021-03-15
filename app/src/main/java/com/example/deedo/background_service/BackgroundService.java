package com.example.deedo.background_service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.HOME_ETC.Home_activity;
import com.example.deedo.R;
import com.example.deedo.area.Area_Data;
import com.example.deedo.callback.Get_Area_info_onCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;

public class BackgroundService extends Service {
    public static Intent serviceIntent = null;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    DBHelperFirebase firebase;
    String userId;
    String DATE;

    @Override
    public void onCreate() {
        super.onCreate();
        firebase = new DBHelperFirebase();
        Log.d("222222222", "여기 실행");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d("3333333333", "여기 실행");
                Log.d("mylong", "Lat is" + locationResult.getLastLocation().getLatitude() +
                        ",  Lng is : " + locationResult.getLastLocation().getLongitude() + "id = " + userId);
                firebase.get_Area_info(new Get_Area_info_onCallback() {
                    @Override
                    public void get_Area_info_onCallback(ArrayList<Area_Data> Area_Data_list, Context con) { }
                    @Override
                    public void get_Area_info_onCallback(ArrayList<Area_Data> Area_Data_list) {
                        Log.v("background에서 Area 크기 받기", "");
                        Log.v("list size ======= ", ""+ Area_Data_list.size());
                        float distance = 30;
                        for (int i = 0; i < Area_Data_list.size(); i++) {
                            LatLng nowRocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                            LatLng registeredArea = new LatLng(Double.parseDouble(Area_Data_list.get(i).getTextView_latitude()), Double.parseDouble(Area_Data_list.get(i).getTextView_longitude()));
                            distance = calculateLocationDifference(nowRocation, registeredArea);
                            Log.v("사이 거리  = " , " " + distance);

                            if(distance < 30){
                                Log.v("위치로 진입", Area_Data_list.get(i).getTextView_name());
                                CalendarDay date = CalendarDay.today();
                                DATE = date.toString(); // ex : Calender{2021-02-28}

                                String[] parsedDATA = DATE.split("[{]"); // ex : [0] = Calender || [1] = 2021-02-28}

                                parsedDATA = parsedDATA[1].split("[}]"); // ex : [0] = 2021-02-28 || [1] = ""

                                parsedDATA = parsedDATA[0].split("-"); // ex : [0] = 2021 || [1] = 02 || [2] = 28
                                firebase.create_daily(userId, parsedDATA,  Area_Data_list.get(i).getTextView_name());
                                break;
                            }
                        }
                            if(distance >=30) {
                                Log.v("기타로 진입", "");
                                CalendarDay date = CalendarDay.today();
                                DATE = date.toString(); // ex : Calender{2021-02-28}

                                String[] parsedDATA = DATE.split("[{]"); // ex : [0] = Calender || [1] = 2021-02-28}

                                parsedDATA = parsedDATA[1].split("[}]"); // ex : [0] = 2021-02-28 || [1] = ""

                                parsedDATA = parsedDATA[0].split("-"); // ex : [0] = 2021 || [1] = 02 || [2] = 28
                                firebase.create_daily(userId, parsedDATA, "기타");
                            }



                        Log.v("지금 아이디 ======= ", ""+ userId);
                    }
                }, userId);


                //firebase.insert_daily();
                Intent intent = new Intent("ACT_LOC");
                intent.putExtra("latitude", locationResult.getLastLocation().getLatitude());
                intent.putExtra("longitude", locationResult.getLastLocation().getLongitude());

                sendBroadcast(intent);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
        Log.d("111111111", "여기 실행");
        Log.d("스타트에서 id = ", "" + serviceIntent.getStringExtra("id"));
        userId = serviceIntent.getStringExtra("id");
        initializeNotification();
        requestLocation();

        return super.onStartCommand(intent, flags, startId);
    }

    private void requestLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void initializeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("설정을 보려면 누르세요.");
        style.setBigContentTitle(null);
        style.setSummaryText("서비스 동작중");
        builder.setContentText(null);
        builder.setContentTitle(null);
        builder.setOngoing(true);
        builder.setStyle(style);
        builder.setWhen(0);
        builder.setShowWhen(false);
        Intent notificationIntent = new Intent(this, Home_activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1", "undead_service", NotificationManager.IMPORTANCE_NONE));
        }
        Notification notification = builder.build();
        startForeground(1, notification);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    private float calculateLocationDifference(LatLng lastLocation, LatLng firstLocation) {
        float[] dist = new float[1];
        Location.distanceBetween(lastLocation.latitude, lastLocation.longitude, firstLocation.latitude, firstLocation.longitude, dist);
        return dist[0];
    }
}
