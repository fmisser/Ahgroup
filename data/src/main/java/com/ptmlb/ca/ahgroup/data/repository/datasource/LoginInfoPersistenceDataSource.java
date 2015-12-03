package com.ptmlb.ca.ahgroup.data.repository.datasource;

import com.ptmlb.ca.ahgroup.data.exception.DeletePersistenceDataException;
import com.ptmlb.ca.ahgroup.data.exception.GetPersistenceDataException;
import com.ptmlb.ca.ahgroup.data.exception.InvalidPersistenceDataException;
import com.ptmlb.ca.ahgroup.data.exception.PersistDataException;
import com.ptmlb.ca.ahgroup.data.exception.UnknownPersistenceException;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/12/3.
 *
 */

public interface LoginInfoPersistenceDataSource {
    public List<LoginInfo> getLoginInfoList()
            throws GetPersistenceDataException, InvalidPersistenceDataException, UnknownPersistenceException;

    public int save(LoginInfo loginInfo)
            throws PersistDataException, UnknownPersistenceException;

    public int delete(List<LoginInfo> loginInfoList)
            throws DeletePersistenceDataException, UnknownPersistenceException;
}
