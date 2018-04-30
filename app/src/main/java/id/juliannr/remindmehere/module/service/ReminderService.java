package id.juliannr.remindmehere.module.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import id.juliannr.remindmehere.R;
import id.juliannr.remindmehere.data.controller.ReminderController;
import id.juliannr.remindmehere.data.model.Reminder;
import id.juliannr.remindmehere.util.DateHelper;

/**
 * Created by juliannr on 23/04/18.
 */

public class ReminderService extends Service {

    private ReminderController controller;
    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 0f;
    private int counter = 0;

    private class LocationListener implements android.location.LocationListener{
        Location lastLocation;

        public LocationListener(String provider) {
            lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e("TES", "onLocationChanged: " + location.getLatitude() + "/"  + location.getLongitude());
            lastLocation.set(location);
            long[] vibratePattern = {500,500,500,500,500,500,500,500,500};
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder
                    (getApplicationContext())
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("There is something you must do near your location")
                    .setLights(Color.BLUE, 500, 500)
                    .setVibrate(vibratePattern)
                    .setStyle(new NotificationCompat.InboxStyle())
                    .setSound(sound);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context
                    .NOTIFICATION_SERVICE);

            controller = ReminderController.getInstance();
            List<Reminder> reminders = controller.getAll();
            float[] dist = new float[1];
            Date date = new Date();
            for(Reminder reminder: reminders){
                if(date.after(DateHelper.fromDueDate(reminder.getDueDate()))){
                    controller.delete(reminder);
                }else{
                    try{
                        Location.distanceBetween(location.getLatitude(), location.getLongitude(), Double
                                .parseDouble
                                        (reminder.getLatitude()), Double.parseDouble(reminder.getLongitude()), dist);
                        if(dist[0]/100 <= 1){
                            notificationBuilder.setContentText(reminder.getDescription());
                            notificationManager.notify(1, notificationBuilder.build());
                        }

                    }catch (Exception e){
                    }
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    LocationListener[] locationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        initializeLocationManager();
        try{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[1]);
        }catch (java.lang.SecurityException ex){
           ex.printStackTrace();
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[0]);
        }catch (java.lang.SecurityException ex){
            ex.printStackTrace();
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
    }

    private void initializeLocationManager() {
        if(locationManager == null){
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context
                    .LOCATION_SERVICE);
        }
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
        if(locationManager != null){
            for(int i = 0; i< locationListeners.length; i++){
                try{
                    locationManager.removeUpdates(locationListeners[i]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        Intent broadcastIntent = new Intent("id.juliannr.remindmehere.module.service" +
                ".ReminderRestart");
        sendBroadcast(broadcastIntent);
        stopTimerTask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    private void startTimer(){
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000);
    }

    private void initializeTimerTask(){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i("TES", "run: " + (counter++));
            }
        };
    }

    private void stopTimerTask(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
