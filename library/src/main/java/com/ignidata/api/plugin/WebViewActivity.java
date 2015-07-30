/*
package com.ignidata.api.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


*/
/**
 * Created by pedro on 02/02/2015.
 *//*

public class WebViewActivity extends Activity {

    private WebView myWebView;
    private Client client;
    private Survey survey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // WebView myWebView = (WebView) findViewById(R.id.webview);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        //myWebView.addJavascriptInterface(new SignupJSInterface(this), "Android");

        //new GetEndpointsAsyncTask().execute();

        // new GetSurveyAsyncTask().execute();

        //String customHtml = "<input type=\"button\" value=\"Send\" onClick=\"leave('Thank you for answering the survey!')\" /><script type=\"text/javascript\">function leave(toast) {Android.leave(toast);}</script>";
        //myWebView.loadData(customHtml, "text/html", "UTF-8");
        myWebView.loadUrl("file::///android_asset/signup.html");
        //myWebView.loadData(readTextFromResource(R.raw.signup), "text/html", "UTF-8");
    }


*/
/*    private String readTextFromResource(int resourceID)
    {
        InputStream raw = getResources().openRawResource(resourceID);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int i;
        try
        {
            i = raw.read();
            while (i != -1)
            {
                stream.write(i);
                i = raw.read();
            }
            raw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return stream.toString();
    }*//*



    public static String getUserAgent() {
        return System.getProperty("http.agent");
    }

    // static just for testing
    private static class GetEndpointsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.getEndpoints(new Callback<JsonObject>() {

                @Override
                public void failure(RetrofitError arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void success(JsonObject arg0, Response arg1) {
                    // Log.d("RESPONSE: ", arg0.get("clients").toString());
                    // Log.d("RESPONSE", arg0.get("surveys").toString());
                    Log.d("GET_ENDPOINTS_RESPONSE:", arg0.toString());
                }
            });

            return null;

        }

*/
/*        @Override
        protected void onPostExecute(String result) {
            Log.d("onPostExecute()", "HttpAsyncTask:" + result);
        }*//*


    }

    // static just for testing
    private static class GetClientAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.getClient(new Client(), new Callback<Client>() {
                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println(retrofitError.getLocalizedMessage() + " -> "
                            + retrofitError.getKind().ordinal());
                }

                @Override
                public void success(Client arg0, Response response) {
                    //myWebView.loadData(json.get("payload").toString(), "text/html", "UTF-8");
                    Log.d("GET_CLIENT_RESPONSE: ", arg0.toString());
                }
            });

            return null;

        }

    }

    // static just for testing
    private static class PostClientAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.postClient(new Client(), new Callback<Client>() {
                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println(retrofitError.getLocalizedMessage() + " -> "
                            + retrofitError.getKind().ordinal());
                }

                @Override
                public void success(Client arg0, Response response) {
                    //myWebView.loadData(json.get("payload").toString(), "text/html", "UTF-8");
                    Log.d("POST_CLIENT_RESPONSE: ", arg0.toString());
                }
            });

            return null;

        }

    }

    // static just for testing
    private static class GetSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.getSurvey(new Survey(), new Callback<Survey>() {
                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println(retrofitError.getLocalizedMessage() + " -> "
                            + retrofitError.getKind().ordinal());
                }

                @Override
                public void success(Survey survey, Response response) {
                    //System.out.println(json.get("payload").toString());
                    //System.out.println(response.getStatus());
                    //myWebView.loadData(json.get("payload").toString(), "text/html", "UTF-8");
                    Log.d("GET_SURVEY_RESPONSE: ", survey.toString());
                }
            });

            return null;

        }

    }

    // static just for testing
    private static class PostSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.postSurvey(new Survey(), new Callback<Survey>() {
                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println(retrofitError.getLocalizedMessage() + " -> "
                            + retrofitError.getKind().ordinal());
                }

                @Override
                public void success(Survey arg0, Response response) {
                    //myWebView.loadData(json.get("payload").toString(), "text/html", "UTF-8");
                    Log.d("POST_SURVEY_RESPONSE: ", arg0.toString());
                }
            });

            return null;

        }

    }

    // static just for testing
    private static class PutSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.putSurvey(new Survey(), new Callback<Survey>() {
                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println(retrofitError.getLocalizedMessage() + " -> "
                            + retrofitError.getKind().ordinal());
                }

                @Override
                public void success(Survey arg0, Response response) {
                    //myWebView.loadData(json.get("payload").toString(), "text/html", "UTF-8");
                    Log.d("PUT_SURVEY_RESPONSE: ", arg0.toString());
                }
            });

            return null;

        }

    }


    public class JSInterface {
        Context mContext;
        Long startTime;
        Long[] elapsedTime = new Long[survey.questions.size()];
        int MINIMUM_TIME_ANSWER = 2000;

        */
/**
         * Instantiate the interface and set the context
         *//*

        JSInterface(Context c) {
            mContext = c;
        }

*/
/*
        *//*


        */
