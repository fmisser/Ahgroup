package com.ptmlb.ca.ahgroup.repository.datasource;

import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

public interface LoginInfoPersistenceDataSource {
    public List<LoginInfo> getLoginInfoList();
    public int save(LoginInfo loginInfo);
}
