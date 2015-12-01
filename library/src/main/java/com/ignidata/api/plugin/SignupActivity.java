package com.ignidata.api.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by pedro on 26-02-2015.
 */
public class SignupActivity extends Activity {

    private WebView myWebView = null;
    private Client client = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_web_view);

        //myWebView = (WebView) findViewById(R.id.webview);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        myWebView = new WebView(this);

        myWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(myWebView);

        setContentView(layout);

        // clean cache
        for (File f : getCacheDir().listFiles()) {
            f.delete();
        }

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("SIGNUP JAVASCRIPT:", "" + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });


        myWebView.setWebViewClient(new WebViewClient());

        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.addJavascriptInterface(new SignupJSInterface(this), "Android");

        // to get jQuery mobile working from Jelly Bean onwards
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        }

        myWebView.getSettings().setDatabaseEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);


        //myWebView.loadUrl("file::///android_asset/signup.html");
       // myWebView.loadUrl("http://10.0.2.2:5000/static/signup.html");
    }


    public static String getUserAgent() {
        return System.getProperty("http.agent");
    }


    // if a user_key isn't stored in the device, a client object containing age, gender, country
    // and user agent shall be sent to the backend - the latter is to answer back, returning a
    // client object with the same data sent before along with a computed user hash code - which will
    // be stored at the plugin's directory in the device storage.



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

/*
    // if a user_key isn't stored in the device, a client object containing age, gender, country
    // and user agent shall be sent to the backend - the latter is to answer back, returning a
    // client object with the same data sent before along with a computed user hash code - which will
    // be stored at the plugin's directory in the device storage.
    private class GetClientAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.getClient(client, new Callback<Client>() {
                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println(retrofitError.getLocalizedMessage() + " -> "
                            + retrofitError.getKind().ordinal());
                }

                @Override
                public void success(Client arg0, Response response) {
                    Log.d("GET_CLIENT_RESPONSE: ", arg0.toString());
                    client = arg0;
                }
            });

            return null;

        }

    }
*/

    public class SignupJSInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        SignupJSInterface(Context c) {
            mContext = c;
        }


        /**
         * Show a toast from the web page
         */

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void leave(String toast) {
            showToast(toast);
            Intent intent = new Intent(mContext, SurveyActivity.class);

            // let's pass our client object in serialized form
            intent.putExtra("client", client);

            startActivity(intent);
            finish();
        }


        public boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //may return null in case phone is in airplane mode
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }

        @JavascriptInterface
        public void getUserSignup(String age, String gender, String language, String profession,String city, String country, String postalCode) {
           String userID = "";
            client = new Client(age, gender, profession, city, country, postalCode, language,userID);
            client.age = age;
            client.gender = gender;
            client.language = language;
            client.profession = profession;
            client.country = country;
            client.postalCode = postalCode;
            client.country = country;
            client.city = city;


            //client.userAgent = SignupActivity.getUserAgent();
            //new PostClientAsyncTask().execute();
        }


    }


}