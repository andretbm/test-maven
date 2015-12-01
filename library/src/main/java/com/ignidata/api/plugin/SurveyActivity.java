package com.ignidata.api.plugin;

import android.app.Activity;
import android.app.DatePickerDialog;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;




import org.xwalk.core.JavascriptInterface;

import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.system.StructUtsname;
import android.text.Editable;
import android.view.Window;
import org.xwalk.core.XWalkUIClient;
//import android.webkit.JavascriptInterface;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ignidata.api.plugin.IgnidataSurveyApi.IgnidataSurveyApiInterface;
import com.ignidata.api.plugin.Question.QuestionType;
import com.unity3d.player.UnityPlayer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xwalk.core.internal.XWalkSettings;

import retrofit.Callback;
import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SurveyActivity extends Activity {
    private static final String POSTALCODE_PATERN = "^[a-z0-9][a-z0-9- ]{0,10}[a-z0-9]$";
    private static final String JOB_PATERN = "^([a-zA-Z]+\\s)*[a-zA-Z]+$";
    private String address;
    private XWalkView webviewLowerAndroid;
    private String birthdate;
    private String city;
    private Client client;
    private String country;
    private OnDateSetListener date;
    private EditText editBirthDate;
    private AutoCompleteTextView editGender;
    private AutoCompleteTextView editLanguage;
    private EditText editPostalcode;
    private EditText editProfession;
    private String[] gender_list;
    private String[] lanuage_list;
    private LinearLayout layout;
    private LinearLayout layoutSignView;
    private Calendar myCalendar;
    private WebView myWebView;
    private Survey partialSurvey;
    private Boolean hasID;

    private Button surveButtonAction;
    private EditText mText;
    private SharedPreferences shared;
    private  String id_participant;
    private ProgressDialog progress;
    private String validPostalCode;

    /* renamed from: com.ignidata.api.plugin.SurveyActivity.1 */
    class C07521 implements OnDateSetListener {
        C07521() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SurveyActivity.this.myCalendar.set(1, year);
            SurveyActivity.this.myCalendar.set(2, monthOfYear);
            SurveyActivity.this.myCalendar.set(5, dayOfMonth);
           //SurveyActivity.this.birthdate = SurveyActivity.this.getAge(year, monthOfYear, dayOfMonth);
            SurveyActivity.this.updateLabel();
        }
    }

    /* renamed from: com.ignidata.api.plugin.SurveyActivity.2 */
    class C07532 implements OnClickListener {
        C07532() {
        }

        public void onClick(View v) {
            final int maximumYear = Calendar.getInstance().get(Calendar.YEAR)-18;

            DatePickerDialog dialog = new DatePickerDialog(SurveyActivity.this, date, maximumYear, myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            Calendar maxDate = Calendar.getInstance();
            ;
            maxDate.set(maximumYear, myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

            dialog.show();

        }
    }

    /* renamed from: com.ignidata.api.plugin.SurveyActivity.3 */
    class C07543 implements OnTouchListener {
        C07543() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            SurveyActivity.this.editGender.showDropDown();
            return false;
        }
    }

    /* renamed from: com.ignidata.api.plugin.SurveyActivity.4 */
    class C07554 implements OnTouchListener {
        C07554() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            SurveyActivity.this.editLanguage.setSelection(0);
            SurveyActivity.this.editLanguage.showDropDown();
            return false;
        }
    }

    /* renamed from: com.ignidata.api.plugin.SurveyActivity.5 */
    class C07565 implements OnEditorActionListener {
        C07565() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 6) {

                if(isNetworkAvailable())
                {
                    SurveyActivity.this.getInformationByPostalCode(SurveyActivity.this.editPostalcode.getText().toString());
                }
                else
                {
                    Utils.showToast(SurveyActivity.this, "You need a working connection to get our goodies! Double-check your Wi-Fi or mobile data connection! ");
                }

            }
            return false;
        }
    }

    /* renamed from: com.ignidata.api.plugin.SurveyActivity.6 */
    class C07576 implements OnClickListener {
        C07576() {
        }

        public void onClick(View v) {

            if(!isNetworkAvailable()) {
                Utils.showToast(SurveyActivity.this, "You need a working connection to get our goodies! Double-check your Wi-Fi or mobile data connection! ");
            }
            else {
                if (SurveyActivity.this.editProfession.getText().toString().length() == 0 || SurveyActivity.this.editGender.getText().toString().length() == 0 || SurveyActivity.this.editPostalcode.getText().toString().length() == 0 || SurveyActivity.this.editLanguage.getText().toString().length() == 0 || SurveyActivity.this.editBirthDate.getText().toString().length() == 0) {
                    Utils.showToast(SurveyActivity.this, "Sorry you have to fill all information to proceed!");
                }
                else if (!editPostalcode.getText().toString().matches(POSTALCODE_PATERN))
                {
                    Utils.showToast(SurveyActivity.this, "Please insert a valid postal code");
                }
                else if (!editProfession.getText().toString().matches(JOB_PATERN))
                {
                    Utils.showToast(SurveyActivity.this, "Please insert a valid profession");
                }
                else {


                    SurveyActivity.this.getUserSignup(SurveyActivity.this.editBirthDate.getText().toString(), SurveyActivity.this.editGender.getText().toString(), SurveyActivity.this.editLanguage.getText().toString(), SurveyActivity.this.editProfession.getText().toString(), SurveyActivity.this.editPostalcode.getText().toString().toString());
                }
            }
        }
    }

    /* renamed from: com.ignidata.api.plugin.SurveyActivity.7 */
    static class C07587 implements Runnable {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ String val$unityAnchorObject;
        final /* synthetic */ int val$usingPlatform;

        C07587(Activity activity, int i, String str) {
            this.val$activity = activity;
            this.val$usingPlatform = i;
            this.val$unityAnchorObject = str;
        }

        public void run() {
            Intent intent = new Intent(this.val$activity, SurveyActivity.class);
            intent.putExtra("platform", this.val$usingPlatform);
            intent.putExtra("unityAnchorObject", this.val$unityAnchorObject);
            this.val$activity.startActivity(intent);
        }
    }

    private class GetClientAsyncTask extends AsyncTask<Void, Void, Void> {

        /* renamed from: com.ignidata.api.plugin.SurveyActivity.GetClientAsyncTask.1 */
        class C14171 implements Callback<Client> {
            C14171() {
            }

            public void failure(RetrofitError retrofitError) {
                System.out.println(retrofitError.getLocalizedMessage() + " -> " + retrofitError.getKind().ordinal());
            }

            public void success(Client arg0, Response response) {
                SurveyActivity.this.client = arg0;
            }
        }

        private GetClientAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {
           // ((IgnidataSurveyApiInterface) new Builder().setEndpoint((String) Globals.endpoints.get("clients")).build().create(IgnidataSurveyApiInterface.class)).getClient(new Client(), new C14171());
            return null;
        }
    }

    private class GetEndpointsAsyncTask extends AsyncTask<Void, Void, Void> {

        /* renamed from: com.ignidata.api.plugin.SurveyActivity.GetEndpointsAsyncTask.1 */
        class C14181 implements Callback<JsonObject> {
            C14181() {
            }

            public void failure(RetrofitError arg0) {
            }

            public void success(JsonObject arg0, Response arg1) {
                Globals.endpoints.put("clients", arg0.get("clients").toString());
                Globals.endpoints.put("surveys", arg0.get("surveys").toString());
            }
        }

        private GetEndpointsAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {
            ((IgnidataSurveyApiInterface) new Builder().setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build().create(IgnidataSurveyApiInterface.class)).getEndpoints(new C14181());
            return null;
        }
    }

    private class GetSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        /* renamed from: com.ignidata.api.plugin.SurveyActivity.GetSurveyAsyncTask.1 */
        class C14191 implements Callback<Survey> {
            C14191() {
            }

            public void failure(RetrofitError retrofitError) {
                System.out.println(retrofitError.getLocalizedMessage() + " -> " + retrofitError.getKind().ordinal());
            }

            public void success(Survey survey, Response response) {
                SurveyActivity.this.myWebView.loadData(response.getBody().toString(), "text/html", "UTF-8");
            }
        }

        private GetSurveyAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {
            ((IgnidataSurveyApiInterface) new Builder().setEndpoint((String) Globals.endpoints.get("surveys")).build().create(IgnidataSurveyApiInterface.class)).getSurvey(SurveyActivity.this.partialSurvey, new C14191());
            return null;
        }
    }

    private class PostClientAsyncTask extends AsyncTask<Void, Void, Void> {



        /* renamed from: com.ignidata.api.plugin.SurveyActivity.PostClientAsyncTask.1 */
        class C14201 implements Callback<Response> {


            C14201() {
            }

            public void failure(RetrofitError retrofitError) {

                if (retrofitError.getResponse().getStatus() == 204) {

                    Utils.showToast(SurveyActivity.this, "Request made with success but we have no survey available for you. Please try again later.");
                    SurveyActivity.this.finish();

                } else {

                    if(retrofitError.getResponse()!=null)
                    {


                    Utils.showToast(SurveyActivity.this, " We're so sorry!Our services are down Please try again later");
                }else{
                    Utils.showToast(SurveyActivity.this, "You need a working connection to get our goodies! Double-check your Wi-Fi or mobile data connection!");
                }
             }

                SurveyActivity.this.setResult(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                }


                SurveyActivity.this.finish();
            }


            public void success(Response result, Response response) {


                IOException e;
                BufferedReader bufferedReader;
                String aux;


                if (response.getStatus() == 204) {
                    Utils.showToast(SurveyActivity.this, " We're so sorry!Our services are down Please try again later");
                    SurveyActivity.this.finish();

                } else {
                    final StringBuilder sb = new StringBuilder();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                        while (true) {
                            try {
                                String line = reader.readLine();
                                if (line == null) {
                                    break;
                                }

                                sb.append(line);
                            } catch (IOException e2) {

                            }


                        }
                        bufferedReader = reader;


                    } catch (IOException e4) {


                        SurveyActivity.this.layout.removeAllViewsInLayout();
                        SurveyActivity.this.myWebView.setLayoutParams(new LayoutParams(SurveyActivity.this.getResources().getDisplayMetrics().widthPixels, SurveyActivity.this.getResources().getDisplayMetrics().heightPixels));
                        SurveyActivity.this.myWebView.setLayoutParams(new LayoutParams(-1, -2));
                        SurveyActivity.this.myWebView.getSettings().setUseWideViewPort(true);

                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                        //  SurveyActivity.this.myWebView.loadDataWithBaseURL("http://192.168.3.152:9000/getMobileSurvey", aux, "text/html","UTF-8", "http://192.168.3.152:9000/getMobileSurvey");

                        SurveyActivity.this.layout.addView(SurveyActivity.this.myWebView);
                        SurveyActivity.this.setContentView(SurveyActivity.this.layout);
                    }

                    SurveyActivity.this.layout.removeAllViewsInLayout();


             /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {


                    SurveyActivity.this.myWebView.clearCache(true);
                    aux = sb.toString().replaceAll("\\r\\n|\\r|\\n", " ");
                    float webviewsize = SurveyActivity.this.myWebView.getContentHeight() - myWebView.getTop();

                    SurveyActivity.this.myWebView.loadDataWithBaseURL("http://surveys.ignidata.com/getMobileSurvey",aux, "text/html", "UTF-8",null);
                    //SurveyActivity.this.myWebView.loadData(aux, "text/html","UTF-8");
                    SurveyActivity.this.myWebView.setLayoutParams(new LayoutParams(SurveyActivity.this.getResources().getDisplayMetrics().widthPixels, SurveyActivity.this.getResources().getDisplayMetrics().heightPixels));
                    SurveyActivity.this.myWebView.setLayoutParams(new LayoutParams(-1, -2));



                    SurveyActivity.this.myWebView.getSettings().setLoadWithOverviewMode(true);
                    SurveyActivity.this.myWebView.getSettings().setUseWideViewPort(true);
                    SurveyActivity.this.myWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);

                    SurveyActivity.this.myWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

                    myWebView.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView view, int newProgress) {
                            super.onProgressChanged(view, newProgress);
                            if (newProgress > 0) {
                                showProgressDialog("Please Wait ...");
                            }
                            if (newProgress >= 100) {
                                hideProgressDialog();

                            }
                        }


                    });
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                    SurveyActivity.this.layout.addView(SurveyActivity.this.myWebView);
                    SurveyActivity.this.setContentView(SurveyActivity.this.layout);


                }
                else {*/


                    aux = sb.toString().replaceAll("\\r\\n|\\r|\\n", " ");

                    org.jsoup.nodes.Document doc = Jsoup.parse(aux);

                   

                    XWalkPreferences.setValue("enable-javascript", true);




                    if (hasID == false) {
                        final String userID = doc.getElementsByTag("input").get(7).val().toString();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Your code to run in GUI thread here
                                SharedPreferences prefs = getSharedPreferences("app.prefs", Context.MODE_PRIVATE);

                                prefs.edit().putString("USERID", userID).commit();



                            }//public void run() {
                        });
                    }
                    webviewLowerAndroid.clearCache(true);


                    webviewLowerAndroid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.01f));

                    //webviewLowerAndroid.addJavascriptInterface(new WebAppInterface(), "Android");

                    //SurveyActivity.this.webviewLowerAndroid.load("http://surveys.ignidata.com/getMobileSurvey",aux);

                    SurveyActivity.this.webviewLowerAndroid.load("http://surveys.ignidata.com/getMobileSurvey", aux);


                    //SurveyActivity.this.webviewLowerAndroid.setLayoutParams(new LayoutParams(SurveyActivity.this.getResources().getDisplayMetrics().widthPixels, SurveyActivity.this.getResources().getDisplayMetrics().heightPixels));
                    // SurveyActivity.this.webviewLowerAndroid.setLayoutParams(new LayoutParams(-1, -2));

                    //SurveyActivity.this.webviewLowerAndroid.setLayoutParams(new LayoutParams(SurveyActivity.this.getResources().getDisplayMetrics().widthPixels, SurveyActivity.this.getResources().getDisplayMetrics().heightPixels));

                    SurveyActivity.this.webviewLowerAndroid.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


                    // SurveyActivity.this.webviewLowerAndroid.setLayoutParams(new LayoutParams(-1, -2));


                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    SurveyActivity.this.layout.addView(webviewLowerAndroid);
                    SurveyActivity.this.setContentView(SurveyActivity.this.layout);
                    // }


                }
            }
        }


        private PostClientAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {

           if(hasID == false)
            {


                ((IgnidataSurveyApiInterface) new Builder().setLogLevel(LogLevel.FULL).setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build().create(IgnidataSurveyApiInterface.class)).postClient(client.age, client.gender, client.language,client.profession, client.postalCode, city, country, new C14201());
                //((IgnidataSurveyApiInterface) new Builder().setLogLevel(LogLevel.FULL).setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build().create(IgnidataSurveyApiInterface.class)).postClient(SurveyActivity.this.client, new C14201());

            }
            else
            {
                ((IgnidataSurveyApiInterface) new Builder().setLogLevel(LogLevel.FULL).setEndpoint(IgnidataSurveyApi.IGNIDATA_BASE_URL).build().create(IgnidataSurveyApiInterface.class)).postClientWithParticipantID(id_participant, new C14201());
            }





            return null;
        }
    }

    class ResourceClient extends XWalkResourceClient {


        public ResourceClient(XWalkView xwalkView) {
            super(xwalkView);
        }

        public void onLoadStarted(XWalkView view, String url) {
            super.onLoadStarted(view, url);

        }

        public void onLoadFinished(XWalkView view, String url) {
            super.onLoadFinished(view, url);
          hideProgressDialog();
        }

        public void onProgressChanged(XWalkView view, int progressInPercent) {
            super.onProgressChanged(view, progressInPercent);
            showProgressDialog("Please Wait ...");
        }


    }



    private class PostMobileSurvey extends AsyncTask<Void, Void, Void> {

        /* renamed from: com.ignidata.api.plugin.SurveyActivity.PostMobileSurvey.1 */
        class C14211 implements Callback<Response> {
            C14211() {
            }

            public void failure(RetrofitError retrofitError) {
                if (retrofitError.getResponse() != null) {
                    Utils.showToast(SurveyActivity.this,  " We're so sorry!Our services are down Please try again later");
                } else {
                    Utils.showToast(SurveyActivity.this, "You need a working connection to get our goodies! Double-check your Wi-Fi or mobile data connection!");
                }
                SurveyActivity.this.setResult(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                }
                SurveyActivity.this.finish();
            }

            public void success(Response result, Response response) {
                IOException e;
                BufferedReader bufferedReader;
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                    while (true) {
                        try {
                            String line = reader.readLine();
                            if (line == null) {
                                break;
                            }
                            sb.append(line);
                        } catch (IOException e2) {

                        }
                    }
                    bufferedReader = reader;
                } catch (IOException e4) {
                    SurveyActivity.this.myWebView.loadDataWithBaseURL("http://surveys.ignidata.com/getMobileSurvey", sb.toString().replaceAll("\\r\\n|\\r|\\n", " "), "text/html", "UTF-8", IgnidataSurveyApi.IGNIDATA_BASE_URL);
                }
                SurveyActivity.this.myWebView.loadDataWithBaseURL("http://surveys.ignidata.com/getMobileSurvey", sb.toString().replaceAll("\\r\\n|\\r|\\n", " "), "text/html", "UTF-8", IgnidataSurveyApi.IGNIDATA_BASE_URL);
            }
        }

        private PostMobileSurvey() {
        }

        protected Void doInBackground(Void... voids) {
           //
           // ((IgnidataSurveyApiInterface) new Builder().setEndpoint("http://surveys.ignidata.com/getMobileSurvey").build().create(IgnidataSurveyApiInterface.class))77.postClient(SurveyActivity.this.client, new C14211());
            return null;
        }
    }

    private class PostalCodeInformation extends AsyncTask<String, Void, Boolean> {
        private PostalCodeInformation() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(String... urls) {
            try {
                HttpResponse response = new DefaultHttpClient().execute(new HttpGet(IgnidataSurveyApi.POSTAL_CODE_URL.concat("&address=" + SurveyActivity.this.address)));
                if (response.getStatusLine().getStatusCode() == Globals.USING_ANDROID) {
                    JSONArray results = ((JSONObject) new JSONTokener(EntityUtils.toString(response.getEntity())).nextValue()).getJSONArray("results");
                    if (results.length() == 0) {
                        SurveyActivity.this.validPostalCode = "invalid";
                    } else {
                        JSONArray address = results.getJSONObject(0).getJSONArray("address_components");
                        SurveyActivity.this.validPostalCode = "valid";
                        int length = address.length();
                        SurveyActivity.this.city = address.getJSONObject(length - 2).get("long_name").toString();
                        SurveyActivity.this.country = address.getJSONObject(length - 1).get("long_name").toString();
                    }
                    return Boolean.valueOf(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            return Boolean.valueOf(false);
        }

        protected void onPostExecute(Boolean result) {
        }
    }
    public void showProgressDialog(final String msg) {

        runOnUiThread(new Runnable() {
            public void run() {
                if (progress == null || !progress.isShowing()) {
                    progress = ProgressDialog.show(SurveyActivity.this, "", msg);
                }
            }
        });
    }

    public void hideProgressDialog() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (progress.isShowing())
                        progress.dismiss();
                } catch (Throwable e) {

                }
            }
        });
    }



    private class PutSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        /* renamed from: com.ignidata.api.plugin.SurveyActivity.PutSurveyAsyncTask.1 */
        class C14221 implements Callback<Survey> {
            C14221() {
            }

            public void failure(RetrofitError retrofitError) {
                System.out.println(retrofitError.getLocalizedMessage() + " -> " + retrofitError.getKind().ordinal());
            }

            public void success(Survey arg0, Response response) {
            }
        }

        private PutSurveyAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {
            ((IgnidataSurveyApiInterface) new Builder().setEndpoint((String) Globals.endpoints.get("surveys")).build().create(IgnidataSurveyApiInterface.class)).putSurvey(SurveyActivity.this.partialSurvey, new C14221());
            return null;
        }
    }


    public class WebAppInterface {



       /* private Context context;
        private XWalkView xWalkWebView;

        public WebAppInterface(Context c, XWalkView xWalkWebView) {
            context = c;
            this.xWalkWebView = xWalkWebView;
        }*/
       @org.xwalk.core.JavascriptInterface
        public void leave(String toast) {

           webviewLowerAndroid = null;


           layout.removeAllViews();
           finish();




        }

        @org.xwalk.core.JavascriptInterface
        public void finishWithResult(String result){

            if (result.equals("success")) {

                Utils.showToast(SurveyActivity.this, "Your survey has been submitted.");
                 SurveyActivity.this.setResult(100);
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), 100);
                }


            }
            if (result.equals("cancel")) {
                Utils.showToast(SurveyActivity.this, "You cancelled the survey.");
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_CANCEL);
                }

            }
            if (result.equals("error")) {
                SurveyActivity.this.setResult(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                }
            }
            if (result.equals("invalid")) {
                SurveyActivity.this.setResult(Globals.RESULT_INVALID);
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_INVALID);
                }
            }

            webviewLowerAndroid = null;


           finish();
            layout.removeAllViews();


        }
    }

   /* public class SurveyJSInterface {

       int MINIMUM_TIME_ANSWER;
        Long[] elapsedTime;
        Context mContext;
        Long startTime;

        SurveyJSInterface(Context c) {
            this.elapsedTime = new Long[1];
            this.MINIMUM_TIME_ANSWER = 15000;
            this.mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(this.mContext, toast, 0).show();
        }

       // @JavascriptInterface
        public void leave(String toast) {
            showToast(toast);
            myWebView = null;

            SurveyActivity.this.finish();


        }

        @JavascriptInterface
        public void startTimer() {
            this.startTime = Long.valueOf(System.currentTimeMillis());
        }

        public void elapsedTime(int questionIndex) {
            this.elapsedTime[questionIndex] = Long.valueOf(new Date().getTime() - this.startTime.longValue());
        }

        @JavascriptInterface
        public void finishWithResult(String result) {


            if (result.equals("success")) {

                Utils.showToast(SurveyActivity.this, "Your survey has been submitted.");
                SurveyActivity.this.setResult(100);
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), 100);
                }

                myWebView.clearCache(true);
                myWebView.clearHistory();
                myWebView.clearFormData();
                myWebView = null;



            }
            if (result.equals("cancel")) {
                Utils.showToast(SurveyActivity.this, "You cancelled the survey.");
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_CANCEL);
                }

            }
            if (result.equals("error")) {
                SurveyActivity.this.setResult(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                }
            }
            if (result.equals("invalid")) {
                SurveyActivity.this.setResult(Globals.RESULT_INVALID);
                if (SurveyActivity.this.getIntent().hasExtra("platform") && SurveyActivity.this.getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                    SurveyActivity.sendResultToUnityPlayer(SurveyActivity.this.getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_INVALID);
                }
            }

            myWebView = null;

           SurveyActivity.this.finish();


        }

       /* @JavascriptInterface
        @Deprecated
        public void getPartialResults(int questionIndex, int answerIndex, String answer) {
            ((Answer) ((Question) SurveyActivity.this.partialSurvey.questions.get(questionIndex)).getAnswers().get(answerIndex)).answer = answer;
            if (Utils.isOnline()) {
                new PutSurveyAsyncTask().execute();
            }
        }*/
        //@JavascriptInterface
       /* public void submitPartialResults(String questionIndex, String questionType, String answers) {
            elapsedTime(Integer.parseInt(questionIndex));
            List<Answer> listAnswers = ((Question) SurveyActivity.this.partialSurvey.questions.get(Integer.parseInt(questionIndex))).getAnswers();
            if (QuestionType.valueOf(questionType) == QuestionType.ChooseMany) {
                for (String pos : answers.split(",")) {
                    ((Answer) listAnswers.get(Integer.parseInt(pos))).answer = "selected";
                }
            }
            if (QuestionType.valueOf(questionType) == QuestionType.FreeValue) {
                ((Answer) listAnswers.get(0)).answer = answers;
            }
            if (QuestionType.valueOf(questionType) == QuestionType.ChooseOne) {
                ((Answer) listAnswers.get(Integer.parseInt(answers))).answer = "selected";
            }
            if (Utils.isOnline()) {
                new PutSurveyAsyncTask().execute();
            }
        }
    }*/

    public SurveyActivity() {
        this.myWebView = null;
        this.webviewLowerAndroid = null;
        this.partialSurvey = null;
        this.lanuage_list = new String[]{"English"};
        this.gender_list = new String[]{"Male", "Female"};
    }


    public class MyAppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // Handle local URLs.
            if (Uri.parse("http://surveys.ignidata.com/getMobileSurvey").getHost().length() == 0) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT <= 15) {
            Utils.showToast(this, "Unfortunately, you can only use Ignidata Surveys on devices running Android 4.0.3 or most recent versions!");
            setResult(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (getIntent().hasExtra("platform") && getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
                sendResultToUnityPlayer(getIntent().getExtras().getString("unityAnchorObject"), LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            }
            finish();
        }
        this.layout = new LinearLayout(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            this.myWebView = new WebView(this);

            // webviewLowerAndroid = new XWalkView(null,SurveyActivity.this);

            this.myWebView.setWebViewClient(new WebViewClient());

            this.myWebView.setWebChromeClient(new WebChromeClient());
        Log.d("TAGFDGFHGDA", "ANDROID LOLLIPOP");
            this.myWebView.getSettings().setJavaScriptEnabled(true);
            this.myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
            this.myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            this.myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            this.myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            this.myWebView.getSettings().setAllowFileAccess(true);
            this.myWebView.addJavascriptInterface(new SurveyJSInterface(this), "Android");

            this.myWebView.getSettings().setAllowContentAccess(true);



            //if (VERSION.SDK_INT >= 16) {
              //  this.myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
               // this.myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
            //}

        }
        else
        {*/




       // }

        webviewLowerAndroid = new XWalkView(SurveyActivity.this.getApplicationContext(), this);


        webviewLowerAndroid.addJavascriptInterface(new WebAppInterface(), "Android");

        this.layout.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.layout.setOrientation(LinearLayout.VERTICAL);

      //  XWalkSettings webSettings = webviewLowerAndroid.getSettings();
       // webSettings.setJavaScriptEnabled(true);

        webviewLowerAndroid.setResourceClient(new ResourceClient(webviewLowerAndroid));


        SharedPreferences settings = getSharedPreferences("app.prefs", Context.MODE_PRIVATE);
         id_participant = settings.getString("USERID","");


        if(id_participant.length()==0)
        {
            hasID = false;
            showSignup();
        }
        else

        {

            hasID = true;
            new PostClientAsyncTask().execute();
        }



    }



    public void onBackPressed() {
        Utils.showToast(this, "You cancelled the survey.");
        setResult(Globals.RESULT_CANCEL);
        if (getIntent().hasExtra("platform") && getIntent().getExtras().get("platform").toString().equals(String.valueOf(Globals.USING_UNITY3D))) {
            sendResultToUnityPlayer(getIntent().getExtras().getString("unityAnchorObject"), Globals.RESULT_CANCEL);
        }
        finish();
    }

    public void saveUserId(Client client) throws FileNotFoundException {
        File path = new File(String.valueOf(getFilesDir()));
        path.mkdir();
        try {
            PrintWriter pw = new PrintWriter(new File(path, "user_key"));
            BufferedWriter br = new BufferedWriter(pw);
            pw.println(client);
            pw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserId() {
        File userId = new File(new File(String.valueOf(getFilesDir())), "user_key");
        if (userId.exists()) {
            try {
                return new BufferedReader(new FileReader(userId)).readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return BuildConfig.FLAVOR;
    }

        public void showSignup() {


        this.editBirthDate = new EditText(this);
        this.editBirthDate.setInputType(0);
        this.editProfession = new EditText(this);
        this.editProfession.setImeOptions(6);
        this.editLanguage = new AutoCompleteTextView(this);
        this.editLanguage.setInputType(0);
        this.editGender = new AutoCompleteTextView(this);
        this.editPostalcode = new EditText(this);
        this.surveButtonAction = new Button(this);
        ArrayAdapter<String> spinner_gender = new ArrayAdapter(this, 17367049, this.gender_list);
        this.myCalendar = Calendar.getInstance();
        this.surveButtonAction.setText("Go to Survey");
        this.date = new C07521();
        this.editBirthDate.setOnClickListener(new C07532());
        this.editGender.setAdapter(new ArrayAdapter(this, 17367048, this.gender_list));
        this.editGender.setOnTouchListener(new C07543());
        ArrayAdapter<String> adapterLanguage = new ArrayAdapter(this, 17367048, this.lanuage_list);
        this.editLanguage.setText(((String) adapterLanguage.getItem(0)).toString());
        this.editLanguage.setAdapter(adapterLanguage);
        this.editLanguage.setOnTouchListener(new C07554());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       // layoutParams = 1;

        LinearLayout.LayoutParams titleLabel = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleLabel.setMargins(0, 50, 0, 50);
        titleLabel.gravity = Gravity.CENTER_HORIZONTAL;


        TextView title = new TextView(this);
        title.setText("Please fill the form to get the free content");
        title.setLayoutParams(titleLabel);
        title.setTextSize(16.0f);
        LinearLayout.LayoutParams layoutParamsButton = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParamsButton.gravity = Gravity.CENTER_HORIZONTAL;




        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;



        //layoutParams.setMargins(100,130,0,130);


        if(height >=1500)
        {
            layoutParams.setMargins(100, 100,0,0);
            layoutParamsButton.setMargins(0, 190, 0, 0);

        }
        else
        {
            layoutParams.setMargins(50,30,0,0);
            layoutParamsButton.setMargins(0, 60, 0, 0);
        }


        editProfession.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    String job = editProfession.getText().toString();
                    if (job.matches("[a-zA-Z_\\s]+")) {
                        editProfession.setText(job, TextView.BufferType.EDITABLE);
                    } else {
                        Utils.showToast(SurveyActivity.this, "Sorry you have to insert a valid job!");
                    }

                }
                return false;
            }
        });


        this.editBirthDate.setHint("Insert your birthdate");
        this.editBirthDate.setSingleLine(true);
        this.editProfession.setHint("Insert your profession");
        this.editGender.setHint("Insert Gender");
        this.editLanguage.setHint("Insert language");
        this.editGender.setSingleLine(true);
        this.editProfession.setSingleLine(true);
        this.editLanguage.setSingleLine(true);
        this.editGender.setInputType(0);
        this.editPostalcode.setHint("Insert your postal code");
        this.editPostalcode.setSingleLine(true);
        this.editPostalcode.setOnEditorActionListener(new C07565());
        this.layout.addView(title, titleLabel);

        this.layout.addView(this.editBirthDate, layoutParams);

        this.layout.addView(this.editGender, layoutParams);

        this.layout.addView(this.editLanguage, layoutParams);

        this.layout.addView(this.editProfession, layoutParams);

        this.layout.addView(this.editPostalcode, layoutParams);
        this.layout.addView(this.surveButtonAction, layoutParamsButton);
        setContentView(this.layout);
        this.surveButtonAction.setOnClickListener(new C07576());
    }

    public void getInformationByPostalCode(String postal) {
        if (Pattern.compile(POSTALCODE_PATERN).matcher(postal).matches()) {
            this.address = postal.toString();
            this.address = this.address.replace(" ", BuildConfig.FLAVOR);
            new PostalCodeInformation().execute(new String[0]);
            return;
        }
        else {
            Utils.showToast(this, "Please insert a valid postal code!");
        }
    }

    /*private int getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(1) - dob.get(1);
        if (today.get(6) < dob.get(6)) {
            age--;
        }
        return new Integer(age).intValue();
    }*/

    public void getUserSignup(String age, String gender, String language, String profession, String postalCode) {
        String userID = "";
        this.client = new Client(age,gender,language,city,profession,postalCode,country,userID);
        this.client.age = birthdate.toString();
        this.client.gender = gender;
        this.client.language = language;
        this.client.profession = profession;
        this.client.postalCode = postalCode;
        this.client.country = this.country;
        this.client.city = this.city;


        new PostClientAsyncTask().execute(new Void[0]);
    }

    private void updateLabel() {

        this.editBirthDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(this.myCalendar.getTime()));
        birthdate = (new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(this.myCalendar.getTime())).toString();
    }

    public static void initForResult(Activity activity, int usingPlatform) {
        Intent intent = new Intent(activity, SurveyActivity.class);
        intent.putExtra("platform", usingPlatform);
        activity.startActivityForResult(intent, 1);
    }

    public static void sendResultToUnityPlayer(String unityAnchorObject, int result) {
        UnityPlayer.UnitySendMessage(unityAnchorObject, "nativeMessageReceiver", String.valueOf(result));
    }

    public static void init(Activity activity, int usingPlatform, String unityAnchorObject) {
        activity.runOnUiThread(new C07587(activity, usingPlatform, unityAnchorObject));
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webviewLowerAndroid = null;
        layout.removeAllViews();


        finish();
        // remove webView, prevent chromium to crash
    }

}


