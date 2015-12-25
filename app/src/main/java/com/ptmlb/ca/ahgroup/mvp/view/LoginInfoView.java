package com.ptmlb.ca.ahgroup.mvp.view;

import com.ptmlb.ca.ahgroup.mvp.model.LoginInfoModel;

import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */

public interface LoginInfoView extends LoadDataView {
    void update(List<LoginInfoModel> models);
}
