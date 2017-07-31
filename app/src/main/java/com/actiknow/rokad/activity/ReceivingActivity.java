package com.actiknow.rokad.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.actiknow.rokad.R;

/**
 * Created by actiknow on 7/26/17.
 */

public class ReceivingActivity extends AppCompatActivity{
    RelativeLayout rlBack;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        rlBack=(RelativeLayout)findViewById(R.id.rlBack);
    }

    private void initData() {

    }

    private void initListener() {
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


    }
}
