package com.ignidata.api.plugin;

import android.app.Activity;
import android.widget.Toast;

import java.net.InetAddress;

/**
 * Created by Pedro on 23/03/2015.
 */
public class Utils {

    public static void showToast(Activity act, String msg) {
        Toast.makeText(act.getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isOnline() {
/*        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //may return null in case phone is in airplane mode
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();*/
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

}
