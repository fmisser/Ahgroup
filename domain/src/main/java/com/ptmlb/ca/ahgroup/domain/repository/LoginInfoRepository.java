package com.ptmlb.ca.ahgroup.domain.repository;

import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.exception.GetDataException;
import com.ptmlb.ca.ahgroup.domain.exception.SaveDataException;

import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

public interface LoginInfoRepository {
    public List<LoginInfo> getLoginInfoList() throws GetDataException;
    public int save(LoginInfo loginInfo) throws SaveDataException;
}
