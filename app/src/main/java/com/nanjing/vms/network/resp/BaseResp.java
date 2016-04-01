package com.nanjing.vms.network.resp;

import com.google.gson.annotations.Expose;

/**
 * Created by Jacky on 2015/8/3.
 */
public class BaseResp {
    private static final int CODE_SUCCESS = 200;

    @Expose
    private int status_code;
    @Expose
    private String msg;

    public int getStatusCode() {
        return status_code;
    }

    public void setStatusCode(int statusCode) {
        this.status_code = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return status_code == 200;
    }

}
