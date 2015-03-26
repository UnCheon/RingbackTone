package com.allo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by baek_uncheon on 2015. 3. 25..
 */
public class NameActivity extends Activity {

    EditText nameEt;
    Context context;

    String phone_number;
    String nickname;



    private GoogleCloudMessaging _gcm;
    private String _regId;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENDER_ID = "793263929001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // google play service가 사용가능한가
        if (checkPlayServices())
        {
            _gcm = GoogleCloudMessaging.getInstance(this);
            _regId = getRegistrationId();

            if (TextUtils.isEmpty(_regId))
                registerInBackground();
        }
        else
        {
            Log.i("MainActivity.java | onCreate", "|No valid Google Play Services APK found.|");

        }


        Log.i("reg_id", getRegistrationId());

        // display received msg
        String msg = getIntent().getStringExtra("msg");
        if (!TextUtils.isEmpty(msg)){

        }

        context = this;


        setContentView(R.layout.activity_name);
        nameEt = (EditText)findViewById(R.id.nameEt);


    }

    public void agreeBtn (View v){

        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phone_number = telManager
                .getLine1Number();
        if (phone_number.startsWith("+82")) {
            phone_number = phone_number.replace(
                    "+82", "0");
        }
        nickname = nameEt.getText().toString();


        if (nickname.equals("")){
            Toast.makeText(this, "사용하실 닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
        }else{
            // register user info

            String url = context.getString(R.string.base_url) + "/account/register/";

            AsyncHttpClient myClient = new AsyncHttpClient();
            myClient.setTimeout(30000);

            PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
            myClient.setCookieStore(myCookieStore);

            RequestParams params = new RequestParams();
            params.put("phone_number", phone_number);
            params.put("nickname", nickname);
            params.put("device_uuid", phone_number);
            params.put("device_type", "android");

            myClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("HTTP RESPONSE......", new String(responseBody));
                    try {
                        JSONObject response_object = new JSONObject(new String(responseBody));
                        String status = response_object.getString("status");
                        if (status.equals("success")) {
                            // save ID PASSWORD & go to main menu
                            SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();

                            editor.putString("username", phone_number);
                            editor.putString("nickname", nickname);
                            editor.putString("password", String.valueOf(phone_number));
                            editor.commit();

                            register_success();
                        } else {

                        }

                    } catch (JSONException e) {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println(new String(responseBody));
                }
            });
        }
    }

    private void register_success(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }







    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        // display received msg
        String msg = intent.getStringExtra("msg");
        Log.i("MainActivity.java | onNewIntent", "|" + msg + "|");
        if (!TextUtils.isEmpty(msg))
            Log.i("MainActivity.java | onNewIntent", "|" + msg + "|");


    }

    // google play service가 사용가능한가
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i("MainActivity.java | checkPlayService", "|This device is not supported.|");

                finish();
            }
            return false;
        }
        return true;
    }

    // registration  id를 가져온다.
    private String getRegistrationId()
    {
        String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
        if (TextUtils.isEmpty(registrationId))
        {
            Log.i("MainActivity.java | getRegistrationId", "|Registration not found.|");

            return "";
        }
        int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion)
        {
            Log.i("MainActivity.java | getRegistrationId", "|App version changed.|");


            return "";
        }
        return registrationId;
    }

    // app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
    private int getAppVersion()
    {
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    // gcm 서버에 접속해서 registration id를 발급받는다.
    private void registerInBackground()
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String msg = "";
                try
                {
                    if (_gcm == null)
                    {
                        _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    _regId = _gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + _regId;

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(_regId);
                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg)
            {
                Log.i("MainActivity.java | onPostExecute", "|" + msg + "|");

            }
        }.execute(null, null, null);
    }

    // registraion id를 preference에 저장한다.
    private void storeRegistrationId(String regId)
    {
        int appVersion = getAppVersion();
        Log.i("MainActivity.java | storeRegistrationId", "|" + "Saving regId on app version " + appVersion + "|");
        PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
        PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
    }

}

