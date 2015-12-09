package com.ptmlb.ca.ahgroup.di.module;

import com.ptmlb.ca.ahgroup.data.cache.Cache;
import com.ptmlb.ca.ahgroup.data.cache.list.ListCache;
import com.ptmlb.ca.ahgroup.data.cache.ttl.TtlCache;
import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;
import com.ptmlb.ca.ahgroup.data.persistence.OrmLiteHelper;
import com.ptmlb.ca.ahgroup.data.repository.datasource.LoginInfoPersistenceDataSource;
import com.ptmlb.ca.ahgroup.data.repository.datasource.LoginInfoPersistenceDataSourceImpl;
import com.ptmlb.ca.ahgroup.di.scope.ActivityScope;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;
import com.ptmlb.ca.ahgroup.domain.executor.JobExecutor;
import com.ptmlb.ca.ahgroup.domain.executor.PostExecutionThread;
import com.ptmlb.ca.ahgroup.domain.interactor.Interactor;
import com.ptmlb.ca.ahgroup.domain.interactor.SaveLoginInfoInteractor;
import com.ptmlb.ca.ahgroup.domain.repository.LoginInfoRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2015/12/9.
 */

@Module
public class LoginInfoModule {

    private LoginInfo loginInfo;

    public LoginInfoModule(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    @Provides @ActivityScope
    Interactor provideSaveLoginInfoInteractor(@Named("concurrent") JobExecutor jobExecutor, PostExecutionThread postExecutionThread, LoginInfoRepository repository) {
        return new SaveLoginInfoInteractor(jobExecutor, postExecutionThread, repository, loginInfo);
    }

    @Provides @ActivityScope
    LoginInfoPersistenceDataSource provideLoginInfoPersistenceDataSource(OrmLiteHelper helper, Cache<LoginInfoEntity> cache, ListCache<LoginInfoEntity> listCache) {
        return new LoginInfoPersistenceDataSourceImpl(helper.getLoginInfoDao(), cache, listCache);
    }

    @Provides @Singleton
    Cache<LoginInfoEntity> provideCache() {
        return new TtlCache<>(30, TimeUnit.DAYS);
    }

    @Provides @Singleton
    ListCache<LoginInfoEntity> provideListCache(Cache<LoginInfoEntity> cache) {
        return new ListCache<>(cache);
    }

}
