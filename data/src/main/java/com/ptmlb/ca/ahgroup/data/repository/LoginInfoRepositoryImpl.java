package com.ptmlb.ca.ahgroup.data.repository;

import com.ptmlb.ca.ahgroup.data.exception.GetPersistenceDataException;
import com.ptmlb.ca.ahgroup.data.exception.InvalidPersistenceDataException;
import com.ptmlb.ca.ahgroup.data.exception.PersistDataException;
import com.ptmlb.ca.ahgroup.data.exception.UnknownPersistenceException;
import com.ptmlb.ca.ahgroup.data.repository.datasource.LoginInfoPersistenceDataSource;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.exception.GetDataException;
import com.ptmlb.ca.ahgroup.domain.exception.SaveDataException;
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
    public List<LoginInfo> getLoginInfoList() throws GetDataException {
        try {
            return persistenceDataSource.getLoginInfoList();
        } catch (GetPersistenceDataException | InvalidPersistenceDataException | UnknownPersistenceException e) {
            throw new GetDataException();
        }
    }

    @Override
    public int save(LoginInfo loginInfo) throws SaveDataException {
        try {
            return persistenceDataSource.save(loginInfo);
        } catch (PersistDataException | UnknownPersistenceException e) {
            throw new SaveDataException();
        }
    }
}
