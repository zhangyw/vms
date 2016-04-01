package com.nanjing.vms.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nanjing.vms.R;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.network.resp.BankAdvertisements;
import com.nanjing.vms.utils.HmacSHA1;
import com.nanjing.vms.utils.Logger;
import com.nanjing.vms.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class NetworkTestActivity extends BaseActivity {
    private Button btnRx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
        btnRx = (Button) findViewById(R.id.btn_rx);
        btnRx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_bank_ad();
            }
        });
    }


    public void bank_advertisements(View view) {
        showLoadingDialog("正在加载...");
        Call<BankAdvertisements> call = RetrofitClient.vmsApis().bank_advertisements(getParamters());
        call.enqueue(new Callback<BankAdvertisements>() {
            @Override
            public void onResponse(Call<BankAdvertisements> call, Response<BankAdvertisements> response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();
                            }
                        });
                    }
                }).start();
                if (response.isSuccess()) {
                    BankAdvertisements resp = response.body();
                    if (resp != null) {
                        if (resp.isSuccess()) {
                            ToastUtils.showToast(getApplicationContext(), "请求成功");
                        } else {
                            ToastUtils.showToast(getApplicationContext(), " " + resp.getMsg());
                        }
                    } else {
                        ToastUtils.showToast(getApplicationContext(), "未知错误");
                    }
                }
                Logger.d(this, "==============onResponse");
            }

            @Override
            public void onFailure(Call<BankAdvertisements> call, Throwable t) {
                dismissLoadingDialog();
                ToastUtils.showToast(getApplicationContext(), "网络异常");
                Logger.e(this, t);
            }
        });
    }

    private Observable<BankAdvertisements> observable;
    private Subscriber<BankAdvertisements> subscriber;


    public void get_bank_ad() {

        final ProgressDialog dialog = new ProgressDialog(NetworkTestActivity.this);
        dialog.setMessage("正在加载");


        subscriber = new Subscriber<BankAdvertisements>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
                btnRx.setText("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
                btnRx.setText("onError");
            }

            @Override
            public void onNext(BankAdvertisements bankAdvertisements) {
                if (bankAdvertisements.isSuccess()) {
                    ToastUtils.showToast(getApplicationContext(), "请求成功");
                }
            }
        };


        observable = RetrofitClient.vmsApis().bank_advertisement(getParamters());
        observable
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        btnRx.setText("doOnSubscribe");
                        dialog.show();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }

    public Map<String, String> getParamters() {
        HashMap<String, String> map = new HashMap<>();
        map.put("timestamp", getTimeStamp());
        map.put("signature", HmacSHA1.getSignature(map));
        return map;
    }

    public static String getTimeStamp() {
        String timestamp = (System.currentTimeMillis() / 1000) + "";
//        String timestamp = "1449908478";
        return timestamp;
    }
}
