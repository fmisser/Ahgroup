package com.ptmlb.ca.ahgroup.domain.executor;

import rx.Scheduler;

/**
 * Created by Administrator on 2015/12/8.
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
