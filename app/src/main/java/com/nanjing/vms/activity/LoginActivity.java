package com.nanjing.vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nanjing.vms.BuildConfig;
import com.nanjing.vms.R;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.model.Member;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.network.resp.LoginResp;
import com.nanjing.vms.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class LoginActivity extends BaseTitleActivity {

    private EditText etUserId;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_vms);
        initView();
    }

    private void initView() {
        setTitle("登录", "设置", NO_RES_ID);
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SettingActivity.class));
            }
        });

        etUserId = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BuildConfig.DEBUG){
                    Member member = new Member();
                    member.setMemberID("12");
                    member.setAccessToken("abcdefg");
                    member.setmMemberType(5);
                    member.setMonitorUsername("public");
                    member.setMonitorPassword("888888");
                    Share.putObject(Share.KEY_MEMBER_INFO,member);
                    startMainActivity();
                    return;
                }

                String user_name = getTextNoSpace(etUserId);
                String password = getTextNoSpace(etPassword);
                if (TextUtils.isEmpty(user_name)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showToast(getApplicationContext(), "请输入密码");
                    return;
                }
                login(getTextNoSpace(etUserId), getTextNoSpace(etPassword));
            }
        });
    }

    private void login(String username, String password) {
        showLoadingDialog();
        Call<LoginResp> loginCall = RetrofitClient.vmsApis().loginprocess(username, password);
        loginCall.enqueue(new Callback<LoginResp>() {
            @Override
            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                dismissLoadingDialog();
                if (response.body() != null) {
                    LoginResp resp = response.body();
                    if (resp.isSuccess()) {
                        Share.putObject(Share.KEY_MEMBER_INFO, resp.getData());
                        startMainActivity();
                    } else {
                        ToastUtils.showToast(getApplicationContext(), resp.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResp> call, Throwable t) {
                dismissLoadingDialog();
                ToastUtils.showToast(getApplicationContext(), getResources().getString(R.string.msg_net_error));
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
