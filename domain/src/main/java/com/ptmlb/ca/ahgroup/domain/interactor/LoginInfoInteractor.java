package com.ptmlb.ca.ahgroup.domain.interactor;

import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.exception.GetDataException;
import com.ptmlb.ca.ahgroup.domain.executor.JobExecutor;
import com.ptmlb.ca.ahgroup.domain.executor.PostExecutionThread;
import com.ptmlb.ca.ahgroup.domain.repository.LoginInfoRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2015/12/8.
 */
public class LoginInfoInteractor extends Interactor {

    private LoginInfoRepository repository;

    public LoginInfoInteractor(JobExecutor jobExecutor, PostExecutionThread postExecutionThread, LoginInfoRepository repository) {
        super(jobExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    protected Observable buildObservable() {
        return Observable.create(new Observable.OnSubscribe<List<LoginInfo>>() {
            @Override
            public void call(Subscriber<? super List<LoginInfo>> subscriber) {

                try {
                    List<LoginInfo> loginInfoList = repository.getLoginInfoList();
                    subscriber.onNext(loginInfoList);
                    subscriber.onCompleted();
                } catch (GetDataException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
