package com.ptmlb.ca.ahgroup.executor;

import com.ptmlb.ca.ahgroup.domain.executor.PostExecutionThread;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2015/12/8.
 */

@Singleton
public class PostExecutionThreadImpl implements PostExecutionThread {

    @Inject
    PostExecutionThreadImpl() {}

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
