package com.ptmlb.ca.ahgroup.presenter;

import com.mobandme.android.transformer.Transformer;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.interactor.SaveLoginInfoInteractor;
import com.ptmlb.ca.ahgroup.model.LoginInfoModel;
import com.ptmlb.ca.ahgroup.view.SaveLoginInfoView;

import rx.Subscriber;

/**
 * Created by Administrator on 2015/12/8.
 */
public class SaveLoginInfoPresenter implements Presenter {

    private SaveLoginInfoView view;
    private SaveLoginInfoInteractor interactor;
    private static final Transformer transformer = new Transformer.Builder().build(LoginInfoModel.class);

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

    public void saveLoginInfo() {
        this.view.showLoading();
        this.interactor.execute(new Subscriber<LoginInfo>() {

            @Override
            public void onCompleted() {
                SaveLoginInfoPresenter.this.view.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                SaveLoginInfoPresenter.this.view.hideLoading();
            }

            @Override
            public void onNext(LoginInfo loginInfo) {
                LoginInfoModel model = SaveLoginInfoPresenter.this.transformer.transform(loginInfo, LoginInfoModel.class);
                SaveLoginInfoPresenter.this.view.update(model);
            }
        });
    }
}
