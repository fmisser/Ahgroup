package com.ptmlb.ca.ahgroup.domain.entity;

import java.util.Date;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

public class LoginInfo {
    public String account;
    public String password;
    public String accessToken;
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
