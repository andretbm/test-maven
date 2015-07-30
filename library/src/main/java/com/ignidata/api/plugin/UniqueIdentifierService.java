/*
package com.ignidata.api.plugin;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.accounts.Account;

import java.util.UUID;

public class UniqueIdentifierService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "UniqueIdentifierService Started", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "UniqueIdentifierService Destroyed", Toast.LENGTH_LONG).show();
    }

    public String getUniqueId() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10
                + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10; //13 digits
        return m_szDevIDShort;
    }

    public void getLocation() {

    }

}*/
