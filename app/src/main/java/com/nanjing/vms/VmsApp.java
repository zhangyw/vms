package com.nanjing.vms;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.nanjing.vms.cache.Share;
import com.nanjing.vms.network.RetrofitClient;
import com.nanjing.vms.utils.StringUtils;

import java.io.File;

/**
 * Created by Jacky on 2016/2/21.
 * Version 1.0
 */
public class VmsApp extends Application{
    public static final String VMS_CACHE_NAME = "VMS_CACHE";

    private static VmsApp ins;
    @Override
    public void onCreate() {
        super.onCreate();
        ins = this;
        MCRSDK.init();
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
        initCache();
        initBaseUrl();
    }

    public static VmsApp getIns(){
        return ins;
    }


    private void initBaseUrl(){
        String ip = Share.getString(Share.KEY_ITS_ADDRESS,getResources().getString(R.string.its_address));
        String port = Share.getString(Share.KEY_ITS_PORT,getResources().getString(R.string.its_port));
        RetrofitClient.sBaseUrl = StringUtils.getBaseUrl(ip, port);
    }


    private void initCache(){
        File file = getCacheDir(this,VMS_CACHE_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        Share.init("CACHE", 10 * 1024, file.toString());
    }


    // Creates a unique subdirectory of the designated app cache directory. Tries to use external but if not mounted, falls back on internal storage.
    public static File getCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir otherwise use internal cache dir
        final String cachePath = (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
                !Environment.isExternalStorageRemovable()) ?
                context.getExternalCacheDir().getPath() :
                context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }









}
