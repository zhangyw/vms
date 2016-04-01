package com.nanjing.vms.network;

import android.content.Context;

import com.nanjing.vms.R;
import com.nanjing.vms.network.resp.BaseResp;
import com.nanjing.vms.utils.Logger;
import com.nanjing.vms.utils.ToastUtils;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * TODO
 * Created by Jacky on 2016/3/8.
 * Version 1.0
 */
public abstract  class BaseRespCallback <T extends BaseResp> implements Callback<T>{

    private Context mContext;

    public  BaseRespCallback (Context context){
        this.mContext =context;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onAfter();
        if(response.isSuccess()) {
            if(response.body().isSuccess()) {
                onSuccess(response.body());
            }else {
                ToastUtils.showToast(mContext,response.body().getMsg());
            }
        }else {
            ToastUtils.showToast(mContext,"服务器相应失败");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(t);
        onAfter();
        if(mContext!=null) {
            if(t instanceof SocketTimeoutException) {
                ToastUtils.showToast(mContext,"请求超时");
            }else if(t instanceof TimeoutException){
                ToastUtils.showToast(mContext,"网络异常");
            }else if(t instanceof ConnectException){
                ToastUtils.showToast(mContext,"网络异常，请检查网络");
            }else if(t instanceof SocketException){
                Logger.d(this,"请求取消");
            }else {
                ToastUtils.showToast(mContext,mContext.getString(R.string.msg_net_error));
            }

        }
    }

    public abstract void onSuccess(T t);
    public abstract void onAfter();
    public abstract  void onError(Throwable t);
}
