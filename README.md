**Integrating Ignidata SDK for Android**

Changelog:

02.04.2015 - Replaced method ```SurveyActivity.init(...)``` for ```SurveyActivity.initForResult(...)```.

25.03.2015 - First draft.

* Download and add `ignidata-surveys-plugin-*.jar` as a project dependency. **Beware:** for now, this plugin works correctly only for Android 4.0.3+.

* Add following dependencies to your Android Studio project:
```
appcompat-v7-22.0.0 (configurable via SDK Manager)
retrofit-1.9.0
gson-2.3.1
```

* In your AndroidManifest.xml, define the following permissions:

```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

```

* Add this activity:

```
        <activity
            android:name="com.ignidata.api.plugin.SurveyActivity"
            android:label="SurveyActivity"
            android:screenOrientation="portrait">
        </activity>
```

* Import classes:

```
import com.ignidata.api.plugin.Globals;
import com.ignidata.api.plugin.SurveyActivity;
```

* Place this line where you expect the user to engage with a survey:
```
SurveyActivity.initForResult(Activity activity, int platform);
```
> For Android, you should be using something like this:
```
SurveyActivity.initForResult(this, Globals.USING_ANDROID);
```

* Override onActivityResult callback within the activity from which you'll launch the survey
and drop in the relevant code for each result: 
```
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Globals.REQUEST_SURVEY) {
            if (resultCode == Globals.RESULT_SUCCESS) {
                // here you must unlock the content

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
```