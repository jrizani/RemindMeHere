package id.juliannr.remindmehere.module.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by juliannr on 23/04/18.
 */

public class ReminderRestart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TES", "onReceive: " + "Service Stopped and will restarted");
        context.startService(new Intent(context, ReminderService.class));
    }
}
