package com.ptmlb.ca.ahgroup.mvp.view;

import com.ptmlb.ca.ahgroup.mvp.model.LoginInfoModel;

/**
 * Created by Administrator on 2015/12/8.
 */
public interface SaveLoginInfoView extends LoadDataView {
    void update(LoginInfoModel loginInfo);
}
