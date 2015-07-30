package com.ignidata.api.plugin;

/**
 * Created by pedro on 26-02-2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.unity3d.player.UnityPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SurveyActivity extends Activity {


    private WebView myWebView = null;
    private Client client = null;
    private Survey partialSurvey = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if Android version is 4.0.3+
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            Utils.showToast(this, "Unfortunately, you can only use Ignidata Surveys on devices " +
                    "running Android 4.0.3 or most recent versions!");
            setResult(Globals.RESULT_ERROR);

            if (getIntent().hasExtra("platform")) {
                if (getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    sendResultToUnityPlayer(getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_ERROR);
                }
            }

            finish();
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        myWebView = new WebView(this);
        myWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout.addView(myWebView);
        setContentView(layout);


        //setContentView(R.layout.activity_web_view);

        //myWebView = (WebView) findViewById(R.id.webview);

/*
        // clean cache
        for (File f : getCacheDir().listFiles()) {
            f.delete();
        }

        // get relevant endpoints
        new GetEndpointsAsyncTask().execute();

        // if intent has this extra, obviously it comes from SignupActivity
        Intent intent = getIntent();

        if (intent.hasExtra("client")) {
            client = (Client) intent.getSerializableExtra("client");
        }

        // check if client exists already
        if (client == null) {

            String userId = getUserId();

            // if user key is not present
            if (userId.equals("")) {

                // get to signup user first
                Intent signupIntent = new Intent(this, SignupActivity.class);
                startActivity(signupIntent);
                finish();
            }
        }
*/
        myWebView.setWebChromeClient(new WebChromeClient());
        //myWebView.setWebChromeClient(new WebChromeClient() {
        //    @Override
        //    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        //        Log.d("SURVEY JAVASCRIPT:", "" + consoleMessage.message());
        //        return super.onConsoleMessage(consoleMessage);
        //    }
        //});
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new SurveyJSInterface(this), "Android");

        // to get jQuery mobile working from Jelly Bean onwards
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        }
        //myWebView.loadUrl("http://10.0.2.2:5000/static/surveymock.html");

        //myWebView.getSettings().setDatabaseEnabled(true);
        //myWebView.getSettings().setDomStorageEnabled(true);

        getAsyncSurvey();
        //myWebView.loadUrl("http://surveys.ignidata.com/getMobileSurvey");

    }

    @Deprecated
    public void signupClient() {
        // check if userId exists
        String userId = getUserId();
        if (userId.equals("")) {

            // get to signup user first
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        //setResult(Globals.RESULT_CANCEL, getIntent().putExtra("msg", "You cancelled the survey."));
        Utils.showToast(SurveyActivity.this, "You cancelled the survey.");
        setResult(Globals.RESULT_CANCEL);

        if (getIntent().hasExtra("platform")) {
            if (getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                sendResultToUnityPlayer(getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_CANCEL);
            }
        }

        finish();
    }


    // static just for testing
    private class GetEndpointsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.getEndpoints(new Callback<JsonObject>() {

                @Override
                public void failure(RetrofitError arg0) {

                }

                @Override
                public void success(JsonObject arg0, Response arg1) {
                    // Log.d("RESPONSE: ", arg0.get("clients").toString());
                    // Log.d("RESPONSE", arg0.get("surveys").toString());
                    //Log.d("GET_ENDPOINTS_RESPONSE:", arg0.toString());
                    //get endpoints
                    Globals.endpoints.put("clients", arg0.get("clients").toString());
                    Globals.endpoints.put("surveys", arg0.get("surveys").toString());
                }
            });

            return null;

        }

