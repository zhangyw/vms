package com.nanjing.vms.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.nanjing.vms.utils.DateUtils;
import com.nanjing.vms.utils.ToastUtils;
import com.nanjing.vms.views.ptrloadmore.LoadMoreContainer;
import com.nanjing.vms.views.ptrloadmore.LoadMoreHandler;
import com.nanjing.vms.views.ptrloadmore.LoadMoreListViewContainer;

import java.util.ArrayList;
import java.util.Calendar;
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
public class VehicleLogsSearchListActivity extends BaseTitleActivity implements View.OnClickListener{

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
        setContentView(R.layout.activity_search_vehicle_logs_list);
        initView();
        initListView();
    }

    private void initView() {
        setTitle("车辆出入记录",null,NO_RES_ID);
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);
        etKeyword = (EditText) findViewById(R.id.et_keyword);
        btnSearch = (Button) findViewById(R.id.btn_search);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_date:
                showSelectDateDialog(tvStartDate);
                break;
            case R.id.tv_end_date:
                showSelectDateDialog(tvEndDate);
                break;
            case R.id.btn_search:
                //查询
                loadFirstData();
                break;
        }
    }

    private void loadFirstData(){
        if(TextUtils.isEmpty(getTextNoSpace(tvStartDate))){
            ToastUtils.showToast(getApplicationContext(),"请选择起始日期");
            mPtrFrameLayout.refreshComplete();
            return ;
        }

        if(TextUtils.isEmpty(getTextNoSpace(tvEndDate))){
            ToastUtils.showToast(getApplicationContext(),"请选择截至日期");
            mPtrFrameLayout.refreshComplete();
            return ;
        }
        String startDate = String.valueOf(DateUtils.formatTimeToLong(getTextNoSpace(tvStartDate)));
        String endDate = String.valueOf(DateUtils.formatTimeToLong(getTextNoSpace(tvEndDate)));
        String keyword = getTextNoSpace(etKeyword);
        tvStartDate.setTag(startDate);
        tvEndDate.setTag(endDate);
        etKeyword.setTag(keyword);
        loadData(null);
    }


    private void initListView() {

        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.layout_ptr);
        mPtrFrameLayout.setPullToRefresh(false);
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
                loadData(mVehicles.get(mVehicles.size() - 1).getVehicleLogID());
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
                loadFirstData();
            }
        });

    }

    private void loadData(final String last_id) {

        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        String access_token = member != null ? member.getAccessToken() : "access_token";
        String startDate = (String) tvStartDate.getTag();
        String endDate = (String) tvEndDate.getTag();
        String keyword = (String) etKeyword.getTag();
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


    //选择时间
    public void showSelectDateDialog(final TextView tv) {
        final StringBuffer sb = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int minute = calendar.get(Calendar.MINUTE);
        final int monthOfYear = calendar.get(Calendar.MONTH);
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        }, year, monthOfYear, dayOfMonth);
        final DatePicker datePicker = datePickerDialog.getDatePicker();
        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(VehicleLogsSearchListActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sb.delete(0, sb.length());
                        sb.append(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                        sb.append("\u0020");
                        sb.append(String.format("%02d:%02d", hourOfDay, minute));
                        tv.setText(sb.toString());
                    }
                }, hourOfDay, minute, true);

                timePickerDialog.show();
            }
        });
        datePickerDialog.show();
    }
}
