package com.ptmlb.ca.ahgroup.data.entity.mapper;

import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;

import java.util.ArrayList;
import java.util.List;

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

    public List<LoginInfo> transformList(List<LoginInfoEntity> entities) {
        List<LoginInfo> loginInfoList = new ArrayList<>();

        LoginInfo loginInfo;
        for (LoginInfoEntity entity : entities) {
            loginInfo = transform(entity);
            if (loginInfo != null) {
                loginInfoList.add(loginInfo);
            }
        }

        return loginInfoList;
    }

    public LoginInfoEntity transformToEntity(LoginInfo loginInfo) {
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

    public List<LoginInfoEntity> transformToEntityList(List<LoginInfo> loginInfoList) {
        List<LoginInfoEntity> entities = new ArrayList<>();
        LoginInfoEntity entity;
        for (LoginInfo loginInfo : loginInfoList) {
            entity = transformToEntity(loginInfo);
            if (entity != null) {
                entities.add(entity);
            }
        }

        return entities;
    }
}
