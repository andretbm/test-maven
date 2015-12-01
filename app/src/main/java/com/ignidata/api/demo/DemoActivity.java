package com.ignidata.api.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ignidata.api.plugin.Globals;
import com.ignidata.api.plugin.SurveyActivity;
import com.ignidata.api.plugin.Utils;


public class DemoActivity extends Activity {

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

    }

    public void openMainActivity(View view) {

        Log.i("TAG",Boolean.toString(isNetworkAvailable()));

        if(isNetworkAvailable())
        {
            SurveyActivity.initForResult(this, Globals.USING_ANDROID);
        }
        else
        {
            Utils.showToast(this,"You need a working connection to get our goodies! Double-check your Wi-Fi or mobile data connection" +
                    "!");
        }

        //Intent intent = new Intent(this, SurveyActivity.class);
        // startActivity(intent);
        //startActivityForResult(intent, Globals.REQUEST_SURVEY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Globals.REQUEST_SURVEY) {
            if (resultCode == Globals.RESULT_SUCCESS) {
                // ...
                Utils.showToast(this, "Your survey has been submitted.");
            }

            if (resultCode == Globals.RESULT_CANCEL) {
                // ...
            }

            if (resultCode == Globals.RESULT_ERROR) {
                // ...
            }

            if (resultCode == Globals.RESULT_INVALID) {
                // ...
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
