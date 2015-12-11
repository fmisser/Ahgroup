package com.ptmlb.ca.ahgroup.di.component;

import android.content.Context;
import android.content.SharedPreferences;

import com.j256.ormlite.dao.Dao;
import com.ptmlb.ca.ahgroup.BaseActivity;
import com.ptmlb.ca.ahgroup.data.cache.Cache;
import com.ptmlb.ca.ahgroup.data.cache.list.ListCache;
import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;
import com.ptmlb.ca.ahgroup.data.entity.mapper.LoginInfoEntityMapper;
import com.ptmlb.ca.ahgroup.di.module.ApplicationModule;
import com.ptmlb.ca.ahgroup.domain.executor.JobExecutor;
import com.ptmlb.ca.ahgroup.domain.executor.PostExecutionThread;
import com.ptmlb.ca.ahgroup.domain.repository.LoginInfoRepository;
import com.ptmlb.ca.ahgroup.model.mapper.LoginInfoModelMapper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2015/12/9.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity activity);


    Context context();

    @Named("concurrent")
    JobExecutor concurrentJobExecutor();

    @Named("sequence")
    JobExecutor sequenceJobExecutor();

    PostExecutionThread postExecutionThread();

    SharedPreferences sharedPreferences();

    Dao<LoginInfoEntity, String> loginInfoDao();

    Cache<LoginInfoEntity> loginInfoCache();

    ListCache<LoginInfoEntity> loginInfoListCache();

    LoginInfoRepository loginInfoRepository();

    LoginInfoModelMapper loginInfoModelMapper();

    LoginInfoEntityMapper loginInfoEntityMapper();
}
