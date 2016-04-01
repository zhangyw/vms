package com.nanjing.vms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.nanjing.vms.Constants;
import com.nanjing.vms.R;
import com.nanjing.vms.adapter.EmpAdapter;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.model.Emp;
import com.nanjing.vms.model.Member;
import com.nanjing.vms.network.BaseRespCallback;
import com.nanjing.vms.network.RequestParameters;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.network.resp.ListEmpsResp;
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
 * Created by Jacky on 2016/2/15.
 * Version 1.0
 */
public class EmpSearchListActivity extends BaseActivity{

    private EditText etKeyword;
    private ListView listView;
    private PtrFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer loadMoreListViewContainer;

    private List<Emp> mEmps = new ArrayList<>();
    private EmpAdapter mAdapter;
    private Call mCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_search_list);
        initView();
        initListView();
        autoLoadData();
    }

    private void initView() {
        findViewById(R.id.title_leftimageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmpSearchListActivity.this.onBackPressed();
            }
        });

        findViewById(R.id.title_rightimageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        etKeyword = (EditText) findViewById(R.id.et_keyword);
    }

    private void search(){
        String keyword = getTextNoSpace(etKeyword);
        if(TextUtils.isEmpty(keyword)) {
            ToastUtils.showToast(this, "关键字不能为空");
            return ;
        }else {
            //按关键字查询
            etKeyword.setTag(keyword);
            loadData(null, keyword);

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Emp emp = (Emp) parent.getItemAtPosition(position);
                if (emp != null) {
                    Intent intent = new Intent(EmpSearchListActivity.this, EmpDetailsActivity.class);
                    intent.putExtra("emp", emp);
                    startActivity(intent);
                }
            }
        });
        //设置listView adapter
        mAdapter = new EmpAdapter(this, mEmps);
        listView.setAdapter(mAdapter);


        //设置footerView
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                String key = (String) etKeyword.getTag();
                loadData(mEmps.get(mEmps.size() - 1).getEmpID(),key);
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
                loadData(null,(String)etKeyword.getTag());
            }
        });

    }


    private void autoLoadData(){
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(false);
            }
        }, 100);
    }


    private void loadData(final String last_id,String keyword) {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
        Member member = (Member) Share.getObject(Share.KEY_MEMBER_INFO);
        String access_token = member != null?member.getAccessToken():"access_token";
        mCall = RetrofitClient.vmsApis().list_emps(RequestParameters.list_emps(access_token, Constants.PAGE_SIZE_DEFAULT,last_id,keyword));
        mCall.enqueue(new BaseRespCallback<ListEmpsResp>(getApplicationContext()) {
            @Override
            public void onSuccess(ListEmpsResp baseResp) {
                if (last_id == null) {
                    mEmps.clear();
                }
                List<Emp> temp_vehicles = baseResp.getData().getEmps();
                mEmps.addAll(temp_vehicles);
                mAdapter.notifyDataSetChanged();
                loadMoreListViewContainer.loadMoreFinish(mAdapter.isEmpty(), false);
            }

            @Override
            public void onAfter() {
                mPtrFrameLayout.refreshComplete();
//                for (int i = 0; i < 20; i++) {
//                    mEmps.add(new Emp());
//                }
//                mAdapter.notifyDataSetChanged();
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
