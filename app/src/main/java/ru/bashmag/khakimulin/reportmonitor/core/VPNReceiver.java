package ru.bashmag.khakimulin.reportmonitor.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.Callable;

/**
 * Created by Mark Khakimulin on 07.12.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class VPNReceiver extends BroadcastReceiver {

    Callable onVpnConnected;

    public VPNReceiver(Callable callable) {
        onVpnConnected = callable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (onVpnConnected != null) {
            try {
                onVpnConnected.call();
            } catch (Exception e) {

            }
        }
    }
}
