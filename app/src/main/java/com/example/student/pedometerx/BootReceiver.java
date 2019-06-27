package com.example.student.pedometerx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service Stops","xddd");
        context.startService(new Intent(context, BootReceiver.class));
    }
}
