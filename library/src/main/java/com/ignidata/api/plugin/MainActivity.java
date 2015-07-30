/*

<!--<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >

    <Button android:id="@+id/btnOpenWebView"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="@string/open_web_view"
        android:onClick="openWebView" />

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="SAVE A FUCKING FILE"
        android:id="@+id/button"
        android:layout_gravity="center_horizontal"
        android:onClick="saveSurvey"/>

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="YO, CHECK THIS MUTHAFUCKIN JSON ENTITY"
        android:id="@+id/button2"
        android:layout_gravity="center_horizontal"
        android:onClick="createSerializableEntity"/>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceSmall"
        android:id="@+id/textView"
        android:layout_gravity="center_horizontal"
        android:text="json will show up here..."
        />

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="TEST READ CACHED SURVEY"
        android:id="@+id/button3"
        android:layout_gravity="center_horizontal"
        android:onClick="testReadCachedSurvey" />

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="TEST WRITE USERID"
        android:id="@+id/button4"
        android:layout_gravity="center_horizontal"
        android:onClick="saveUserIdTest"/>

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="TEST USER AGENT"
        android:id="@+id/button5"
        android:layout_gravity="center_horizontal"
        android:onClick="getUserAgent" />

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="TEST SAVE SURVEY"
        android:id="@+id/button6"
        android:layout_gravity="center_horizontal"
        android:onClick="saveSurvey" />

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="TEST GET USERID"
        android:id="@+id/button7"
        android:layout_gravity="center_horizontal"
        android:onClick="getUserIdTest" />

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="OPEN SIGNUP ACTIVITY"
        android:id="@+id/button8"
        android:layout_gravity="center_horizontal"
        android:onClick="openSignupActivity" />

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="OPEN SURVEY ACTIVITY"
        android:id="@+id/button9"
        android:layout_gravity="center_horizontal"
        android:onClick="openSurveyActivity" />

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="OPEN WEBVIEW"
        android:id="@+id/button10"
        android:layout_gravity="center_horizontal"
        android:onClick="openWebView" />

</LinearLayout>-->

<!-- menu_main.xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools" tools:context=".MainActivity">
    <item android:id="@+id/action_settings" android:title="@string/action_settings"
        android:orderInCategory="100" />
</menu>
-->

package com.ignidata.api.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, UniqueIdentifierService.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Method to start the service
    public void startService(View view) {
        startService(new Intent(getBaseContext(), UniqueIdentifierService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), UniqueIdentifierService.class));
    }

    public void openWebView(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }

    public void openSignupActivity(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void openSurveyActivity(View view) {
        Intent intent = new Intent(this, SurveyActivity.class);
        // startActivity(intent);
        startActivityForResult(intent, Globals.REQUEST_SURVEY);
    }


    //public void saveSurvey(JSONObject json) {
    public void saveSurvey(View view) {
        try {
            saveSurveyTest("survey-" + new Date().getTime(), new Survey());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void saveSurveyTest(String s, Survey survey) throws FileNotFoundException {
        File path = new File(getFilesDir() + "/cached-surveys");
        path.mkdir();

        File newFile = new File(path, s);
        try {
            PrintWriter pw = new PrintWriter(newFile);
            BufferedWriter br = new BufferedWriter(pw);
            new Gson().toJson(survey, pw);
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createSerializableEntity(View view) {
        Toast.makeText(this, "Survey created", Toast.LENGTH_SHORT);

        TextView txtView = (TextView) findViewById(R.id.textView);
        //txtView.setText((new JsonObject()).toString());
        txtView.setText("Shit happens");

    }


    public void testReadCachedSurvey(View view) {
        String text = null;
        try {
            text = readCachedSurvey();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "IOException, dawg", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this, "JSONException, dawg", Toast.LENGTH_SHORT).show();
        }

    }


    public String readCachedSurvey() throws IOException, JSONException {
        File file = new File(getFilesDir() + "/cached-surveys");
        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader br;

        if (file.exists()) {
            for (File f : file.listFiles()) {
                br = new BufferedReader(new FileReader(f));

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        }

        //JSONTokener tokener = new JSONTokener(sb.toString());
        //JSONObject json = new JSONObject(tokener);
        return sb.toString();
    }

    public void getUserAgent(View view) {
        //return System.getProperty("http.agent");
        Toast.makeText(this, System.getProperty("http.agent"), Toast.LENGTH_SHORT).show();
    }

    public void saveUserIdTest(View view) {
        try {
            saveUserId(String.valueOf(UUID.randomUUID()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveUserId(String userId) throws FileNotFoundException {
        File path = new File(String.valueOf(getFilesDir()));
        path.mkdir();

        File newFile = new File(path, "user_key");
        try {
            PrintWriter pw = new PrintWriter(newFile);
            BufferedWriter br = new BufferedWriter(pw);
            pw.println(userId);
            pw.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getUserId() {
        File path = new File(String.valueOf(getFilesDir()));
        File userId = new File(path, "user_key");

        if (userId.exists())
            try {
                return new BufferedReader(new FileReader(userId)).readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }


        return "";
    }


    public void getUserIdTest(View view) {
        Toast.makeText(this, getUserId(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String msg = data.getExtras().getString("msg");

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

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        }
    }


}
*/
