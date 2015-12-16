package com.ptmlb.ca.ahgroup.data.network;

import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Administrator on 2015/12/16.
 *
 */

public interface RestApi {

    String OTHER_HELPER_HANDLER = "OtherHelperHandler.ashx";

    @POST(OTHER_HELPER_HANDLER)
    Call<LoginInfoEntity> getLoginInfo(@Body RequestBody body);
}
