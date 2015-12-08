package com.ptmlb.ca.ahgroup.model;

import com.mobandme.android.transformer.compiler.Mappable;
import com.mobandme.android.transformer.compiler.Mapped;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;

import java.util.Date;

/**
 * Created by Administrator on 2015/12/4.
 */

@Mappable(with = LoginInfo.class)
public class LoginInfoModel {

    @Mapped
    public String account;

    @Mapped
    public String password;

    @Mapped
    public String accessToken;

    @Mapped
    public Date lastLoginDate;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
