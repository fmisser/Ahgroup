package com.ptmlb.ca.ahgroup.presenter;

import com.ptmlb.ca.ahgroup.di.scope.ActivityScope;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.interactor.Interactor;
import com.ptmlb.ca.ahgroup.model.LoginInfoModel;
import com.ptmlb.ca.ahgroup.model.mapper.LoginInfoModelMapper;
import com.ptmlb.ca.ahgroup.view.LoginInfoView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Administrator on 2015/12/8.
 */

public class LoginInfoPresenter implements Presenter {

    private LoginInfoView view;
    private Interactor interactor;
    private LoginInfoModelMapper mapper;

    public LoginInfoPresenter(Interactor interactor, LoginInfoModelMapper mapper) {
        this.interactor = interactor;
    }

    public void setView(LoginInfoView view) {
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        interactor.unsubscribe();
    }

    public void loadLoginInfoList() {
        this.view.showLoading();
        this.interactor.execute(new Subscriber<List<LoginInfo>>() {
            @Override
            public void onCompleted() {
                LoginInfoPresenter.this.view.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                LoginInfoPresenter.this.view.hideLoading();
                LoginInfoPresenter.this.view.showError(e.getLocalizedMessage());
            }

            @Override
            public void onNext(List<LoginInfo> o) {
                List<LoginInfoModel> models = new ArrayList<LoginInfoModel>();
                for (LoginInfo loginInfo : o) {
                    models.add(LoginInfoPresenter.this.mapper.transform(loginInfo));
                }
                LoginInfoPresenter.this.view.update(models);
            }
        });
    }

}
