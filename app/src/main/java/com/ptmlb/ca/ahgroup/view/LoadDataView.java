package com.ptmlb.ca.ahgroup.view;

import android.content.Context;

/**
 * Created by Administrator on 2015/12/4.
 *
 */

public interface LoadDataView {
    void showLoading();
    void hideLoading();
    Context getContext();
    void showError(String errorMsg);
}
