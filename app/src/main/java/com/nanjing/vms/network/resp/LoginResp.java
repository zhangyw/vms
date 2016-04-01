package com.nanjing.vms.network.resp;

import com.nanjing.vms.model.Member;

/**
 * Created by Jacky on 2016/3/7.
 * Version 1.0
 */
public class LoginResp extends BaseResp{

    private Member data;

    public Member getData() {
        return data;
    }

    public void setData(Member data) {
        this.data = data;
    }
}
