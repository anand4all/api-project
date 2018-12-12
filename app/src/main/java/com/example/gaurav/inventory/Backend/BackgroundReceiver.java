package com.example.gaurav.inventory.Backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BackgroundReceiver extends BroadcastReceiver {
    SessionManager sessionManager;
    Context ctx;
    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        sessionManager = new SessionManager(context);
        if (sessionManager.getCsrfId() != "" && sessionManager.getSessionId() != "") {
            context.startService(new Intent(context, Backgroundservice.class));
        }
    }
}