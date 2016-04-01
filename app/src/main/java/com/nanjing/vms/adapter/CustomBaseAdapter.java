package com.nanjing.vms.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.nanjing.vms.Constants;

import java.util.List;

/**
 * Created by Jacky on 2016/3/12.
 * Version 1.0
 */
public abstract class CustomBaseAdapter <T> extends BaseAdapter{

    private int mPageSize = Constants.PAGE_SIZE_DEFAULT;
    private Context mContext;
    private List<T> mList;

    public CustomBaseAdapter(Context context,List<T> list){
        this.mContext = context;
        this.mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public boolean isEmpty(){
        return mList == null || mList.size()== 0;
    }

    public boolean hasMore (){
        return mList!=null && mList.size()!=0 && mList.size()%mPageSize == 0;
    }

    public void setDefaultPageSize(int size){
        this.mPageSize = size;
    }

    public Context getContext(){
        return mContext;
    }

    public List<T> getList(){
        return  mList;
    }

}
