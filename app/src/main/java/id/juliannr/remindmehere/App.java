package id.juliannr.remindmehere;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import id.juliannr.remindmehere.module.service.ReminderService;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by juliannr on 19/04/18.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //realm
        Realm.init(this);
        configRealm();

        Intent serviceIntent = new Intent(getApplicationContext(), ReminderService.class);
        if(!isServiceRun(ReminderService.class)){
            startService(serviceIntent);
        }
    }

    private boolean isServiceRun(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context
                .ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices
                (Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    private void configRealm() {
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("RememberMeHere.realm")
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);
    }
}
