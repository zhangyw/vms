package com.hik.resource;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hik.callbacks.MsgCallback;
import com.hik.callbacks.MsgIds;
import com.hik.consts.Constants;
import com.hik.data.TempData;
import com.hik.live.LiveActivity;
import com.hik.playback.PlayBackActivity;
import com.hik.util.UIUtil;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.nanjing.vms.R;
import com.nanjing.vms.activity.BaseTitleActivity;

import java.util.List;


public class ResourceListActivity extends BaseTitleActivity implements MsgCallback {
    /**
     * 资源列表
     */
    private ListView resourceListView;
    /**
     * 父节点资源类型，TYPE_UNKNOWN表示首次获取资源列表
     */
    private int pResType = Constants.Resource.TYPE_UNKNOWN;
    /**
     * 父控制中心的id，只有当parentResType为TYPE_CTRL_UNIT才有用
     */
    private int pCtrlUnitId;
    /**
     * 父区域的id，只有当parentResType为TYPE_REGION才有用
     */
    private int pRegionId;
    /**
     * 资源列表适配器
     */
    private ResourceListAdapter adapter;
    /**
     * 消息处理Handler
     */
    private MsgHandler handler = new MsgHandler();
    /**
     * 获取资源逻辑控制类
     */
    private ResourceControl rc;

    @SuppressLint("HandlerLeak")
    private final class MsgHandler extends Handler {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                // 获取控制中心列表成功
                case MsgIds.GET_C_F_NONE_SUC:
                    // 从控制中心获取下级资源列表成功
                case MsgIds.GET_SUB_F_C_SUC:
                    // 从区域获取下级列表成功
                case MsgIds.GET_SUB_F_R_SUC:
                    refreshResList((List<Object>) msg.obj);
                    break;
                // 获取控制中心列表失败
                case MsgIds.GET_C_F_NONE_FAIL:
                    // 调用getControlUnitList失败
                case MsgIds.GET_CU_F_CU_FAIL:
                    // 调用getRegionListFromCtrlUnit失败
                case MsgIds.GET_R_F_C_FAIL:
                    // 调用getCameraListFromCtrlUnit失败
                case MsgIds.GET_C_F_C_FAIL:
                    // 从控制中心获取下级资源列表成失败
                case MsgIds.GET_SUB_F_C_FAIL:
                    // 调用getRegionListFromRegion失败
                case MsgIds.GET_R_F_R_FAIL:
                    // 调用getCameraListFromRegion失败
                case MsgIds.GET_C_F_R_FAIL:
                    // 从区域获取下级列表失败
                case MsgIds.GET_SUB_F_R_FAILED:
                    onGetResListFailed();
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrl_unit_list);

        initUI();

        initData();

