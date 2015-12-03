package com.ptmlb.ca.ahgroup.data.repository;

import com.ptmlb.ca.ahgroup.data.repository.datasource.LoginInfoPersistenceDataSource;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.repository.LoginInfoRepository;

import java.util.List;

/**
 * Created by Administrator on 2015/12/3.
 */
public class LoginInfoRepositoryImpl implements LoginInfoRepository {

    LoginInfoPersistenceDataSource persistenceDataSource;

    public LoginInfoRepositoryImpl(LoginInfoPersistenceDataSource persistenceDataSource) {
        this.persistenceDataSource = persistenceDataSource;
    }

    @Override
    public List<LoginInfo> getLoginInfoList() {
        //return persistenceDataSource.getLoginInfoList();
        return null;
    }

    @Override
    public int save(LoginInfo loginInfo) {
        //return persistenceDataSource.save(loginInfo);
        return 0;
    }
}
