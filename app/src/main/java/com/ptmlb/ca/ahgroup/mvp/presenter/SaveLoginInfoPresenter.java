package com.ptmlb.ca.ahgroup.mvp.presenter;

import com.ptmlb.ca.ahgroup.di.scope.ActivityScope;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.interactor.SaveLoginInfoInteractor;
import com.ptmlb.ca.ahgroup.mvp.model.LoginInfoModel;
import com.ptmlb.ca.ahgroup.mvp.model.mapper.LoginInfoModelMapper;
import com.ptmlb.ca.ahgroup.mvp.view.SaveLoginInfoView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Administrator on 2015/12/8.
 */

@ActivityScope
public class SaveLoginInfoPresenter implements Presenter {

    private SaveLoginInfoView view;
    private SaveLoginInfoInteractor interactor;
    private LoginInfoModelMapper mapper;

    @Inject
    public SaveLoginInfoPresenter(SaveLoginInfoInteractor interactor, LoginInfoModelMapper mapper) {
        this.interactor = interactor;
        this.mapper = mapper;
    }

    public void setView(SaveLoginInfoView view) {
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

    public void saveLoginInfo(LoginInfoModel model) {
        this.view.showLoading();
        this.interactor.setLoginInfo(mapper.transform(model));
        this.interactor.execute(new Subscriber<LoginInfo>() {

            @Override
            public void onCompleted() {
                SaveLoginInfoPresenter.this.view.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                SaveLoginInfoPresenter.this.view.hideLoading();
                SaveLoginInfoPresenter.this.view.showError(e.getLocalizedMessage());
            }

            @Override
            public void onNext(LoginInfo loginInfo) {
                LoginInfoModel model = SaveLoginInfoPresenter.this.mapper.transform(loginInfo);
                SaveLoginInfoPresenter.this.view.update(model);
            }
        });
    }
}
