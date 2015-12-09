package com.ptmlb.ca.ahgroup.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ptmlb.ca.ahgroup.AndroidApplication;
import com.ptmlb.ca.ahgroup.BuildConfig;
import com.ptmlb.ca.ahgroup.data.executor.JobExecutorImpl;
import com.ptmlb.ca.ahgroup.domain.executor.JobExecutor;
import com.ptmlb.ca.ahgroup.domain.executor.PostExecutionThread;
import com.ptmlb.ca.ahgroup.executor.PostExecutionThreadImpl;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2015/12/9.
 *
 */

@Module
public class ApplicationModule {

    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton @Named("concurrent")
    JobExecutor provideConcurrentJobExecutor() {
        return new JobExecutorImpl(BuildConfig.CONCURRENT_JOB_EXECUTOR_CORE_POOL_SIZE,
                                        BuildConfig.CONCURRENT_JOB_EXECUTOR_MAXIMUM_POOL_SIZE,
                                        BuildConfig.JOB_EXECUTOR_KEPP_ALIVE_TIME_SEC,
                                        TimeUnit.SECONDS,
                                        new LinkedBlockingQueue<Runnable>());
    }

    @Provides @Singleton @Named("sequence")
    JobExecutor provideSequenceJobExecutor() {
        return new JobExecutorImpl(1, 1, BuildConfig.JOB_EXECUTOR_KEPP_ALIVE_TIME_SEC, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Provides @Singleton
    PostExecutionThread providePostExecutionThread(PostExecutionThreadImpl postExecutionThread) {
        return postExecutionThread;
    }

    @Provides @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

}
