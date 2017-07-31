package com.actiknow.rokad.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actiknow.rokad.R;
import com.actiknow.rokad.utils.AppConfigTags;
import com.actiknow.rokad.utils.AppConfigURL;
import com.actiknow.rokad.utils.Constants;
import com.actiknow.rokad.utils.NetworkConnection;
import com.actiknow.rokad.utils.UserDetailsPref;
import com.actiknow.rokad.utils.Utils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


/**
 * Created by actiknow on 7/26/17.
 */

public class TruckEntryActivity extends AppCompatActivity implements com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener{
    ProgressDialog progressDialog;
    EditText etLrNo;
    EditText etWeight;
    EditText etDate;
   EditText etDestination;
    EditText etOrderNo;
    EditText etInvoiceNo;
    EditText etParty;
    EditText etCashAdvance;
    EditText etDieselAdvance;
    EditText etBillRate;
    CoordinatorLayout clMain;
    TextView tvSubmit;
    UserDetailsPref userDetailsPref;
    EditText etNumberPlate1;
    EditText etNumberPlate2;
    EditText etNumberPlate3;
    EditText etNumberPlate4;
    RelativeLayout rlBack;

    private int mYear, mMonth, mDay, mHour, mMinute;
    String date = "", time = "", address2 = "";
    private boolean mAutoHighlight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_entry);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        etLrNo = (EditText) findViewById(R.id.etLrNo);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etDate = (EditText) findViewById(R.id.etDate);
        etDestination = (EditText) findViewById(R.id.etDestination);
        etOrderNo = (EditText) findViewById(R.id.etOrderNo);
        etInvoiceNo = (EditText) findViewById(R.id.etInvoiceNo);
        etParty = (EditText) findViewById(R.id.etParty);
        etCashAdvance = (EditText) findViewById(R.id.etCashAdvance);
        etDieselAdvance = (EditText) findViewById(R.id.etDieselAdvance);
        etBillRate = (EditText) findViewById(R.id.etBillRate);
        etNumberPlate1 = (EditText) findViewById(R.id.etNumberPlate1);
        etNumberPlate2 = (EditText) findViewById(R.id.etNumberPlate2);
        etNumberPlate3 = (EditText) findViewById(R.id.etNumberPlate3);
        etNumberPlate4 = (EditText) findViewById(R.id.etNumberPlate4);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        rlBack=(RelativeLayout)findViewById(R.id.rlBack);

    }

    public void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(this);

    }


    private void initListener() {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                com.borax12.materialdaterangepicker.date.DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        TruckEntryActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAutoHighlight(mAutoHighlight);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });



        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etLrNo.getText().toString().length() == 0) {
                    etLrNo.setError("Please fill LR Number");
                } else if (etWeight.getText().toString().length() == 0) {
                    etWeight.setError("Please fill Weight");
                } else if (etDate.getText().toString().length() == 0) {
                    etDate.setError("Please fill Date");
                }else if (etNumberPlate1.getText().toString().length() == 0) {
                    etNumberPlate1.setError("Please fill TruckNumber");
                }else if (etNumberPlate2.getText().toString().length() == 0) {
                    etNumberPlate2.setError("Please fill TruckNumber");
                }else if (etNumberPlate3.getText().toString().length() == 0) {
                    etNumberPlate3.setError("Please fill TruckNumber");
                }else if (etNumberPlate4.getText().toString().length() == 0) {
                    etNumberPlate4.setError("Please fill TruckNumber");
                } else if (etDestination.getText().toString().length() == 0) {
                    etDestination.setError("Please fill Destination");
                } else if (etOrderNo.getText().toString().length() == 0) {
                    etOrderNo.setError("Please fill Order Number");
                } else if (etInvoiceNo.getText().toString().length() == 0) {
                    etInvoiceNo.setError("Please fill Invoice Number");
                } else if (etParty.getText().toString().length() == 0) {
                    etParty.setError("Please fill Party Name");
                } else if (etCashAdvance.getText().toString().length() == 0) {
                    etCashAdvance.setError("Please fill Cash Advance");
                } else if (etDieselAdvance.getText().toString().length() == 0) {
                    etDieselAdvance.setError("Please fill Diesel Advance");
                } else if (etBillRate.getText().toString().length() == 0) {
                    etBillRate.setError("Please fill Bill Rate");
                } else {
                    UpdateTruckEntryToServer(etLrNo.getText().toString(), etWeight.getText().toString(), etDate.getText().toString(),
                            (etNumberPlate1.getText().toString()+""+etNumberPlate2.getText().toString()+""+etNumberPlate3.getText().toString()+""+
                                    etNumberPlate4.getText().toString()),
                            etDestination.getText().toString(), etOrderNo.getText().toString(), etInvoiceNo.getText().toString(),
                            etParty.getText().toString(), etCashAdvance.getText().toString(), etDieselAdvance.getText().toString(),
                            etBillRate.getText().toString());
                }
            }
        });



        etNumberPlate1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(etNumberPlate1.getText().toString().length()==2)     //size as per your requirement
                {
                    etNumberPlate2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        etNumberPlate2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(etNumberPlate2.getText().toString().length()==2)     //size as per your requirement
                {
                    etNumberPlate3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        etNumberPlate3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(etNumberPlate3.getText().toString().length()==2)     //size as per your requirement
                {
                    etNumberPlate4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        etNumberPlate4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(etNumberPlate4.getText().toString().length()==4)     //size as per your requirement
                {
                    etDestination.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
    }


    private void UpdateTruckEntryToServer(final String lr_number, final String weight, final String date, final String truck_number,
                                          final String destination, final String order_number, final String invoice_number, final String party_name,
                                          final String cash_advance, final String diesel_advance, final String bill_date) {
        if (NetworkConnection.isNetworkAvailable(TruckEntryActivity.this)) {

            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_TRUCK_ENTRY, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_TRUCK_ENTRY,
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
                                        MaterialDialog dialog = new MaterialDialog.Builder(TruckEntryActivity.this)
                                                .content(message)
                                                .positiveText("OK")
                                                .show();
                                    } else {
                                        Utils.showSnackBar(TruckEntryActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(TruckEntryActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(TruckEntryActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar(TruckEntryActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.LR_NUMBER, lr_number);
                    params.put(AppConfigTags.WEIGHT, weight);
                    params.put(AppConfigTags.DATE, date);
                    params.put(AppConfigTags.TRUCK_NUMBER, truck_number);
                    params.put(AppConfigTags.DESTINATION, destination);
                    params.put(AppConfigTags.ORDER_NUMBER, order_number);
                    params.put(AppConfigTags.INVOICE_NUMBER, invoice_number);
                    params.put(AppConfigTags.PARTY_NAME, party_name);
                    params.put(AppConfigTags.CASH_ADVANCE, cash_advance);
                    params.put(AppConfigTags.DIESEL_ADVANCE, diesel_advance);
                    params.put(AppConfigTags.BILL_DATE, bill_date);
                    Utils.showLog(Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put(AppConfigTags.USER_LOGIN_KEY, userDetailsPref.getStringPref(TruckEntryActivity.this, UserDetailsPref.USER_LOGIN_KEY));
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


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = "You picked the following date: From- "+dayOfMonth+"/"+(++monthOfYear)+"/"+year+" To "+dayOfMonthEnd+"/"+(++monthOfYearEnd)+"/"+yearEnd;
        etDate.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
    }


}
