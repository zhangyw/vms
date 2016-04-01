package com.nanjing.vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nanjing.vms.R;
import com.nanjing.vms.Constants;
import com.nanjing.vms.adapter.VehicleAdapter;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.model.Member;
import com.nanjing.vms.model.Vehicle;
import com.nanjing.vms.network.BaseRespCallback;
import com.nanjing.vms.network.RequestParameters;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.network.resp.ListVehiclesResp;
import com.nanjing.vms.utils.ToastUtils;
import com.nanjing.vms.views.ptrloadmore.LoadMoreContainer;
import com.nanjing.vms.views.ptrloadmore.LoadMoreHandler;
import com.nanjing.vms.views.ptrloadmore.LoadMoreListViewContainer;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import retrofit2.Call;

/**
 * 车辆列表
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class VehicleListActivity extends BaseTitleActivity {

    private ListView lvVehicle;
    private VehicleAdapter mVehicleAdapter;
    private List<Vehicle> mVehicles = new ArrayList<>();
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer loadMoreListViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);
        initView();
        autoLoadData();
    }

    private void autoLoadData() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(true);
            }
        }, 100);
    }

    private void initView() {
        setTitle("车辆列表", "添加", NO_RES_ID);
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
                if(Member.hasPermission(member.getmMemberType(), Constants.MemberPermission.VEHICLE_CREATE)){
                    Intent intent = new Intent(VehicleListActivity.this, VehicleEditActivity.class);
                    startActivity(intent);
                }else {
                    ToastUtils.showToast(getApplicationContext(),getString(R.string.no_permission_tip));
                }

            }
        });

        lvVehicle = (ListView) findViewById(R.id.lv_vehicle);
        View headerMarginView = new View(VehicleListActivity.this);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40));
        lvVehicle.addHeaderView(headerMarginView);
        lvVehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vehicle vehicle = (Vehicle) parent.getItemAtPosition(position);
                if (vehicle != null) {
                    Intent intent = new Intent(VehicleListActivity.this, VehicleDetailsActivity.class);
                    intent.putExtra("vehicle", vehicle);
                    startActivity(intent);
                }
            }
        });
        mVehicleAdapter = new VehicleAdapter(this, mVehicles);
        lvVehicle.setAdapter(mVehicleAdapter);


        findViewById(R.id.layout_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleListActivity.this, VehicleSearchListActivity.class);
                startActivity(intent);
            }
        });


        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadData(mVehicles.get(mVehicles.size() - 1).getVehicleID(), null);
            }
        });

        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.layout_ptr);
        MaterialHeader materialHeader = new MaterialHeader(this);
        mPtrFrameLayout.setHeaderView(materialHeader);
        mPtrFrameLayout.addPtrUIHandler(materialHeader);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvVehicle, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData(null, null);
            }
        });


    }


    private Call mCall;

    private void loadData(final String vehicle_id, String keyword) {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        mCall = RetrofitClient.vmsApis().list_vehicles(RequestParameters
                .list_vehicles(member.getAccessToken(), vehicle_id, keyword));
        mCall.enqueue(new BaseRespCallback<ListVehiclesResp>(getApplicationContext()) {
            @Override
            public void onSuccess(ListVehiclesResp baseResp) {
                if (vehicle_id == null) {
                    mVehicles.clear();
                }
                List<Vehicle> temp_vehicles = baseResp.getData().getVehicles();
                mVehicles.addAll(temp_vehicles);
                mVehicleAdapter.notifyDataSetChanged();
                loadMoreListViewContainer.loadMoreFinish(mVehicles.size() != 0, mVehicles.size() != 0 && mVehicles.size() % Constants.PAGE_SIZE_DEFAULT == 0);

            }

            @Override
            public void onAfter() {
                mPtrFrameLayout.refreshComplete();
                //TODO 删除测试数据
                for (int i = 0; i < 20; i++) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicleID("" + i);
                    vehicle.setVehiclePlateLicense("苏A 8888" + i);
                    vehicle.setVehicleStatus(i % 4);
                    vehicle.setVehicleTrafficType(i % 9);
                    vehicle.setVehicleExpireDate("1453468397");
                    mVehicles.add(vehicle);
                }
                loadMoreListViewContainer.loadMoreFinish(mVehicleAdapter.isEmpty(), mVehicleAdapter.hasMore());
                mVehicleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {
                loadMoreListViewContainer.loadMoreError(0, "加载更多失败");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCall != null && mCall.isExecuted()) {
            mCall.cancel();
        }
    }
}
