package com.nanjing.vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nanjing.vms.R;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class LoadingActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler(){}.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(LoadingActivity.this,LoginActivity.class));
                        LoadingActivity.this.finish();
                    }
                });
            }
        },1000);
    }
}
