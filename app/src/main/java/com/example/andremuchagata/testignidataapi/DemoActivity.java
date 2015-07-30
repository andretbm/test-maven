package com.example.andremuchagata.testignidataapi;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ignidata.api.plugin.Globals;
import com.ignidata.api.plugin.SurveyActivity;




public class DemoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

    }

    public void openMainActivity(View view) {
        SurveyActivity.initForResult(this, Globals.USING_ANDROID);
        //Intent intent = new Intent(this, SurveyActivity.class);
        // startActivity(intent);
        //startActivityForResult(intent, Globals.REQUEST_SURVEY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Globals.REQUEST_SURVEY) {
            if (resultCode == Globals.RESULT_SUCCESS) {
                // ...
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
}
