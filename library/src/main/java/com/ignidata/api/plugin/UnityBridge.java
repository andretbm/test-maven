package com.ignidata.api.plugin;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;

public class UnityBridge extends Activity {


    //Context mContext = null;
    private String TAG = "Plug.StartActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Activity created");
        Toast.makeText(this, "Activity created", Toast.LENGTH_SHORT).show();

        //mContext = this;
    }


    public static void Call(Activity activity, final String apiKey, final double pricepoint, final boolean debug) {

        Toast.makeText(activity, "'init' invoked - API key:" + apiKey + "; pricepoint:" + pricepoint + "; debug:" + debug, Toast.LENGTH_SHORT).show();
        // temporariamente, apontamos para a MainActivity
        // Intent myIntent = new Intent(context, SurveyActivity.class);
        Intent myIntent = new Intent(activity, SurveyActivity.class);
        myIntent.putExtra("apiKey", apiKey);
        myIntent.putExtra("pricepoint", pricepoint);
        myIntent.putExtra("debug", debug);
        activity.startActivity(myIntent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // not sure about String.valueOf(data) but hélas
        // i'll think of something when time comes
        if (requestCode == Globals.REQUEST_SURVEY) {
            UnityPlayer.UnitySendMessage(String.valueOf(requestCode), String.valueOf(resultCode), String.valueOf(data));
        }

    }


}


/**
 *
 *	UnityBridge.java
 *
 *
 *//*



package com.ignidata.api.plugin;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UnityBridge {

    private Context context = null;
    //private static Activity instance = null;
    private static UnityBridge instance = null;

    public UnityBridge() {
        this.instance = this;
    }


    // temporario
    // public static SurveyActivity instance() {

*/
/*    public static MainActivity instance() {
        if (instance == null) {
            // temporariamente, apontamos para a MainActivity
            //instance = new SurveyActivity();
            instance = new MainActivity();
        }
        return (MainActivity) instance;
    }
*//*


    public static UnityBridge instance() {
        if (instance == null) {
            // temporariamente, apontamos para a MainActivity
            //instance = new SurveyActivity();
            instance = new UnityBridge();
        }
        return instance;
    }


    public void setContext(Context context) {
        this.context = context;
    }


    public void init(final String apiKey, final double pricepoint, final boolean debug) {
        Toast.makeText(this.context, "'init' invoked - API key:" + apiKey + "; pricepoint:" + pricepoint + "; debug:" + debug, Toast.LENGTH_SHORT).show();


        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                // temporariamente, apontamos para a MainActivity
                // Intent myIntent = new Intent(context, SurveyActivity.class);
                Intent myIntent = new Intent(context, MainActivity.class);
                myIntent.putExtra("apiKey", apiKey);
                myIntent.putExtra("pricepoint", pricepoint);
                myIntent.putExtra("debug", debug);
                context.startActivity(myIntent);
            }
        });
    }

    // just for testing
    public void showMessage(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show();
    }

}
*/
