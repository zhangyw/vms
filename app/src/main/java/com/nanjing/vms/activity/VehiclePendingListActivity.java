package com.nanjing.vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nanjing.vms.Constants;
import com.nanjing.vms.R;
import com.nanjing.vms.adapter.VehicleAdapter;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.model.Member;
import com.nanjing.vms.model.Vehicle;
import com.nanjing.vms.network.BaseRespCallback;
import com.nanjing.vms.network.RequestParameters;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.network.resp.ListVehiclesResp;
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
 * 车辆待审批列表
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class VehiclePendingListActivity extends BaseTitleActivity{

    private ListView listView;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer loadMoreListViewContainer;

    private List<Vehicle> mVehicles = new ArrayList<>();
    private VehicleAdapter mAdapter;
    private Call mCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_pending_list);
        initView();
        initListView();
        autoLoadData();
    }


    private void initView() {
        setTitle("车辆待审批列表", null, NO_RES_ID);
    }


    private void initListView() {

        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.layout_ptr);
        listView = (ListView) findViewById(R.id.listView);
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);

        //设置头部
        View headerMarginView = new View(this);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.list_margin_header_height)));
        listView.addHeaderView(headerMarginView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vehicle item = (Vehicle) parent.getItemAtPosition(position);
                if (item != null) {
                    Intent intent = new Intent(VehiclePendingListActivity.this, VehicleDetailsActivity.class);
                    intent.putExtra("vehicle", item);
                    startActivity(intent);
                }
            }
        });
        //设置listView adapter
        mAdapter = new VehicleAdapter(this, mVehicles);
        listView.setAdapter(mAdapter);


        //设置footerView
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadData(mVehicles.get(mVehicles.size() - 1).getVehicleID(),null);
            }
        });


        MaterialHeader materialHeader = new MaterialHeader(this);
        mPtrFrameLayout.setHeaderView(materialHeader);
        mPtrFrameLayout.addPtrUIHandler(materialHeader);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData(null, null);
            }
        });

    }

    private void loadData(final String last_id, String keyword) {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        String access_token = member != null?member.getAccessToken():"access_token";
        mCall = RetrofitClient.vmsApis().list_vehicles(RequestParameters
                .list_emps(access_token, Constants.PAGE_SIZE_DEFAULT, last_id, keyword));
        mCall.enqueue(new BaseRespCallback<ListVehiclesResp>(getApplicationContext()) {
            @Override
            public void onSuccess(ListVehiclesResp baseResp) {
                if (last_id == null) {
                    mVehicles.clear();
                }
                List<Vehicle> temp_vehicles = baseResp.getData().getVehicles();
                mVehicles.addAll(temp_vehicles);
                mAdapter.notifyDataSetChanged();
                loadMoreListViewContainer.loadMoreFinish(mAdapter.isEmpty(), mAdapter.hasMore());
            }

            @Override
            public void onAfter() {
                mPtrFrameLayout.refreshComplete();
            }

            @Override
            public void onError(Throwable t) {
                loadMoreListViewContainer.loadMoreError(0, "加载更多失败");
            }
        });
    }

    private void autoLoadData() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(true);
            }
        }, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCall != null && mCall.isExecuted()) {
            mCall.cancel();
        }
    }
}
