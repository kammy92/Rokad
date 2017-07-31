package com.actiknow.rokad.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.CorrectionInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actiknow.rokad.R;
import com.actiknow.rokad.utils.AppConfigTags;
import com.actiknow.rokad.utils.AppConfigURL;
import com.actiknow.rokad.utils.Constants;
import com.actiknow.rokad.utils.NetworkConnection;
import com.actiknow.rokad.utils.TypefaceSpan;
import com.actiknow.rokad.utils.UserDetailsPref;
import com.actiknow.rokad.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by actiknow on 7/26/17.
 */

public class LoginActivity extends AppCompatActivity {
    EditText etMobile;
    EditText etPassword;
    TextView tvSubmit;
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    JSONObject jsonDeviceDetails;
    UserDetailsPref userDetailsPref;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(this);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager ().getPackageInfo (getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        jsonDeviceDetails = new JSONObject ();
        try {
            jsonDeviceDetails.put ("device_id", Settings.Secure.getString (LoginActivity.this.getContentResolver (), Settings.Secure.ANDROID_ID));
            jsonDeviceDetails.put ("device_api_level", Build.VERSION.SDK_INT);
            jsonDeviceDetails.put ("device_os_version", Build.VERSION.RELEASE);
            jsonDeviceDetails.put ("device_manufacturer", Build.MANUFACTURER);
            jsonDeviceDetails.put ("device_model", Build.MODEL);
            jsonDeviceDetails.put ("app_version", pInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace ();
        }

    }

    private void initListener() {
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString s = new SpannableString(getResources().getString(R.string.please_enter_mobile));
                s.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString(getResources().getString(R.string.please_enter_valid_mobile));
                s2.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString(getResources().getString(R.string.please_enter_valid_mobile));
                s3.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString(getResources().getString(R.string.please_enter_password));
                s4.setSpan(new TypefaceSpan(LoginActivity.this, Constants.font_name), 0, s4.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if(etMobile.getText().toString().trim().length() == 0){
                        etMobile.setError(s);
                    }else if(etPassword.getText().toString().trim().length() == 0){
                        etPassword.setError(s4);
                    }else{
                        try{
                            switch (Utils.isValidMobile(etMobile.getText().toString())){
                                case 1:
                                    etMobile.setError(s2);
                                    break;
                                case 2:
                                    etMobile.setError(s2);
                                    break;
                                case 3:
                                    LoginFromServer(etMobile.getText().toString(), etPassword.getText().toString(), jsonDeviceDetails.toString ());
                                    // getOTP(etMobile.getText().toString());
                              /*  Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);*/
                                    break;
                                case 4:
                                    etMobile.setError(s3);
                                    break;
                            }
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            etMobile.setError(s3);
                        }
                    }
                }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etPassword.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etMobile.setError(null);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initView() {
        etMobile = (EditText)findViewById(R.id.etMobile);
        etPassword = (EditText)findViewById(R.id.etPassword);
        clMain = (CoordinatorLayout)findViewById(R.id.clMain);
        tvSubmit = (TextView)findViewById(R.id.tvSubmit);
    }

    private void LoginFromServer(final String mobile, final String password, final String device_details) {
        if (NetworkConnection.isNetworkAvailable(LoginActivity.this)) {

            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_LOGIN, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_LOGIN,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        userDetailsPref.putStringPref(LoginActivity.this,UserDetailsPref.USER_NAME,jsonObj.getString(AppConfigTags.USER_NAME));
                                        userDetailsPref.putStringPref(LoginActivity.this,UserDetailsPref.USER_MOBILE,jsonObj.getString(AppConfigTags.USER_MOBILE));
                                        userDetailsPref.putStringPref(LoginActivity.this,UserDetailsPref.USER_EMAIL,jsonObj.getString(AppConfigTags.USER_EMAIL));
                                        userDetailsPref.putStringPref(LoginActivity.this,UserDetailsPref.USER_TYPE,jsonObj.getString(AppConfigTags.USER_TYPE));
                                        userDetailsPref.putStringPref(LoginActivity.this,UserDetailsPref.USER_LOGIN_KEY,jsonObj.getString("user_login_key"));
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar(LoginActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog(Log.ERROR, AppConfigTags.ERROR, new String(response.data), true);
                            }
                            Utils.showSnackBar(LoginActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.MOBILE, mobile);
                    params.put(AppConfigTags.PASSWORD, Utils.encrypt(password));
                    params.put(AppConfigTags.DEVICE_DETAILS, device_details);

                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            Utils.showSnackBar(this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }
}
