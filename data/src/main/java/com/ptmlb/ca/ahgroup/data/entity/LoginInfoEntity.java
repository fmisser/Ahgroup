package com.ptmlb.ca.ahgroup.data.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.ptmlb.ca.ahgroup.data.cache.ttl.TtlCacheObject;

import java.util.Date;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

@DatabaseTable(tableName = "tb_login_info")
public class LoginInfoEntity implements TtlCacheObject {

    @DatabaseField(id = true)
    public String account;

    @DatabaseField
    public String password;

    @DatabaseField
    public String accessToken;

    @DatabaseField
    public Date lastLoginDate;

    @DatabaseField
    public long persistedTime;

    LoginInfoEntity() {}

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

    @Override
    public long getPersistedTime() {
        return persistedTime;
    }

    public void setPersistedTime(long persistedTime) {
        this.persistedTime = persistedTime;
    }
}
