package com.nanjing.vms.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nanjing.vms.Constants;
import com.nanjing.vms.R;
import com.nanjing.vms.adapter.VehicleLogAdapter;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.model.Member;
import com.nanjing.vms.model.VehicleLog;
import com.nanjing.vms.network.BaseRespCallback;
import com.nanjing.vms.network.RequestParameters;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.network.resp.ListVehicleLogsResp;
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
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class EmpLogSearchListActivity extends BaseTitleActivity implements View.OnClickListener{

    private TextView tvStartDate;
    private TextView tvEndDate;
    private EditText etKeyword;
    private Button btnSearch;


    private ListView listView;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer loadMoreListViewContainer;

    private List<VehicleLog> mVehicles = new ArrayList<>();
    private VehicleLogAdapter mAdapter;
    private Call mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_emp_logs_list);
        initView();
        initListView();
    }

    private void initView() {
        setTitle("人员出入记录",null,NO_RES_ID);
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);
        etKeyword = (EditText) findViewById(R.id.et_keyword);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_date:
                break;
            case R.id.tv_end_date:
                break;
            case R.id.btn_search:
                //查询
                String keyword = etKeyword.getText().toString().trim();
                loadData(null,keyword);
                break;
        }
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
        //设置listView adapter
        mAdapter = new VehicleLogAdapter(this, mVehicles);
        listView.setAdapter(mAdapter);


        //设置footerView
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {

                loadData(mVehicles.get(mVehicles.size() - 1).getVehicleLogID(), null);
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
        String access_token = member != null ? member.getAccessToken() : "access_token";
        String startDate = "";
        String endDate = "";
        mCall = RetrofitClient.vmsApis().list_vehicle_logs(RequestParameters
                .list_vehicle_logs(access_token, startDate, endDate, Constants.PAGE_SIZE_DEFAULT, last_id, keyword));
        mCall.enqueue(new BaseRespCallback<ListVehicleLogsResp>(getApplicationContext()) {
            @Override
            public void onSuccess(ListVehicleLogsResp baseResp) {
                if (last_id == null) {
                    mVehicles.clear();
                }
                List<VehicleLog> temp_vehicles = baseResp.getData().getVehicles();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCall != null && mCall.isExecuted()) {
            mCall.cancel();
        }
    }
}
