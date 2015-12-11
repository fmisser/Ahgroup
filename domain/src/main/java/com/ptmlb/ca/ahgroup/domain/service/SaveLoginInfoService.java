package com.ptmlb.ca.ahgroup.domain.service;

import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.exception.SaveDataException;
import com.ptmlb.ca.ahgroup.domain.repository.LoginInfoRepository;

/**
 * Created by Administrator on 2015/12/10.
 */

public class SaveLoginInfoService {

    private LoginInfoRepository repository;

    public SaveLoginInfoService(LoginInfoRepository repository) {
        this.repository = repository;
    }

    public int save(LoginInfo loginInfo) throws SaveDataException {
        return repository.save(loginInfo);
    }
}
