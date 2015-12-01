package com.ptmlb.ca.ahgroup.repository;

import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.repository.LoginInfoRepository;
import com.ptmlb.ca.ahgroup.repository.datasource.LoginInfoPersistenceDataSource;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

public class LoginInfoRepositoryImpl implements LoginInfoRepository {

    LoginInfoPersistenceDataSource persistenceDataSource;

    public LoginInfoRepositoryImpl(LoginInfoPersistenceDataSource dataSource) {
        this.persistenceDataSource = dataSource;
    }

    @Override
    public List<LoginInfo> getLoginInfoList() {
        return persistenceDataSource.getLoginInfoList();
    }

    @Override
    public int save(LoginInfo loginInfo) {
        return persistenceDataSource.save(loginInfo);
    }
}
