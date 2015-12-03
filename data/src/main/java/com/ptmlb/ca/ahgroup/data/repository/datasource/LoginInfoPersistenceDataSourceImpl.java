package com.ptmlb.ca.ahgroup.data.repository.datasource;

import com.j256.ormlite.dao.Dao;
import com.ptmlb.ca.ahgroup.data.cache.Cache;
import com.ptmlb.ca.ahgroup.data.cache.list.ListCache;
import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

public class LoginInfoPersistenceDataSourceImpl implements LoginInfoPersistenceDataSource {

    private Dao<LoginInfoEntity, String> dao;
    private Cache<LoginInfoEntity> cache;
    private ListCache<LoginInfoEntity> listCache;

    public LoginInfoPersistenceDataSourceImpl(Dao<LoginInfoEntity, String> dao, Cache<LoginInfoEntity> cache, ListCache<LoginInfoEntity> listCache) {

    }

    @Override
    public List<LoginInfo> getLoginInfoList() {
        return null;
    }

    @Override
    public int save(LoginInfo loginInfo) {
        return 0;
    }
}
