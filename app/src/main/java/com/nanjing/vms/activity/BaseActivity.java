package com.nanjing.vms.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.nanjing.vms.R;

import java.util.LinkedList;

/**
 * Created by Jacky on 2016/2/14.
 * Version 1.0
 */
public class BaseActivity extends AppCompatActivity{


    public static final int NO_RES_ID = -1;
    public static LinkedList<Activity> activities = new LinkedList<>();

    private ProgressDialog mLoadingDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
    }

    protected void killAll() {
        for (int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    /**
     * FLAG_TRANSLUCENT_STATUS
     */
    public void setImmersionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    /**
     * 网络请求错误提示
     */
    protected void showRequestError() {
        Toast.makeText(this, "网络请求异常,请检查网络配置", Toast.LENGTH_SHORT).show();
    }

    public String getTextNoSpace(TextView tv) {
        return tv.getText().toString().trim();
    }




    protected void showLoadingDialog(){
        showLoadingDialog(getString(R.string.network_loading));
    }
    protected  void showLoadingDialog(String message) {
       showLoadingDialog(message,true);
    }

    protected void showLoadingDialog(String message,boolean cancelable){
        if(mLoadingDialog!=null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = ProgressDialog.show(this,null,message);
        mLoadingDialog.setCancelable(cancelable);
}


    protected void dismissLoadingDialog(){
        if(mLoadingDialog!=null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }
}
