/**
 *
 *	UnityBridge.java
 *
 *
 */


package com.ignidata.api.plugin;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Bridge {

    private Context context = null;
    //private static Activity instance = null;
    private static Bridge instance = null;

    public Bridge() {
        this.instance = this;
    }

    public static Bridge instance() {
        if (instance == null) {
            // temporariamente, apontamos para a MainActivity
            //instance = new SurveyActivity();
            instance = new Bridge();
        }
        return instance;
    }


    public void setContext(Context context) {
        this.context = context;
    }


    public void init(final String apiKey, final double pricepoint, final boolean debug) {
        Toast.makeText(this.context, "'init' invoked - API key:" + apiKey + "; pricepoint:" + pricepoint + "; debug:" + debug, Toast.LENGTH_SHORT).show();


        //((Activity) context).runOnUiThread(new Runnable() {
        //    public void run() {
        // temporariamente, apontamos para a MainActivity
        // Intent myIntent = new Intent(context, SurveyActivity.class);
        Intent myIntent = new Intent(context, SurveyActivity.class);
        myIntent.putExtra("apiKey", apiKey);
        myIntent.putExtra("pricepoint", pricepoint);
        myIntent.putExtra("debug", debug);
        context.startActivity(myIntent);
        //    }
        //});
    }

    public void showMessage(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }

}
