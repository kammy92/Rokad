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
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actiknow.rokad.R;
import com.actiknow.rokad.adapter.DestinationAdapter;
import com.actiknow.rokad.adapter.PartyAdapter;
import com.actiknow.rokad.adapter.TruckAdapter;
import com.actiknow.rokad.model.Destination;
import com.actiknow.rokad.model.Party;
import com.actiknow.rokad.model.Truck;
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
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by actiknow on 7/26/17.
 */

public class TruckEntryActivity extends AppCompatActivity {
    final List<Truck> truckList = new ArrayList<> ();
    final List<Destination> destinationList = new ArrayList<> ();
    final List<Party> partyList = new ArrayList<> ();
    
    EditText etPartyName;
    TextView tvSelectParty;
    EditText etLrNo;
    EditText etWeight;
    EditText etDate;
    EditText etTruckNumber;
    TextView tvSelectTruckNumber;
    EditText etDestination;
    TextView tvSelectDestination;
    EditText etCompanyRate;
    EditText etBillRate;
    EditText etDeliveryNo;
    EditText etInvoiceNo;
    EditText etCashAdvance;
    EditText etDieselAdvance;
    TextView tvTotalAdvance;
    EditText etTotalAdvance;
    TextView tvTotalCompanyBilling;
    EditText etTotalCompanyBilling;
    TextView tvTotalBhada;
    EditText etTotalBhada;
    
    CoordinatorLayout clMain;
    TextView tvSubmit;
    UserDetailsPref userDetailsPref;
    RelativeLayout rlBack;
    
    TruckAdapter truckAdapter;
    MaterialDialog truckDialog;
    RecyclerView rvTrucks;
    LinearLayout llAddTruckDetail;
    RelativeLayout rlTruckList;
    ProgressBar progressBarTrucks;
    
    DestinationAdapter destinationAdapter;
    MaterialDialog destinationDialog;
    RecyclerView rvDestinations;
    LinearLayout llAddDestination;
    RelativeLayout rlDestinationList;
    ProgressBar progressBarDestination;
    
    PartyAdapter partyAdapter;
    MaterialDialog partyDialog;
    RecyclerView rvParty;
    LinearLayout llAddParty;
    RelativeLayout rlPartyList;
    ProgressBar progressBarParty;
    