/*        @Override
        protected void onPostExecute(String result) {
            Log.d("onPostExecute()", "HttpAsyncTask:" + result);
        }*/

    }


    // if a user_key isn't stored in the device, a client object containing age, gender, country
    // and user agent shall be sent to the backend - the latter is to answer back, returning a
    // client object with the same data sent before along with a computed user hash code - which will
    // be stored at the plugin's directory in the device storage.
    private class GetClientAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                    //.setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .setEndpoint(Globals.endpoints.get("clients")).build()
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
                    //Log.d("GET_CLIENT_RESPONSE: ", arg0.toString());
                    client = arg0;
                }
            });

            return null;

        }

    }


    /*
    // static just for testing
    private class PostClientAsyncTask extends AsyncTask<Void, Void, Void> {

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
*/

    private class GetSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                    //.setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .setEndpoint(Globals.endpoints.get("surveys")).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.getSurvey(partialSurvey, new Callback<Survey>() {
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

                    // load directly in webview
                    myWebView.loadData(response.getBody().toString(), "text/html", "UTF-8");

                    //Log.d("GET_SURVEY_RESPONSE: ", survey.toString());
                }
            });

            return null;

        }

    }

    // WARNING: only to be used during testing period
    private class ProtoGetSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                    //.setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .setEndpoint("http://surveys.ignidata.com").build()
                            //.setEndpoint("http://10.0.3.2:5000").build() // for Genymotion emulator, fool
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.protoGetSurvey(new Callback<Response>() {
                @Override
                public void failure(final RetrofitError retrofitError) {

                    if (retrofitError.getResponse() != null) {
                        Utils.showToast(SurveyActivity.this, "Oops! We're so sorry!" +
                                "We failed hard on you! " +
                                "Our monkeys are working hard to find the problem! ( " +
                                retrofitError.getResponse().getStatus() + ": " +
                                retrofitError.getResponse().getReason() + " )");
                    } else {
                        Utils.showToast(SurveyActivity.this, "You need a working connection to " +
                                "get our goodies! Double-check your Wi-Fi or mobile data connection!");
                    }

                    //Log.d("SIMON SAYS:", retrofitError.getLocalizedMessage() + " -> "
                    //        + retrofitError.getKind().ordinal());
                    //setResult(Globals.RESULT_ERROR, getIntent().putExtra("msg", ""));
                    setResult(Globals.RESULT_ERROR);

                    if (getIntent().hasExtra("platform")) {
                        if (getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                            sendResultToUnityPlayer(getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_ERROR);
                        }
                    }

                    finish(); // get back to main activity...
                }

                @Override
                public void success(Response result, Response response) {
                    //System.out.println(json.get("payload").toString());
                    //System.out.println(response.getStatus());
                    //myWebView.loadData(json.get("payload").toString(), "text/html", "UTF-8");

                    // load directly in webview
                    // Try to get response body
                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    try {

                        reader = new BufferedReader(new InputStreamReader(result
                                .getBody().in()));

                        String line;

                        try {
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String aux = sb.toString();
                    aux = aux.replaceAll("\\r\\n|\\r|\\n", " "); // get out all new lines causing EOFException
                    //myWebView.loadDataWithBaseURL("http://10.0.3.2:5000", aux, "text/html", "UTF-8", "http://10.0.3.2:5000");
                    myWebView.loadDataWithBaseURL("http://surveys.ignidata.com", aux, "text/html", "UTF-8", "http://surveys.ignidata.com");
                    //Log.d("GET_SURVEY_RESPONSE: ", response.getBody().toString());
                }
            });

            return null;

        }
    }


    // static just for testing
    private class PostSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                    //.setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .setEndpoint(Globals.endpoints.get("surveys")).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.postSurvey(partialSurvey, new Callback<Survey>() {
                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println(retrofitError.getLocalizedMessage() + " -> "
                            + retrofitError.getKind().ordinal());
                }

                @Override
                public void success(Survey arg0, Response response) {
                    //myWebView.loadData(json.get("payload").toString(), "text/html", "UTF-8");
                    //Log.d("POST_SURVEY_RESPONSE: ", arg0.toString());
                }
            });

            return null;

        }

    }


    private class PutSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            IgnidataSurveyApi.IgnidataSurveyApiInterface retrofitInterface = new RestAdapter.Builder()
                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                    //.setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build()
                    .setEndpoint(Globals.endpoints.get("surveys")).build()
                    .create(IgnidataSurveyApi.IgnidataSurveyApiInterface.class);

            retrofitInterface.putSurvey(partialSurvey, new Callback<Survey>() {
                @Override
                public void failure(RetrofitError retrofitError) {
                    System.out.println(retrofitError.getLocalizedMessage() + " -> "
                            + retrofitError.getKind().ordinal());
                }

                @Override
                public void success(Survey arg0, Response response) {
                    //myWebView.loadData(json.get("payload").toString(), "text/html", "UTF-8");
                    //Log.d("PUT_SURVEY_RESPONSE: ", arg0.toString());
                }
            });

            return null;

        }

    }


    public class SurveyJSInterface {
        Context mContext;
        Long startTime;
        //Long[] elapsedTime = new Long[partialSurvey.questions.size()];
        Long[] elapsedTime = new Long[1];
        int MINIMUM_TIME_ANSWER = 5000; // for testing

        /**
         * Instantiate the interface and set the context
         */
        SurveyJSInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }


        @JavascriptInterface
        public void leave(String toast) {
            showToast(toast);

            // maybe do something here... and then return control to OS


            finish();
        }


        @JavascriptInterface
        public void startTimer() {
            startTime = System.currentTimeMillis();
            //Log.d("elapsedTime", String.valueOf(startTime));
        }


        //@JavascriptInterface
        public void elapsedTime(int questionIndex) {
            elapsedTime[questionIndex] = ((new Date()).getTime() - startTime);
            //Log.d("elapsedTime", String.valueOf(elapsedTime[questionIndex]));
        }

        /*
                @JavascriptInterface
                public void validate() {
                    long totalResponseTime = 0L;
                    for (int i = 1; i < elapsedTime.length; i++) {
                        totalResponseTime += (elapsedTime[i] - elapsedTime[i - 1]);
                        Log.d("totalResponseTime", String.valueOf(totalResponseTime));
                    }
                    if ((totalResponseTime / elapsedTime.length) <= MINIMUM_TIME_ANSWER) {
                        showToast("You answered the survey too fast...Did you even read the questions? Come on, give it another try!");

                        // reset survey...
                        //TODO check how to pass data to the webview thru the interface!

                    } else {
                        // submit survey!
                        new PostSurveyAsyncTask().execute();
                    }

                }
        */
        //@Deprecated
        //@JavascriptInterface
        //public void validate() {
        //    Intent returnControlToCallerIntent = new Intent(SurveyActivity.this.getBaseContext(), MainActivity.class);
        //    startActivity(returnControlToCallerIntent);
        //    finish();
        //}

        @JavascriptInterface
        public void finishWithResult(String result) {


            if (result.equals("success")) {

                //setResult(Globals.RESULT_SUCCESS, getIntent().putExtra("msg", "Your survey has been submitted."));
                Utils.showToast(SurveyActivity.this, "Your survey has been submitted.");
                setResult(Globals.RESULT_SUCCESS);
                // just for testing
                //sendResultToUnityPlayer(Globals.RESULT_SUCCESS);

                if (getIntent().hasExtra("platform")) {
                    if (getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                        sendResultToUnityPlayer(getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_SUCCESS);
                    }
                }

            }

            if (result.equals("cancel")) {

                Utils.showToast(SurveyActivity.this, "You cancelled the survey.");
                //setResult(Globals.RESULT_CANCEL, getIntent().putExtra("msg", "You cancelled the survey."));

                // just for testing
                if (getIntent().hasExtra("platform")) {
                    if (getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                        sendResultToUnityPlayer(getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_CANCEL);
                    }
                }
            }

            if (result.equals("error")) {

                //setResult(Globals.RESULT_ERROR, getIntent().putExtra("msg", ""));
                setResult(Globals.RESULT_ERROR);

                // just for testing
                if (getIntent().hasExtra("platform")) {
                    if (getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                        sendResultToUnityPlayer(getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_ERROR);
                    }
                }

            }

            if (result.equals("invalid")) {

                setResult(Globals.RESULT_INVALID);
                //setResult(Globals.RESULT_INVALID, getIntent().putExtra("msg", ""));

                // just for testing
                if (getIntent().hasExtra("platform")) {
                    if (getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                        sendResultToUnityPlayer(getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_INVALID);
                    }
                }
            }

            finish();

        }

        /**
         * On _submit_, this method is to be invoked. A JSON struct is then persisted on a folder
         * named 'com.ignidata.plugin' and a background service will be launched, only exiting after
         * sending and unlocking is successful. Then, a notification for the user is to be shown.
         */


        //@JavascriptInterface
