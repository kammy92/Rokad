package com.actiknow.rokad.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.rokad.R;
import com.actiknow.rokad.adapter.TruckDetailAdapter;
import com.actiknow.rokad.model.Destination;
import com.actiknow.rokad.model.TruckDetail;
import com.actiknow.rokad.utils.AppConfigTags;
import com.actiknow.rokad.utils.AppConfigURL;
import com.actiknow.rokad.utils.Constants;
import com.actiknow.rokad.utils.NetworkConnection;
import com.actiknow.rokad.utils.UserDetailsPref;
import com.actiknow.rokad.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * Created by actiknow on 7/26/17.
 */

public class TruckEntryActivity extends AppCompatActivity {
    final List<TruckDetail> truckDetailList = new ArrayList<> ();
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
    EditText etTruckNumber;
    EditText etNumberPlate1;
    EditText etNumberPlate2;
    EditText etNumberPlate3;
    EditText etNumberPlate4;
    RelativeLayout rlBack;
    TextView tvSelectDestination;
    TextView tvSelectTruckNumber;
    TruckDetailAdapter truckDetailAdapter;
    MaterialDialog truckDialog;
    RecyclerView rvTrucks;
    LinearLayout llAddTruckDetail;
    RelativeLayout rlTruckList;
    ProgressBar progressBarTrucks;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_truck_entry);
        initView ();
        initData ();
        initListener ();
    }
    
    private void initView () {
        etLrNo = (EditText) findViewById (R.id.etLrNo);
        etWeight = (EditText) findViewById (R.id.etWeight);
        etDate = (EditText) findViewById (R.id.etDate);
        etDestination = (EditText) findViewById (R.id.etDestination);
        etOrderNo = (EditText) findViewById (R.id.etOrderNo);
        etInvoiceNo = (EditText) findViewById (R.id.etInvoiceNo);
        etParty = (EditText) findViewById (R.id.etParty);
        etCashAdvance = (EditText) findViewById (R.id.etCashAdvance);
        etDieselAdvance = (EditText) findViewById (R.id.etDieselAdvance);
        etBillRate = (EditText) findViewById (R.id.etBillRate);
        etTruckNumber = (EditText) findViewById (R.id.etTruckNumber);
        etNumberPlate1 = (EditText) findViewById (R.id.etNumberPlate1);
        etNumberPlate2 = (EditText) findViewById (R.id.etNumberPlate2);
        etNumberPlate3 = (EditText) findViewById (R.id.etNumberPlate3);
        etNumberPlate4 = (EditText) findViewById (R.id.etNumberPlate4);
        tvSubmit = (TextView) findViewById (R.id.tvSubmit);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
    
        tvSelectTruckNumber = (TextView) findViewById (R.id.tvSelectTruckNumber);
        tvSelectDestination = (TextView) findViewById (R.id.tvSelectDestination);
    }
    
    public void initData () {
        userDetailsPref = UserDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (this);
        etParty.setText (userDetailsPref.getStringPref (TruckEntryActivity.this, UserDetailsPref.PARTY_NAME));
    
        truckDetailAdapter = new TruckDetailAdapter (TruckEntryActivity.this, truckDetailList);
        boolean wrapInScrollView = true;
        truckDialog = new MaterialDialog.Builder (TruckEntryActivity.this)
                .customView (R.layout.dialog_truck_list, wrapInScrollView)
                .neutralText ("ADD")
                .neutralColor (getResources ().getColor (R.color.accent))
                .positiveText ("REFRESH")
                .positiveColor (getResources ().getColor (R.color.accent))
                .build ();
    
        rvTrucks = (RecyclerView) truckDialog.getCustomView ().findViewById (R.id.rvTruckList);
        rlTruckList = (RelativeLayout) truckDialog.getCustomView ().findViewById (R.id.rlTruckList);
        llAddTruckDetail = (LinearLayout) truckDialog.getCustomView ().findViewById (R.id.llAddTruckDetail);
        progressBarTrucks = (ProgressBar) truckDialog.findViewById (R.id.progressBarTrucks);
    
        truckDialog.getActionButton (DialogAction.NEUTRAL).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (rlTruckList.getVisibility () == View.VISIBLE) {
                    truckDialog.getActionButton (DialogAction.NEUTRAL).setText ("CANCEL");
                    truckDialog.getActionButton (DialogAction.POSITIVE).setText ("SAVE");
                    rlTruckList.setVisibility (View.GONE);
                    llAddTruckDetail.setVisibility (View.VISIBLE);
                } else {
                    truckDialog.getActionButton (DialogAction.NEUTRAL).setText ("ADD");
                    truckDialog.getActionButton (DialogAction.POSITIVE).setText ("REFRESH");
                    rlTruckList.setVisibility (View.VISIBLE);
                    llAddTruckDetail.setVisibility (View.GONE);
                }
            }
        });
    
        truckDetailAdapter.SetOnItemClickListener (new TruckDetailAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                TruckDetail truckDetail = truckDetailList.get (position);
                etTruckNumber.setText (truckDetail.getTruck_number ());
                truckDialog.dismiss ();
            }
        });
    
    
        rvTrucks.setAdapter (truckDetailAdapter);
        rvTrucks.setHasFixedSize (true);
        rvTrucks.setLayoutManager (new LinearLayoutManager (TruckEntryActivity.this, LinearLayoutManager.VERTICAL, false));
        rvTrucks.setItemAnimator (new DefaultItemAnimator ());
        
    }
    
    private void initListener () {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    
        etDate.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Utils.pickupDate (TruckEntryActivity.this, etDate);
            }
        });
    
        tvSubmit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                boolean flag = true;
                if (etLrNo.getText ().toString ().length () == 0) {
                    etLrNo.setError ("Please fill LR Number");
                    flag = false;
                }
                if (etWeight.getText ().toString ().length () == 0) {
                    etWeight.setError ("Please fill Weight");
                    flag = false;
                }
                if (etDate.getText ().toString ().length () == 0) {
                    etDate.setError ("Please fill Date");
                    flag = false;
                }
                if (etNumberPlate1.getText ().toString ().length () == 0 ||
                        etNumberPlate2.getText ().toString ().length () == 0 ||
                        etNumberPlate3.getText ().toString ().length () == 0 ||
                        etNumberPlate4.getText ().toString ().length () == 0) {
                    etNumberPlate4.setError ("Please fill TruckNumber");
                    flag = false;
                }
                if (etDestination.getText ().toString ().length () == 0) {
                    etDestination.setError ("Please fill Destination");
                    flag = false;
                }
                if (etOrderNo.getText ().toString ().length () == 0) {
                    etOrderNo.setError ("Please fill Order Number");
                    flag = false;
                }
                if (etInvoiceNo.getText ().toString ().length () == 0) {
                    etInvoiceNo.setError ("Please fill Invoice Number");
                    flag = false;
                }
                if (etParty.getText ().toString ().length () == 0) {
                    etParty.setError ("Please fill Party Name");
                    flag = false;
                }
                if (etCashAdvance.getText ().toString ().length () == 0) {
                    etCashAdvance.setError ("Please fill Cash Advance");
                    flag = false;
                }
                if (etDieselAdvance.getText ().toString ().length () == 0) {
                    etDieselAdvance.setError ("Please fill Diesel Advance");
                    flag = false;
                }
                if (etBillRate.getText ().toString ().length () == 0) {
                    etBillRate.setError ("Please fill Bill Rate");
                    flag = false;
                }
    
                if (flag) {
                    String truckNumber = etNumberPlate1.getText ().toString () + " " + etNumberPlate2.getText ().toString () + " " + etNumberPlate3.getText ().toString () + " " + etNumberPlate4.getText ().toString ();
                    UpdateTruckEntryToServer (
                            etLrNo.getText ().toString (),
                            etWeight.getText ().toString (),
                            Utils.convertTimeFormat (etDate.getText ().toString (), "dd-MM-yyyy", "yyyy-MM-dd"),
                            truckNumber,
                            etDestination.getText ().toString (),
                            etOrderNo.getText ().toString (),
                            etInvoiceNo.getText ().toString (),
                            etCashAdvance.getText ().toString (),
                            etDieselAdvance.getText ().toString (),
                            etBillRate.getText ().toString ());
                }
            }
        });
    
        etNumberPlate1.addTextChangedListener (new TextWatcher () {
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etNumberPlate1.getText ().toString ().length () == 2)     //size as per your requirement
                {
                    etNumberPlate2.requestFocus ();
                }
            }
        
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            
            }
        
            public void afterTextChanged (Editable s) {
                // TODO Auto-generated method stub
            }
        });
        etNumberPlate2.addTextChangedListener (new TextWatcher () {
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etNumberPlate2.getText ().toString ().length () == 2)     //size as per your requirement
                {
                    etNumberPlate3.requestFocus ();
                }
            }
    
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
        
            }
    
            public void afterTextChanged (Editable s) {
                // TODO Auto-generated method stub
            }
        });
        etNumberPlate3.addTextChangedListener (new TextWatcher () {
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etNumberPlate3.getText ().toString ().length () == 3) {
                    etNumberPlate4.requestFocus ();
                }
            }
    
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
        
            }
    
            public void afterTextChanged (Editable s) {
                // TODO Auto-generated method stub
            }
        });
        etNumberPlate4.addTextChangedListener (new TextWatcher () {
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etNumberPlate4.getText ().toString ().length () == 4) {
                    etDestination.requestFocus ();
                }
            }
    
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
        
            }
    
            public void afterTextChanged (Editable s) {
                // TODO Auto-generated method stub
            }
        });
    
    
        tvSelectDestination.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                getAllDestinations ();
            }
        });
    
        tvSelectTruckNumber.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                truckDialog.show ();
                getTruckList ();
            }
        });
    }
    
    
    private void getTruckList () {
        if (NetworkConnection.isNetworkAvailable (TruckEntryActivity.this)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_GET_TRUCK_DETAILS, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.URL_GET_TRUCK_DETAILS,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            truckDetailList.clear ();
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.TRUCKS);
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            truckDetailList.add (new TruckDetail (
                                                    jsonObject.getInt (AppConfigTags.TRUCK_ID),
                                                    jsonObject.getString (AppConfigTags.TRUCK_NUMBER),
                                                    jsonObject.getString (AppConfigTags.TRUCK_OWNER_NAME),
                                                    jsonObject.getString (AppConfigTags.TRUCK_OWNER_MOBILE),
                                                    jsonObject.getString (AppConfigTags.TRUCK_OWNER_PAN_CARD)));
                                        }
                                        truckDetailAdapter.notifyDataSetChanged ();
                                        if (jsonArray.length () > 0) {
                                            progressBarTrucks.setVisibility (View.GONE);
                                        }
                                        
                                        //    truckDialog.show ();
                                    } else {
                                        Utils.showSnackBar (TruckEntryActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    Utils.showSnackBar (TruckEntryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (TruckEntryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (TruckEntryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (TruckEntryActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
            
            
        }
    }
    
    private void getAllDestinations () {
        if (NetworkConnection.isNetworkAvailable (TruckEntryActivity.this)) {
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_GET_DESTINATION, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.URL_GET_DESTINATION,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        ArrayList<Destination> destinationList = new ArrayList<> ();
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.DESTINATIONS);
                                        JSONObject jsonObject;
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            jsonObject = jsonArray.getJSONObject (i);
                                            destinationList.add (new Destination (
                                                    jsonObject.getInt (AppConfigTags.DESTINATION_ID),
                                                    jsonObject.getInt (AppConfigTags.DESTINATION_PARTY_ID),
                                                    jsonObject.getString (AppConfigTags.DESTINATION_NAME),
                                                    jsonObject.getString (AppConfigTags.DESTINATION_ADDRESS)));
                                        }
                                        
                                    } else {
                                        Utils.showSnackBar (TruckEntryActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                } catch (Exception e) {
                                    Utils.showSnackBar (TruckEntryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (TruckEntryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (TruckEntryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (TruckEntryActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    private void UpdateTruckEntryToServer (final String lr_number, final String weight, final String date, final String truck_number, final String destination, final String order_number, final String invoice_number, final String cash_advance, final String diesel_advance, final String bill_date) {
        if (NetworkConnection.isNetworkAvailable (TruckEntryActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_TRUCK_ENTRY, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_TRUCK_ENTRY,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        new MaterialDialog.Builder (TruckEntryActivity.this)
                                                .content (message)
                                                .positiveText ("OK")
                                                .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                    @Override
                                                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        dialog.dismiss ();
                                                        finish ();
                                                    }
                                                })
                                                .show ();
                                    } else {
                                        Utils.showSnackBar (TruckEntryActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (TruckEntryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (TruckEntryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
                            }
                            Utils.showSnackBar (TruckEntryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                            progressDialog.dismiss ();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.LR_NUMBER, lr_number);
                    params.put (AppConfigTags.WEIGHT, weight);
                    params.put (AppConfigTags.DATE, date);
                    params.put (AppConfigTags.TRUCK_NUMBER, truck_number);
                    params.put (AppConfigTags.DESTINATION, destination);
                    params.put (AppConfigTags.ORDER_NUMBER, order_number);
                    params.put (AppConfigTags.INVOICE_NUMBER, invoice_number);
                    params.put (AppConfigTags.COMPANY_ID, userDetailsPref.getStringPref (TruckEntryActivity.this, UserDetailsPref.COMPANY_ID));
                    params.put (AppConfigTags.PARTY_ID, userDetailsPref.getStringPref (TruckEntryActivity.this, UserDetailsPref.PARTY_ID));
                    params.put (AppConfigTags.CASH_ADVANCE, cash_advance);
                    params.put (AppConfigTags.DIESEL_ADVANCE, diesel_advance);
                    params.put (AppConfigTags.BILL_RATE, bill_date);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (TruckEntryActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
}