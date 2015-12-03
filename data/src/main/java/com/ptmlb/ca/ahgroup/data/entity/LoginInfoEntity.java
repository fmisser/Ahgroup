package com.ptmlb.ca.ahgroup.data.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mobandme.android.transformer.compiler.Mappable;
import com.mobandme.android.transformer.compiler.Mapped;
import com.ptmlb.ca.ahgroup.data.cache.ttl.TtlCacheObject;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;

import java.util.Date;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

@Mappable(with = LoginInfo.class)
@DatabaseTable(tableName = "tb_login_info")
public class LoginInfoEntity implements TtlCacheObject {

    @Mapped
    @DatabaseField(id = true)
    public String account;

    @Mapped
    @DatabaseField
    public String password;

    @Mapped
    @DatabaseField
    public String accessToken;

    @Mapped
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
