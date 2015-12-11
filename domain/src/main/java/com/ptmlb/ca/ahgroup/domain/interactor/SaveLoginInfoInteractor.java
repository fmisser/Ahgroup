package com.ptmlb.ca.ahgroup.domain.interactor;

import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.exception.SaveDataException;
import com.ptmlb.ca.ahgroup.domain.executor.JobExecutor;
import com.ptmlb.ca.ahgroup.domain.executor.PostExecutionThread;
import com.ptmlb.ca.ahgroup.domain.repository.LoginInfoRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2015/12/8.
 */

public class SaveLoginInfoInteractor extends Interactor {

    private LoginInfoRepository repository;
    private LoginInfo loginInfo;

    @Inject
    public SaveLoginInfoInteractor(JobExecutor jobExecutor, PostExecutionThread postExecutionThread, LoginInfoRepository repository) {
        super(jobExecutor, postExecutionThread);
        this.repository = repository;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    @Override
    protected Observable buildObservable() {
        return Observable.create(new Observable.OnSubscribe<LoginInfo>() {
            @Override
            public void call(Subscriber<? super LoginInfo> subscriber) {
                try {
                    int ret = repository.save(loginInfo);
                    if (ret == 1) {
                        subscriber.onNext(loginInfo);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new SaveDataException());
                    }
                } catch (SaveDataException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
