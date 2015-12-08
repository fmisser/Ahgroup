package com.ptmlb.ca.ahgroup.executor;

import com.ptmlb.ca.ahgroup.domain.executor.PostExecutionThread;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2015/12/8.
 */
public class PostExecutionThreadImpl implements PostExecutionThread {
    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
