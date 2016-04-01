package com.nanjing.vms.network;

import com.nanjing.vms.cache.Share;
import com.nanjing.vms.utils.Logger;
import com.nanjing.vms.utils.StringUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jacky on 2016/3/7.
 * Version 1.0
 */
public class RetrofitClient {


    public static String sBaseUrl = "";

    private static final String API_VERSION = "api/v1/";


    private static VmsApis vmsApis;
    private static OkHttpClient okHttpClient;

    public static VmsApis vmsApis() {
        if (vmsApis == null) {
            sBaseUrl += API_VERSION;
            Retrofit retrofit = new Retrofit.Builder().baseUrl(sBaseUrl)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            vmsApis = retrofit.create(VmsApis.class);
        } else {

        }
        return vmsApis;
    }

    private synchronized static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().sslSocketFactory(getSSLSocketFactory()).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    String s = response.body().string();
                    Logger.d(this, "============= " + s);
                    return response.newBuilder()
                            .body(ResponseBody.create(response.body().contentType(), s))
                            .build();
                }
            }).connectTimeout(2000, TimeUnit.MILLISECONDS).build();
        }
        return okHttpClient;
    }


    public static void reset() {
        sBaseUrl = StringUtils.getBaseUrl(Share.getString(Share.KEY_ITS_ADDRESS), Share.getString(Share.KEY_ITS_PORT));
        sBaseUrl += API_VERSION;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(sBaseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        vmsApis = retrofit.create(VmsApis.class);
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
            return sc.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            Logger.e("VmsApp", e);
        } catch (KeyManagementException e) {
            Logger.e("VmsApp", e);
        }

        return null;
    }


}
