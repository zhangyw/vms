package com.nanjing.vms.utils;

import android.text.TextUtils;

/**
 * Created by Jacky on 2016/3/8.
 * Version 1.0
 */
public class StringUtils {


    public static String getBaseUrl(String ip, String port) {
        return "http://" + ip + ":" + port + "/";
    }


    public static boolean isIpCorrect(String ip) {
        return ip.matches("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)");
    }


    public static boolean isPortCorrect(String port) {
        if (TextUtils.isDigitsOnly(port)) {
            int port_int = Integer.parseInt(port);
            return port_int >= 1 && port_int <= 65535 ? true : false;
        }
        return false;
    }
}