    int party_id = 0;
    int destination_id = 0;
    int truck_id = 0;
    
    
    ProgressDialog progressDialog;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_truck_entry);
        initView ();
        initData ();
        initDialogs ();
        initListener ();
    }
    
    private void initView () {
        etLrNo = (EditText) findViewById (R.id.etLrNo);
        etWeight = (EditText) findViewById (R.id.etWeight);
        etDate = (EditText) findViewById (R.id.etDate);
        etDestination = (EditText) findViewById (R.id.etDestination);
        etDeliveryNo = (EditText) findViewById (R.id.etOrderNo);
        etInvoiceNo = (EditText) findViewById (R.id.etInvoiceNo);
        etPartyName = (EditText) findViewById (R.id.etParty);
        etCashAdvance = (EditText) findViewById (R.id.etCashAdvance);
        etDieselAdvance = (EditText) findViewById (R.id.etDieselAdvance);
        tvTotalAdvance = (TextView) findViewById (R.id.tvTotalAdvance);
        etTotalAdvance = (EditText) findViewById (R.id.etTotalAdvance);
        etBillRate = (EditText) findViewById (R.id.etBillRate);
        tvTotalCompanyBilling = (TextView) findViewById (R.id.tvTotalCompanyBilling);
        etCompanyRate = (EditText) findViewById (R.id.etCompanyRate);
        etTotalCompanyBilling = (EditText) findViewById (R.id.etTotalCompanyBilling);
        tvTotalBhada = (TextView) findViewById (R.id.tvTotalBhada);
        etTotalBhada = (EditText) findViewById (R.id.etTotalBhada);
        etTruckNumber = (EditText) findViewById (R.id.etTruckNumber);
        tvSubmit = (TextView) findViewById (R.id.tvSubmit);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
    
    
        tvSelectTruckNumber = (TextView) findViewById (R.id.tvSelectTruckNumber);
        tvSelectDestination = (TextView) findViewById (R.id.tvSelectDestination);
        tvSelectParty = (TextView) findViewById (R.id.tvSelectParty);
    }
    
    public void initData () {
        userDetailsPref = UserDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (this);
        etBillRate.setFilters (new InputFilter[] {new DecimalDigitsInputFilter (10, 2)});
        etWeight.setFilters (new InputFilter[] {new DecimalDigitsInputFilter (10, 2)});
        etCashAdvance.setFilters (new InputFilter[] {new DecimalDigitsInputFilter (10, 2)});
        etDieselAdvance.setFilters (new InputFilter[] {new DecimalDigitsInputFilter (10, 2)});
//        etPartyName.setText (userDetailsPref.getStringPref (TruckEntryActivity.this, UserDetailsPref.PARTY_NAME));
    }
    
    private void initDialogs () {
        truckAdapter = new TruckAdapter (TruckEntryActivity.this, truckList);
        truckDialog = new MaterialDialog.Builder (TruckEntryActivity.this)
                .customView (R.layout.dialog_truck, true)
//                .neutralText ("ADD")
//                .neutralColor (getResources ().getColor (R.color.accent))
//                .positiveText ("REFRESH")
//                .positiveColor (getResources ().getColor (R.color.accent))
                .build ();
        
        rvTrucks = (RecyclerView) truckDialog.getCustomView ().findViewById (R.id.rvTruckList);
        rlTruckList = (RelativeLayout) truckDialog.getCustomView ().findViewById (R.id.rlTruckList);
        llAddTruckDetail = (LinearLayout) truckDialog.getCustomView ().findViewById (R.id.llAddTruckDetail);
        progressBarTrucks = (ProgressBar) truckDialog.getCustomView ().findViewById (R.id.progressBarTrucks);
        
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
        
        truckAdapter.SetOnItemClickListener (new TruckAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                Truck truck = truckList.get (position);
                etTruckNumber.setText (truck.getTruck_number ());
                truck_id = truck.getId ();
                etTruckNumber.setError (null);
                truckDialog.dismiss ();
            }
        });
        
        rvTrucks.setAdapter (truckAdapter);
        rvTrucks.setHasFixedSize (true);
        rvTrucks.setLayoutManager (new LinearLayoutManager (TruckEntryActivity.this, LinearLayoutManager.VERTICAL, false));
        rvTrucks.setItemAnimator (new DefaultItemAnimator ());
        
        
        destinationAdapter = new DestinationAdapter (TruckEntryActivity.this, destinationList);
        destinationDialog = new MaterialDialog.Builder (TruckEntryActivity.this)
                .customView (R.layout.dialog_destination, true)
//                .neutralText ("ADD")
//                .neutralColor (getResources ().getColor (R.color.accent))
//                .positiveText ("REFRESH")
//                .positiveColor (getResources ().getColor (R.color.accent))
                .build ();
        
        rvDestinations = (RecyclerView) destinationDialog.getCustomView ().findViewById (R.id.rvDestinationList);
        rlDestinationList = (RelativeLayout) destinationDialog.getCustomView ().findViewById (R.id.rlDestinationList);
        llAddDestination = (LinearLayout) destinationDialog.getCustomView ().findViewById (R.id.llAddDestination);
        progressBarDestination = (ProgressBar) destinationDialog.getCustomView ().findViewById (R.id.progressBarDestination);
        
        destinationDialog.getActionButton (DialogAction.NEUTRAL).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (rlDestinationList.getVisibility () == View.VISIBLE) {
                    destinationDialog.getActionButton (DialogAction.NEUTRAL).setText ("CANCEL");
                    destinationDialog.getActionButton (DialogAction.POSITIVE).setText ("SAVE");
                    rlDestinationList.setVisibility (View.GONE);
                    llAddDestination.setVisibility (View.VISIBLE);
                } else {
                    destinationDialog.getActionButton (DialogAction.NEUTRAL).setText ("ADD");
                    destinationDialog.getActionButton (DialogAction.POSITIVE).setText ("REFRESH");
                    rlDestinationList.setVisibility (View.VISIBLE);
                    llAddDestination.setVisibility (View.GONE);
                }
            }
        });
        
        destinationAdapter.SetOnItemClickListener (new DestinationAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                Destination destination = destinationList.get (position);
                etDestination.setText (destination.getName ());
                destination_id = destination.getId ();
                etDestination.setError (null);
                etCompanyRate.setText (String.format (Locale.ENGLISH, "%.2f", destination.getRate ()));
                etTotalCompanyBilling.setText (String.format (Locale.ENGLISH, "%.2f", (destination.getRate () * Double.parseDouble (etWeight.getText ().toString ()))));
                tvTotalCompanyBilling.setText (getResources ().getString (R.string.activity_total_company_billing) + " (" + etWeight.getText ().toString () + " * " + String.format ("%.2f", destination.getRate ()) + ")");
                destinationDialog.dismiss ();
            }
        });
        
        rvDestinations.setAdapter (destinationAdapter);
        rvDestinations.setHasFixedSize (true);
        rvDestinations.setLayoutManager (new LinearLayoutManager (TruckEntryActivity.this, LinearLayoutManager.VERTICAL, false));
        rvDestinations.setItemAnimator (new DefaultItemAnimator ());
        
        
        partyAdapter = new PartyAdapter (TruckEntryActivity.this, partyList);
        partyDialog = new MaterialDialog.Builder (TruckEntryActivity.this)
                .customView (R.layout.dialog_party, true)