        reqResList();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent it = getIntent();
        if (it.hasExtra(Constants.IntentKey.CONTROL_UNIT_ID)) {
            pResType = Constants.Resource.TYPE_CTRL_UNIT;
            pCtrlUnitId = it
                    .getIntExtra(Constants.IntentKey.CONTROL_UNIT_ID, 0);
            Log.i(Constants.LOG_TAG,
                    "Getting resource from ctrlunit.parent id is "
                            + pCtrlUnitId);
        } else if (it.hasExtra(Constants.IntentKey.REGION_ID)) {
            pResType = Constants.Resource.TYPE_REGION;
            pRegionId = it.getIntExtra(Constants.IntentKey.REGION_ID, 0);
            Log.i(Constants.LOG_TAG,
                    "Getting resource from region. parent id is " + pRegionId);
        } else {
            pResType = Constants.Resource.TYPE_UNKNOWN;
            Log.i(Constants.LOG_TAG, "Getting resource for the first time.");
        }
    }

    /**
     * 调用接口失败时，界面弹出提示
     */
    private void onGetResListFailed() {
        UIUtil.showToast(this,
                getString(R.string.fetch_reslist_failed, UIUtil.getErrorDesc()));
    }

    /**
     * 获取数据成功后刷新列表
     *
     * @param data
     */
    private void refreshResList(List<Object> data) {
        if (data == null || data.isEmpty()) {
            UIUtil.showToast(this, R.string.no_data_tip);
            return;
        }
        UIUtil.showToast(this, R.string.fetch_resource_suc);
        adapter.setData(data);
    }

    /**
     * 请求资源列表
     */
    private void reqResList() {
        rc = new ResourceControl();
        rc.setCallback(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pId = 0;
                if (Constants.Resource.TYPE_CTRL_UNIT == pResType) {
                    pId = pCtrlUnitId;
                } else if (Constants.Resource.TYPE_REGION == pResType) {
                    pId = pRegionId;
                }
                rc.reqResList(pResType, pId);
            }
        }).start();
    }

    /**
     * 初始化界面UI控件
     */
    private void initUI() {
        setTitle("视频监控", null, NO_RES_ID);
        resourceListView = (ListView) findViewById(R.id.ctrlunit_list);
        resourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getItemAtPosition(position);
                gotoNextLevelList(object);
            }
        });
        adapter = new ResourceListAdapter(this);
        resourceListView.setAdapter(adapter);
    }

    /**
     *
     *
     * @param info
     */
    protected void gotoNextLevelList(final Object info) {
        //当前是监控点（摄像头）
        if (info instanceof CameraInfo) {
            gotoLiveOrPlayBack((CameraInfo)info);
            return;
        }

        // 当前是控制中心
        if (info instanceof ControlUnitInfo) {

            gotoNextLevelListFromCtrlUnit((ControlUnitInfo)info);
            return;
        }

        // 当前是区域
        if (info instanceof RegionInfo) {

            gotoNextLevelListFromRegion((RegionInfo)info);
            return ;
        }
    }


    private Dialog mDialog;
    private void gotoLiveOrPlayBack(final CameraInfo info) {
        String[] datas = new String[]{"预览","回放"};
        mDialog = new AlertDialog.Builder(this).setSingleChoiceItems(datas, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();
                switch (which) {

                    case 0:
                        gotoLive(info);
                        break;
                    case 1:
                        gotoPlayback(info);
                        break;
                    default:
                        break;
                }
            }
        }).create();
        mDialog.show();

    }

    /**
     * 进入远程回放
     * @param info
     * @since V1.0
     */
    protected void gotoPlayback(CameraInfo info) {
        if(info == null){
            Log.e(Constants.LOG_TAG, "gotoPlayback():: fail");
            return;
        }
        Intent it = new Intent(this, PlayBackActivity.class);
        it.putExtra(Constants.IntentKey.CAMERA_ID, info.cameraID);
        it.putExtra(Constants.IntentKey.DEVICE_ID, info.deviceID);
        this.startActivity(it);

    }

    /**
     * 进入实时预览
     * @param info
     * @since V1.0
     */
    protected void gotoLive(CameraInfo info) {
        if(info == null){
            Log.e(Constants.LOG_TAG,"gotoLive():: fail");
            return;
        }
        Intent it = new Intent(this, LiveActivity.class);
        it.putExtra(Constants.IntentKey.CAMERA_ID, info.cameraID);
        TempData.getIns().setCameraInfo(info);
        startActivity(it);
    }

    /**
     * 从控制中心获取下级列表
     * @param info
     */
    private void gotoNextLevelListFromCtrlUnit(ControlUnitInfo info) {
        Intent it = new Intent(this, ResourceListActivity.class);
        it.putExtra(Constants.IntentKey.CONTROL_UNIT_ID, info.controlUnitID);
        startActivity(it);
    }

    /**
     * 从区域获取下级列表
     * @param info
     */
    private void gotoNextLevelListFromRegion(RegionInfo info) {
        Intent it = new Intent(this, ResourceListActivity.class);
        it.putExtra(Constants.IntentKey.REGION_ID, info.regionID);
        startActivity(it);
    }

    @Override
    public void onMsg(int msgId, Object data) {
        Message msg = new Message();
        msg.what = msgId;
        msg.obj = data;
        handler.sendMessage(msg);
    }

}
