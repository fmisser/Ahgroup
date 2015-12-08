package com.ptmlb.ca.ahgroup.view;

import com.ptmlb.ca.ahgroup.model.LoginInfoModel;

import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */

public interface LoginInfoView extends LoadDataView {
    void update(List<LoginInfoModel> models);
}
