package com.ptmlb.ca.ahgroup.model.mapper;

import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.model.LoginInfoModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Administrator on 2015/12/10.
 */

@Singleton
public class LoginInfoModelMapper {

    @Inject
    public LoginInfoModelMapper() {

    }

    public LoginInfo transform(LoginInfoModel model) {
        LoginInfo loginInfo = null;
        if (model != null) {
            loginInfo = new LoginInfo();
            loginInfo.setAccount(model.getAccount());
            loginInfo.setPassword(model.getPassword());
            loginInfo.setLastLoginDate(model.getLastLoginDate());
            loginInfo.setAccessToken(model.getAccessToken());
        }
        return loginInfo;
    }

    public LoginInfoModel transform(LoginInfo loginInfo) {
        LoginInfoModel model = null;
        if (loginInfo != null) {
            model = new LoginInfoModel();
            model.setAccount(loginInfo.getAccount());
            model.setPassword(loginInfo.getPassword());
            model.setLastLoginDate(loginInfo.getLastLoginDate());
            model.setAccessToken(loginInfo.getAccessToken());
        }
        return model;
    }
}
