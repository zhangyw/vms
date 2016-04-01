package com.nanjing.vms.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.nanjing.vms.R;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.utils.StringUtils;
import com.nanjing.vms.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class SettingActivity extends BaseTitleActivity {
    @Bind(R.id.et_its_host)
    EditText etItsHost;
    @Bind(R.id.et_its_port)
    EditText etItsPort;
    @Bind(R.id.et_video_host)
    EditText etVideoHost;
    @Bind(R.id.et_video_port)
    EditText etVideoPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setTitle("设置", null, NO_RES_ID);
        setValue();
    }

    private void setValue(){
        etItsHost.setText(Share.getString(Share.KEY_ITS_ADDRESS,getResources().getString(R.string.its_address)));
        etItsPort.setText(Share.getString(Share.KEY_ITS_PORT,getResources().getString(R.string.its_port)));
        etVideoHost.setText(Share.getString(Share.KEY_VIDEO_ADDRESS,getResources().getString(R.string.video_address)));
        etVideoPort.setText(Share.getString(Share.KEY_VIDEO_PORT, getResources().getString(R.string.video_port)));
    }

    @Override
    public void onBackPressed() {
        String its_host = getTextNoSpace(etItsHost);
        String its_port = getTextNoSpace(etItsPort);
        String video_host = getTextNoSpace(etVideoHost);
        String video_port = getTextNoSpace(etVideoPort);

        if(TextUtils.isEmpty(its_host)|| !StringUtils.isIpCorrect(its_host)){
            ToastUtils.showToast(getApplicationContext(),"请输入正确的IP地址");
            return ;
        }
        if(TextUtils.isEmpty(its_port)|| !StringUtils.isPortCorrect(its_port)){
            ToastUtils.showToast(getApplicationContext(),"请输入正确的端口号");
            return ;
        }
        if(TextUtils.isEmpty(video_host)|| !StringUtils.isIpCorrect(video_host)){
            ToastUtils.showToast(getApplicationContext(),"请输入正确的IP地址");
            return ;
        }
        if(TextUtils.isEmpty(video_port)|| !StringUtils.isPortCorrect(video_port)){
            ToastUtils.showToast(getApplicationContext(),"请输入正确的端口号");
            return ;
        }

        if(!its_host.equals(Share.getString(Share.KEY_ITS_ADDRESS,getString(R.string.its_address))) ||
                !its_port.equals(Share.getString(Share.KEY_ITS_PORT,getString(R.string.its_port)))){
            RetrofitClient.reset();
        }
        Share.putString(Share.KEY_ITS_ADDRESS,getTextNoSpace(etItsHost));
        Share.putString(Share.KEY_ITS_PORT,getTextNoSpace(etItsPort));
        Share.putString(Share.KEY_VIDEO_ADDRESS,getTextNoSpace(etVideoHost));
        Share.putString(Share.KEY_VIDEO_PORT,getTextNoSpace(etVideoPort));
        super.onBackPressed();
    }
}