/**
         * Show a toast from the web page
         *//*


        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void leave(String toast) {
            showToast(toast);
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();
        }

*/
/*
        @JavascriptInterface
        public void startTimer() {
            startTime = System.currentTimeMillis();
        }

        @JavascriptInterface
        public void elapsedTime(int index) {
            elapsedTime[index] = ((new Date()).getTime() - startTime);
        }


        @JavascriptInterface
        public void validate() {
            long totalResponseTime = 0L;
            for (long time : elapsedTime) {
                totalResponseTime += time;
            }
            if ((totalResponseTime / elapsedTime.length) <= MINIMUM_TIME_ANSWER) {
                showToast("You answered the survey too fast...Did you even read the questions? Come on, give it another try!");
                // reset survey...
                //TODO check how to pass data to the webview thru the interface!
            } else {
                // submit survey!
                new PutSurveyAsyncTask().execute();
            }
        }

        *//*

*/
/**
 * On _submit_, this method is to be invoked. A JSON struct is then persisted on a folder
 * named 'com.ignidata.plugin' and a background service will be launched, only exiting after
 * sending and unlocking is successful. Then, a notification for the user is to be shown.
 *//*
*/
/*


        @JavascriptInterface
*//*

*/
/*        public void processSurvey(json) {
        // should validate before... TODO
            if (isOnline()) postSurvey();
            else saveSurvey(json);
        }*//*
*/
/*



*//*

*/
/*
        public void saveSurvey(JSONObject json) {
            try {
                File.createTempFile("surveyAnswersRandomHashGoesHere", null, mContext.getCacheDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*//*
*/
/*


        public boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //may return null in case phone is in airplane mode
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }

        @JavascriptInterface
        public void getUserSignup(int age, String gender, String country) {
            client = new Client();
            client.age = age;
            client.gender = gender;
            client.country = country;
            client.userAgent = WebViewActivity.getUserAgent();
        }

*//*

*/
/*
        @JavascriptInterface
        public void getPartialResults(Map<Integer, String> answers) {
            survey = getSurveyInstance();
            //survey.questions = answers;
            if (isOnline()) new GetSurveyAsyncTask().execute();
            // else saveSurvey(new JSONObject(survey).toString());
        }
*/
/*

*//*

    }

    public class SignupJSInterface {
        Context mContext;
        Long startTime;
        Long[] elapsedTime = new Long[survey.questions.size()];
        int MINIMUM_TIME_ANSWER = 2000;

        */
/**
         * Instantiate the interface and set the context
         *//*

        SignupJSInterface(Context c) {
            mContext = c;
        }

*/
/*
        *//*


        */
/**
         * Show a toast from the web page
         *//*


*/
/*
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void leave(String toast) {
            showToast(toast);
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();
        }
*//*


*/
/*
        @JavascriptInterface
        public void startTimer() {
            startTime = System.currentTimeMillis();
        }

        @JavascriptInterface
        public void elapsedTime(int index) {
            elapsedTime[index] = ((new Date()).getTime() - startTime);
        }


        @JavascriptInterface
        public void validate() {
            long totalResponseTime = 0L;
            for (long time : elapsedTime) {
                totalResponseTime += time;
            }
            if ((totalResponseTime / elapsedTime.length) <= MINIMUM_TIME_ANSWER) {
                showToast("You answered the survey too fast...Did you even read the questions? Come on, give it another try!");
                // reset survey...
                //TODO check how to pass data to the webview thru the interface!
            } else {
                // submit survey!
                new PutSurveyAsyncTask().execute();
            }
        }

        *//*

*/
/**
 * On _submit_, this method is to be invoked. A JSON struct is then persisted on a folder
 * named 'com.ignidata.plugin' and a background service will be launched, only exiting after
 * sending and unlocking is successful. Then, a notification for the user is to be shown.
 *//*
*/
/*


        @JavascriptInterface
*//*

*/
/*        public void processSurvey(json) {
        // should validate before... TODO
            if (isOnline()) postSurvey();
            else saveSurvey(json);
        }*//*
*/
/*



*//*

*/
/*
        public void saveSurvey(JSONObject json) {
            try {
                File.createTempFile("surveyAnswersRandomHashGoesHere", null, mContext.getCacheDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*//*
*/
/*


        public boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //may return null in case phone is in airplane mode
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }

        @JavascriptInterface
        public void getUserSignup(int age, String gender, String country) {
            client = new Client();
            client.age = age;
            client.gender = gender;
            client.country = country;
            client.userAgent = WebViewActivity.getUserAgent();
        }

*//*

*/
/*
        @JavascriptInterface
        public void getPartialResults(Map<Integer, String> answers) {
            survey = getSurveyInstance();
            //survey.questions = answers;
            if (isOnline()) new GetSurveyAsyncTask().execute();
            // else saveSurvey(new JSONObject(survey).toString());
        }
*/
/*

*//*

    }

}*/
