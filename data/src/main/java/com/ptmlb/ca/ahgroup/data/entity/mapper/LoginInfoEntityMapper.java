package com.ptmlb.ca.ahgroup.data.entity.mapper;

import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Administrator on 2015/12/10.
 */

@Singleton
public class LoginInfoEntityMapper {

    @Inject
    public LoginInfoEntityMapper() {

    }

    public LoginInfo transform(LoginInfoEntity entity) {
        LoginInfo loginInfo = null;
        if (entity != null) {
            loginInfo = new LoginInfo();
            loginInfo.setAccount(entity.getAccount());
            loginInfo.setPassword(entity.getPassword());
            loginInfo.setLastLoginDate(entity.getLastLoginDate());
            loginInfo.setAccessToken(entity.getAccessToken());
        }
        return loginInfo;
    }

    public LoginInfoEntity transform(LoginInfo loginInfo) {
        LoginInfoEntity entity = null;
        if (loginInfo != null) {
            entity = new LoginInfoEntity();
            entity.setAccount(loginInfo.getAccount());
            entity.setPassword(loginInfo.getPassword());
            entity.setLastLoginDate(loginInfo.getLastLoginDate());
            entity.setAccessToken(loginInfo.getAccessToken());
        }
        return entity;
    }
}