/*        public void processSurvey(json) {
        // should validate before... TODO
            if (isOnline()) postSurvey();
            else saveSurvey(json);
        }*//*



*/
/*
        public void saveSurvey(JSONObject json) {
            try {
                File.createTempFile("surveyAnswersRandomHashGoesHere", null, mContext.getCacheDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/
/*        public boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //may return null in case phone is in airplane mode
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }*/

/*        @JavascriptInterface
        public void getUserSignup(int age, String gender, String country) {
            client = new Client();
            client.age = age;
            client.gender = gender;
            client.country = country;
            client.userAgent = WebViewActivity.getUserAgent();
        }*/
        @Deprecated
        @JavascriptInterface
        public void getPartialResults(int questionIndex, int answerIndex, String answer) {
            partialSurvey.questions.get(questionIndex).getAnswers().get(answerIndex).answer = answer;
            if (Utils.isOnline()) new PutSurveyAsyncTask().execute();
            // else saveSurvey(new JSONObject(survey).toString());
        }


        @JavascriptInterface
        public void submitPartialResults(String questionIndex, String questionType, String answers) {

            elapsedTime(Integer.parseInt(questionIndex)); // store timestamp

            List<Answer> listAnswers = partialSurvey.questions.get(Integer.parseInt(questionIndex)).getAnswers();

            if (Question.QuestionType.valueOf(questionType) == Question.QuestionType.ChooseMany) {
                String[] index_answers = answers.split(",");

                for (String pos : index_answers) {
                    listAnswers.get(Integer.parseInt(pos)).answer = "selected"; // does this make sense?
                }
            }

            if (Question.QuestionType.valueOf(questionType) == Question.QuestionType.FreeValue) {
                listAnswers.get(0).answer = answers; // 'listAnswers' will only contain one position
                // 'answers' contains free text
            }

            if (Question.QuestionType.valueOf(questionType) == Question.QuestionType.ChooseOne) {
                // 'answers' will contain the index of the single selection
                listAnswers.get(Integer.parseInt(answers)).answer = "selected"; // does this make sense?
            }


            // ...


            if (Utils.isOnline()) new PutSurveyAsyncTask().execute(); // submit partial survey

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


    // TODO GetSurveyAsyncTask won't get used till backend gets more love. ProtoGetAsyncTask will do just fine for now.
    public void getAsyncSurvey() {
        //partialSurvey = new Survey();
        //partialSurvey.userId = client.userId;
        //partialSurvey.partnerId = Globals.apiKey;
        //partialSurvey.pricepoint = Globals.pricepoint;

        // ...

        //new GetSurveyAsyncTask().execute();
        new ProtoGetSurveyAsyncTask().execute();
    }

    // public static void init(Activity activity, String apiKey, double pricepoint, boolean debug) {}
    public static void initForResult(Activity activity, int usingPlatform) {
        Intent intent = new Intent(activity, SurveyActivity.class);
        intent.putExtra("platform", usingPlatform);
        // startActivity(intent);
        activity.startActivityForResult(intent, Globals.REQUEST_SURVEY);
    }


    public static void sendResultToUnityPlayer(String unityAnchorObject, int result) {
        UnityPlayer.UnitySendMessage(unityAnchorObject, "nativeMessageReceiver", String.valueOf(result));
    }


    public static void init(final Activity activity, final int usingPlatform, final String unityAnchorObject) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, SurveyActivity.class);
                intent.putExtra("platform", usingPlatform);
                intent.putExtra("unityAnchorObject", unityAnchorObject);
                activity.startActivity(intent);
            }
        });
    }


}
