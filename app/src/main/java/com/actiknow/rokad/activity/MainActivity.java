package com.actiknow.rokad.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actiknow.rokad.R;
import com.actiknow.rokad.utils.SetTypeFace;
import com.actiknow.rokad.utils.UserDetailsPref;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView tvTruckEntry;
    TextView tvReceiving;
    TextView tvRokad;
    TextView tvLogout;
    UserDetailsPref userDetailsPref;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        initView();
        initData();
        initListener();
        isLogin();
    }

    private void isLogin() {
        if(userDetailsPref.getStringPref(MainActivity.this, UserDetailsPref.USER_LOGIN_KEY) == ""){
            Intent myIntent = new Intent (this, LoginActivity.class);
            startActivity (myIntent);
            finish ();
        }
    }

    private void initListener() {
        tvTruckEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTruckEntry = new Intent(MainActivity.this,TruckEntryActivity.class);
                startActivity(intentTruckEntry);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        tvReceiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTruckEntry = new Intent(MainActivity.this,ReceivingActivity.class);
                startActivity(intentTruckEntry);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        tvRokad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogOutDialog();
            }
        });
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
    }

    private void initView() {
        tvTruckEntry = (TextView)findViewById(R.id.tvTruckEntry);
        tvReceiving = (TextView)findViewById(R.id.tvReceiving);
        tvRokad = (TextView)findViewById(R.id.tvRokad);
        tvLogout = (TextView)findViewById(R.id.tvLogout);
    }

    private void showLogOutDialog () {
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .limitIconToDefaultSize ()
                .content ("Do you wish to Sign Out?")
                .positiveText ("Yes")
                .negativeText ("No")
                .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                        userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.USER_MOBILE, "");
                        userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.USER_LOGIN_KEY, "");



                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
    }
}
