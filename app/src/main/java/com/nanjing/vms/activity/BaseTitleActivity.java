package com.nanjing.vms.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanjing.vms.R;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class BaseTitleActivity extends BaseActivity {
    public RelativeLayout titleParentlayout;
    private TextView titleLefttextview;
    private ImageView titleLeftimageview;
    private TextView titleCentertextview;
    private ImageView titleCenterimageview;
    public TextView titleRighttextview;
    private ImageView titleRightimageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setImmersiveStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void setTitle(String title) {
        setTitle(title, null, NO_RES_ID, null, NO_RES_ID);
    }

    public void setTitle(String title, String rightTitle, int rightResId) {
        setTitle(title, null, R.mipmap.btn_back, rightTitle, rightResId);
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * @param title      标题
     * @param leftTitle  左侧按钮文字 不显示为null
     * @param leftResId  左侧按钮图片 不显示为 {@link BaseActivity#NO_RES_ID}
     * @param rightTitle 右侧按钮文字 不显示为null
     * @param rightResId 右侧按钮图片 不显示为 {@link BaseActivity#NO_RES_ID}
     */
    public void setTitle(String title, String leftTitle, int leftResId, String rightTitle, int rightResId) {

        titleParentlayout = (RelativeLayout) findViewById(R.id.title_parentlayout);
        titleLefttextview = (TextView) findViewById(R.id.title_lefttextview);
        titleLeftimageview = (ImageView) findViewById(R.id.title_leftimageview);
        titleCentertextview = (TextView) findViewById(R.id.title_centertextview);
        titleCenterimageview = (ImageView) findViewById(R.id.title_centerimageview);
        titleRighttextview = (TextView) findViewById(R.id.title_righttextview);
        titleRightimageview = (ImageView) findViewById(R.id.title_rightimageview);

        if (!TextUtils.isEmpty(leftTitle)) {
            titleLefttextview.setVisibility(View.VISIBLE);
            titleLefttextview.setText(leftTitle);
        } else {
            titleLefttextview.setVisibility(View.GONE);
        }


        if (leftResId != NO_RES_ID) {
            titleLeftimageview.setVisibility(View.VISIBLE);
            titleLeftimageview.setImageResource(leftResId);
        } else {
            titleLeftimageview.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(rightTitle)) {
            titleRighttextview.setVisibility(View.VISIBLE);
            titleRighttextview.setText(rightTitle);
        } else {
            titleRighttextview.setVisibility(View.GONE);
        }

        if (rightResId != NO_RES_ID) {
            titleRightimageview.setVisibility(View.VISIBLE);
            titleRightimageview.setImageResource(rightResId);
        } else {
            titleRightimageview.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(title)) {
            titleCentertextview.setVisibility(View.VISIBLE);
            titleCentertextview.setText(title);
        } else {
            titleCentertextview.setVisibility(View.GONE);
        }

    }

    public void setBtnLeftOnClickListener(View.OnClickListener listener) {
        if (titleLefttextview.getVisibility() == View.VISIBLE) {
            titleLefttextview.setOnClickListener(listener);
        } else {
            titleLeftimageview.setOnClickListener(listener);
        }
    }


    public void setBtnRightTextOnClickListener(View.OnClickListener listener) {
        if (titleRighttextview.getVisibility() == View.VISIBLE) {
            titleRighttextview.setOnClickListener(listener);
        } else {
            titleRightimageview.setOnClickListener(listener);
        }
    }

    public String getTextNoSpace(TextView tv) {
        return tv.getText().toString().trim();
    }

    public void setRightImage(int resId) {
        if (resId == NO_RES_ID) {
            if (titleRightimageview.getVisibility() != View.GONE) {

            }
        } else {
            if (titleRightimageview.getVisibility() != View.VISIBLE) {
                titleRightimageview.setVisibility(View.VISIBLE);
            }
            titleRightimageview.setImageResource(resId);
        }
    }
}
