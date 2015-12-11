package com.ptmlb.ca.ahgroup.domain.interactor;

import com.ptmlb.ca.ahgroup.domain.executor.JobExecutor;
import com.ptmlb.ca.ahgroup.domain.executor.PostExecutionThread;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by Administrator on 2015/12/7.
 *
 */

public abstract class Interactor {

    private JobExecutor jobExecutor;
    private PostExecutionThread postExecutionThread;
    private Subscription subscription = Subscriptions.empty();

    public Interactor(JobExecutor jobExecutor, PostExecutionThread postExecutionThread) {
        this.jobExecutor = jobExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    protected abstract Observable buildObservable();

    @SuppressWarnings("unchecked")
    public void execute(Subscriber subscriber) {
        this.subscription = buildObservable()
                .observeOn(postExecutionThread.getScheduler())
                .subscribeOn(Schedulers.from(jobExecutor))
                .subscribe(subscriber);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