//                .neutralText ("ADD")
//                .neutralColor (getResources ().getColor (R.color.accent))
//                .positiveText ("REFRESH")
//                .positiveColor (getResources ().getColor (R.color.accent))
                .build ();
        
        rvParty = (RecyclerView) partyDialog.getCustomView ().findViewById (R.id.rvPartyList);
        rlPartyList = (RelativeLayout) partyDialog.getCustomView ().findViewById (R.id.rlPartyList);
        llAddParty = (LinearLayout) partyDialog.getCustomView ().findViewById (R.id.llAddParty);
        progressBarParty = (ProgressBar) partyDialog.getCustomView ().findViewById (R.id.progressBarParty);
        
        partyDialog.getActionButton (DialogAction.NEUTRAL).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (rlPartyList.getVisibility () == View.VISIBLE) {
                    partyDialog.getActionButton (DialogAction.NEUTRAL).setText ("CANCEL");
                    partyDialog.getActionButton (DialogAction.POSITIVE).setText ("SAVE");
                    rlPartyList.setVisibility (View.GONE);
                    llAddParty.setVisibility (View.VISIBLE);
                } else {
                    partyDialog.getActionButton (DialogAction.NEUTRAL).setText ("ADD");
                    partyDialog.getActionButton (DialogAction.POSITIVE).setText ("REFRESH");
                    rlPartyList.setVisibility (View.VISIBLE);
                    llAddParty.setVisibility (View.GONE);
                }
            }
        });
        
        partyAdapter.SetOnItemClickListener (new PartyAdapter.OnItemClickListener () {
            @Override
            public void onItemClick (View view, int position) {
                Party party = partyList.get (position);
                etPartyName.setText (party.getName ());
                party_id = party.getId ();
                etDestination.setText ("");
                destination_id = 0;
                destinationList.clear ();
                etPartyName.setError (null);
                etDestination.setError (null);
                progressBarDestination.setVisibility (View.VISIBLE);
                partyDialog.dismiss ();
            }
        });
        
        rvParty.setAdapter (partyAdapter);
        rvParty.setHasFixedSize (true);
        rvParty.setLayoutManager (new LinearLayoutManager (TruckEntryActivity.this, LinearLayoutManager.VERTICAL, false));
        rvParty.setItemAnimator (new DefaultItemAnimator ());
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
                etDate.setError (null);
            }
        });
    
        tvSubmit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                boolean flag = true;
                if (party_id == 0) {
                    etPartyName.setError ("Please select Party");
                    flag = false;
                }
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
                if (truck_id == 0) {
                    etTruckNumber.setError ("Please select Truck");
                    flag = false;
                }
                if (destination_id == 0) {
                    etDestination.setError ("Please select Destination");
                    flag = false;
                }
                if (etBillRate.getText ().toString ().length () == 0) {
                    etBillRate.setError ("Please fill Bill Rate");
                    flag = false;
                }
                if (etDeliveryNo.getText ().toString ().length () == 0) {
                    etDeliveryNo.setError ("Please fill Delivery Number");
                    flag = false;
                }
                if (etInvoiceNo.getText ().toString ().length () == 0) {
                    etInvoiceNo.setError ("Please fill Invoice Number");
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
    
                if (Double.parseDouble (etTotalAdvance.getText ().toString ()) > Double.parseDouble (etTotalBhada.getText ().toString ())) {
                    flag = false;
                    Utils.showSnackBar (TruckEntryActivity.this, clMain, "Total Advance should be less than Total Bhada", Snackbar.LENGTH_LONG, null, null);
                }
    
                if (flag) {
                    UpdateTruckEntryToServer (
                            String.valueOf (party_id),
                            etLrNo.getText ().toString (),
                            etWeight.getText ().toString (),
                            Utils.convertTimeFormat (etDate.getText ().toString (), "dd-MM-yyyy", "yyyy-MM-dd"),
                            String.valueOf (truck_id),
                            String.valueOf (destination_id),
                            etCompanyRate.getText ().toString (),
                            etBillRate.getText ().toString (),
                            etDeliveryNo.getText ().toString (),
                            etInvoiceNo.getText ().toString (),
                            etCashAdvance.getText ().toString (),
                            etDieselAdvance.getText ().toString (),
                            etTotalAdvance.getText ().toString (),
                            etTotalCompanyBilling.getText ().toString (),
                            etTotalBhada.getText ().toString ()
                    );
                }
            }
        });
        
        tvSelectDestination.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (party_id > 0 && etWeight.getText ().toString ().length () > 0) {
                    destinationDialog.show ();
                    getDestinationList ();
                } else if (party_id == 0) {
                    Utils.showSnackBar (TruckEntryActivity.this, clMain, "Please select Party first", Snackbar.LENGTH_SHORT, "SELECT", new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            partyDialog.show ();
                            getPartyList ();
                        }
                    });
                } else if (etWeight.getText ().toString ().length () == 0) {
                    Utils.showSnackBar (TruckEntryActivity.this, clMain, "Please enter weight first", Snackbar.LENGTH_SHORT, "ENTER", new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            etWeight.requestFocus ();
                        }
                    });
                }
            }
        });
    
        tvSelectTruckNumber.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                truckDialog.show ();
                getTruckList ();
            }
        });
        tvSelectParty.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                partyDialog.show ();
                getPartyList ();
            }
        });
    
        etCashAdvance.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () > 0 && etDieselAdvance.getText ().toString ().length () > 0) {
                    etTotalAdvance.setText (String.format (Locale.ENGLISH, "%.2f", (Double.parseDouble (s.toString ()) + Double.parseDouble (etDieselAdvance.getText ().toString ()))));
                    tvTotalAdvance.setText (getResources ().getString (R.string.activity_total_advance) + " (" + s.toString () + " + " + etDieselAdvance.getText ().toString () + ")");
                } else if (s.length () == 0 && etDieselAdvance.getText ().toString ().length () > 0) {
                    etTotalAdvance.setText (String.format (Locale.ENGLISH, "%.2f", Double.parseDouble (etDieselAdvance.getText ().toString ())));
                    tvTotalAdvance.setText (getResources ().getString (R.string.activity_total_advance) + " (0 + " + etDieselAdvance.getText ().toString () + ")");
                } else if (etDieselAdvance.getText ().toString ().length () == 0 && s.length () > 0) {
                    etTotalAdvance.setText (String.format (Locale.ENGLISH, "%.2f", Double.parseDouble (s.toString ())));
                    tvTotalAdvance.setText (getResources ().getString (R.string.activity_total_advance) + " (" + s.toString () + " + 0)");
                } else {
                    etTotalAdvance.setText ("");
                    tvTotalAdvance.setText (getResources ().getString (R.string.activity_total_advance));
                }
            }
        
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
        
            @Override
            public void afterTextChanged (Editable s) {
            
            }
        });
        etDieselAdvance.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () > 0 && etCashAdvance.getText ().toString ().length () > 0) {
                    etTotalAdvance.setText (String.format (Locale.ENGLISH, "%.2f", (Double.parseDouble (etCashAdvance.getText ().toString ()) + Double.parseDouble (s.toString ()))));
                    tvTotalAdvance.setText (getResources ().getString (R.string.activity_total_advance) + " (" + etCashAdvance.getText ().toString () + " + " + s.toString () + ")");
                } else if (s.length () == 0 && etCashAdvance.getText ().toString ().length () > 0) {
                    etTotalAdvance.setText (String.format (Locale.ENGLISH, "%.2f", Double.parseDouble (etCashAdvance.getText ().toString ())));
                    tvTotalAdvance.setText (getResources ().getString (R.string.activity_total_advance) + " (" + etCashAdvance.getText ().toString () + " + 0)");
                } else if (etCashAdvance.getText ().toString ().length () == 0 && s.length () > 0) {
                    etTotalAdvance.setText (String.format (Locale.ENGLISH, "%.2f", Double.parseDouble (s.toString ())));
                    tvTotalAdvance.setText (getResources ().getString (R.string.activity_total_advance) + " (0 + " + s.toString () + ")");
                } else {
                    etTotalAdvance.setText ("");
                    tvTotalAdvance.setText (getResources ().getString (R.string.activity_total_advance));
                }
            }
    
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
    
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
    
        etBillRate.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () > 0 && etWeight.getText ().toString ().length () > 0) {
                    etTotalBhada.setText (String.format (Locale.ENGLISH, "%.2f", (Double.parseDouble (s.toString ()) * Double.parseDouble (etWeight.getText ().toString ()))));
                    tvTotalBhada.setText (getResources ().getString (R.string.activity_total_bhada) + " (" + etWeight.getText ().toString () + " * " + s.toString () + ")");
                } else {
                    etTotalBhada.setText ("");
                    tvTotalBhada.setText (getResources ().getString (R.string.activity_total_bhada));
                }
            }
        
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
        
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
    
        etWeight.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () > 0) {
                    for (int i = 0; i < destinationList.size (); i++) {
                        if (destinationList.get (i).getId () == destination_id) {
                            etTotalCompanyBilling.setText (String.format (Locale.ENGLISH, "%.2f", (destinationList.get (i).getRate () * Double.parseDouble (etWeight.getText ().toString ()))));
                            tvTotalCompanyBilling.setText (getResources ().getString (R.string.activity_total_company_billing) + " (" + etWeight.getText ().toString () + " * " + destinationList.get (i).getRate () + ")");
                        }
                    }
                    if (etBillRate.getText ().toString ().length () > 0) {
                        etTotalBhada.setText (String.format (Locale.ENGLISH, "%.2f", (Double.parseDouble (etBillRate.getText ().toString ()) * Double.parseDouble (s.toString ()))));
                        tvTotalBhada.setText (getResources ().getString (R.string.activity_total_bhada) + " (" + s.toString () + " * " + String.format (Locale.ENGLISH, "%.2f", Double.parseDouble (etBillRate.getText ().toString ())) + ")");
                    }
                } else {
                    etTotalBhada.setText ("");
                    tvTotalBhada.setText (getResources ().getString (R.string.activity_total_bhada));
                    etTotalCompanyBilling.setText ("");
                    tvTotalCompanyBilling.setText (getResources ().getString (R.string.activity_total_company_billing));
                }
            }
        
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
        
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
    }
    
    private void getPartyList () {
        if (NetworkConnection.isNetworkAvailable (TruckEntryActivity.this)) {
//            partyList.clear ();
//            partyAdapter.notifyDataSetChanged ();
//            progressBarParty.setVisibility (View.VISIBLE);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_GET_PARTIES, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.URL_GET_PARTIES,
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
                                        partyList.clear ();
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.PARTIES);
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            partyList.add (new Party (
                                                    jsonObject.getInt (AppConfigTags.PARTY_ID),
                                                    jsonObject.getString (AppConfigTags.PARTY_NAME),
                                                    jsonObject.getString (AppConfigTags.PARTY_ADDRESS)));
                                        }
                                        partyAdapter.notifyDataSetChanged ();
                                        if (jsonArray.length () > 0) {
                                            progressBarParty.setVisibility (View.GONE);
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
    
    private void getTruckList () {
        if (NetworkConnection.isNetworkAvailable (TruckEntryActivity.this)) {
//            truckList.clear ();
//            truckAdapter.notifyDataSetChanged ();
//            progressBarTrucks.setVisibility (View.VISIBLE);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_GET_TRUCKS, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.URL_GET_TRUCKS,
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
                                        truckList.clear ();
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.TRUCKS);
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject (i);
                                            truckList.add (new Truck (
                                                    jsonObject.getInt (AppConfigTags.TRUCK_ID),
                                                    jsonObject.getString (AppConfigTags.TRUCK_NUMBER),
                                                    jsonObject.getString (AppConfigTags.TRUCK_OWNER_NAME),
                                                    jsonObject.getString (AppConfigTags.TRUCK_OWNER_MOBILE),
                                                    jsonObject.getString (AppConfigTags.TRUCK_OWNER_PAN_CARD)));
                                        }
                                        truckAdapter.notifyDataSetChanged ();
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
    
    private void getDestinationList () {
        if (NetworkConnection.isNetworkAvailable (TruckEntryActivity.this)) {
//            destinationList.clear ();
//            destinationAdapter.notifyDataSetChanged ();
//            progressBarDestination.setVisibility (View.VISIBLE);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_GET_DESTINATIONS, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.GET, AppConfigURL.URL_GET_DESTINATIONS,
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
                                        destinationList.clear ();
                                        JSONArray jsonArray = jsonObj.getJSONArray (AppConfigTags.DESTINATIONS);
                                        JSONObject jsonObject;
                                        for (int i = 0; i < jsonArray.length (); i++) {
                                            jsonObject = jsonArray.getJSONObject (i);
                                            if (jsonObject.getInt (AppConfigTags.DESTINATION_PARTY_ID) == party_id) {
                                                destinationList.add (new Destination (
                                                        jsonObject.getInt (AppConfigTags.DESTINATION_ID),
                                                        jsonObject.getInt (AppConfigTags.DESTINATION_PARTY_ID),
                                                        jsonObject.getString (AppConfigTags.DESTINATION_NAME),
                                                        jsonObject.getString (AppConfigTags.DESTINATION_ADDRESS),
                                                        jsonObject.getDouble (AppConfigTags.DESTINATION_RATE)));
                                            }
                                        }
    
                                        destinationAdapter.notifyDataSetChanged ();
                                        if (jsonArray.length () > 0) {
                                            progressBarDestination.setVisibility (View.GONE);
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
    
    private void UpdateTruckEntryToServer (final String party_id, final String lr_number, final String weight, final String date,
                                           final String truck_id, final String destination_id, final String company_rate,
                                           final String bill_rate, final String delivery_number, final String invoice_number,
                                           final String cash_advance, final String diesel_advance, final String total_advance,
                                           final String total_company_billing, final String total_bhada) {
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
                    params.put (AppConfigTags.COMPANY_ID, userDetailsPref.getStringPref (TruckEntryActivity.this, UserDetailsPref.COMPANY_ID));
                    params.put (AppConfigTags.PARTY_ID, party_id);
                    params.put (AppConfigTags.LR_NUMBER, lr_number);
                    params.put (AppConfigTags.WEIGHT, weight);
                    params.put (AppConfigTags.DATE, date);
                    params.put (AppConfigTags.TRUCK_ID, truck_id);
                    params.put (AppConfigTags.DESTINATION_ID, destination_id);
                    params.put (AppConfigTags.COMPANY_RATE, company_rate);
                    params.put (AppConfigTags.BILL_RATE, bill_rate);
                    params.put (AppConfigTags.DELIVERY_NUMBER, delivery_number);
                    params.put (AppConfigTags.INVOICE_NUMBER, invoice_number);
                    params.put (AppConfigTags.CASH_ADVANCE, cash_advance);
                    params.put (AppConfigTags.DIESEL_ADVANCE, diesel_advance);
                    params.put (AppConfigTags.TOTAL_ADVANCE, total_advance);
                    params.put (AppConfigTags.TOTAL_COMPANY_BILLING, total_company_billing);
                    params.put (AppConfigTags.TOTAL_BHADA, total_bhada);
                    
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
    
    public class DecimalDigitsInputFilter implements InputFilter {
        
        Pattern mPattern;
        
        public DecimalDigitsInputFilter (int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile ("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }
        
        @Override
        public CharSequence filter (CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            
            Matcher matcher = mPattern.matcher (dest);
            if (! matcher.matches ())
                return "";
            return null;
        }
        
    }
}