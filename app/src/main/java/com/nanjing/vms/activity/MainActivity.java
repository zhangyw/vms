package com.nanjing.vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hik.consts.Constants;
import com.hik.data.TempData;
import com.hik.resource.ResourceListActivity;
import com.hik.util.UIUtil;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.nanjing.vms.R;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.model.Member;
import com.nanjing.vms.utils.CommonUtils;
import com.nanjing.vms.utils.Logger;
import com.nanjing.vms.utils.StringUtils;
import com.nanjing.vms.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class MainActivity extends BaseTitleActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        setTitle("VMS", null, NO_RES_ID, "注销", NO_RES_ID);
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
        findViewById(R.id.layout_monitor).setOnClickListener(this);
        findViewById(R.id.layout_vehicle_management).setOnClickListener(this);
        findViewById(R.id.layout_vehicle_logs).setOnClickListener(this);
        findViewById(R.id.layout_vehicle_pending).setOnClickListener(this);
        findViewById(R.id.layout_vehicle_black).setOnClickListener(this);
        findViewById(R.id.layout_emp_list).setOnClickListener(this);
        findViewById(R.id.layout_emp_pending).setOnClickListener(this);
        findViewById(R.id.layout_emp_logs).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.layout_monitor://视频监控
                startVmsActivity();
                break;
            case R.id.layout_vehicle_management://车辆管理
                intent.setClass(this, VehicleListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_vehicle_logs://车辆出入管理
//                intent.setClass(this, VehicleLogsListActivity.class);
                intent.setClass(this, VehicleLogsSearchListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_vehicle_pending://车辆待审批列表
                intent.setClass(this, VehiclePendingListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_vehicle_black://车辆黑名单列表
                intent.setClass(this, VehicleBlackListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_emp_list://人员管理
                intent.setClass(this, EmpListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_emp_logs://人员出入记录
                intent.setClass(this, EmpLogSearchListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_emp_pending://人员待审批列表
                intent.setClass(this, EmpPendingListActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void startVmsActivity() {
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        if (!TextUtils.isEmpty(member.getMonitorPassword()) && !TextUtils.isEmpty(member.getMonitorUsername())) {
            fetchLine();
        } else {
            ToastUtils.showToast(getApplicationContext(), getString(R.string.vms_permission_denied));
        }
    }


    /**
     * 获取线路
     */
    protected void fetchLine() {
        final String vms_url = StringUtils.getBaseUrl(Share.getString(Share.KEY_VIDEO_ADDRESS, getResources().getString(R.string.video_address)),
                Share.getString(Share.KEY_VIDEO_PORT, getResources().getString(R.string.video_port)));
        final List<LineInfo> lineInfoList = new ArrayList<>();
        if (TextUtils.isEmpty(vms_url)) {
            UIUtil.showToast(this, "请先设置视频监控服务器地址");
            return;
        }

        showLoadingDialog("正在自动获取线路");
        Observable
                .create(new Observable.OnSubscribe<List<LineInfo>>() {
                    @Override
                    public void call(Subscriber<? super List<LineInfo>> subscriber) {
                        boolean ret = VMSNetSDK.getInstance().getLineList(vms_url, lineInfoList);
                        if (ret) {
                            subscriber.onNext(lineInfoList);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception());
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LineInfo>>() {
                    @Override
                    public void onCompleted() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        ToastUtils.showToast(getApplicationContext(), "获取线路失败,请检查VPN连接和服务器地址设置是否正确");
                    }

                    @Override
                    public void onNext(List<LineInfo> list) {
                        if (list != null && list.size() > 0) {
                            Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
                            loginVms(vms_url, member.getMonitorUsername(), member.getMonitorPassword(), list.get(0).lineID);
                        }
                    }
                });
    }

    private void loginVms(final String servAddr, final String userName, final String password, final int lineId) {

        showLoadingDialog("正在自动登录");
        final ServInfo servInfo = new ServInfo();
        final String macAddress = CommonUtils.getMac(getApplicationContext());
        Observable
                .create(new Observable.OnSubscribe<ServInfo>() {
                    @Override
                    public void call(Subscriber<? super ServInfo> subscriber) {
                        boolean ret = VMSNetSDK.getInstance().login(servAddr, userName, password, lineId, macAddress,
                                servInfo);
                        if (ret) {
                            subscriber.onNext(servInfo);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception());
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ServInfo>() {
                    @Override
                    public void onCompleted() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        ToastUtils.showToast(getApplicationContext(), "自动登录失败");
                    }

                    @Override
                    public void onNext(ServInfo servInfo) {
                        Logger.d(this, "==========" + servInfo.userID);
                        TempData.getIns().setLoginData(servInfo);
                        gotoResourceListActivity();
                    }
                });
    }

    private void gotoResourceListActivity() {
        Intent intent =new Intent(this, ResourceListActivity.class);
        //新增，直接进入市辖区下一级。跳过主控制中心、江苏省、南京市、市辖区。
        intent.putExtra(Constants.IntentKey.CONTROL_UNIT_ID, 4);
        startActivity(intent);

    }
}
