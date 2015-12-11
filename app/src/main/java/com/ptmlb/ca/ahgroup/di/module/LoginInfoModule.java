package com.ptmlb.ca.ahgroup.di.module;

import com.ptmlb.ca.ahgroup.di.scope.ActivityScope;
import com.ptmlb.ca.ahgroup.domain.executor.JobExecutor;
import com.ptmlb.ca.ahgroup.domain.executor.PostExecutionThread;
import com.ptmlb.ca.ahgroup.domain.interactor.SaveLoginInfoInteractor;
import com.ptmlb.ca.ahgroup.domain.repository.LoginInfoRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2015/12/9.
 */

@Module
public class LoginInfoModule {

    public LoginInfoModule() {}

//    @Provides @ActivityScope
//    SaveLoginInfoPresenter provideSaveLoginInfoPresenter(@Named("Save") Interactor interactor, @Named("ForModel") Transformer transformer) {
//        return new SaveLoginInfoPresenter(interactor, transformer);
//    }

    @Provides @ActivityScope
    SaveLoginInfoInteractor provideSaveLoginInfoInteractor(@Named("concurrent") JobExecutor jobExecutor, PostExecutionThread postExecutionThread, LoginInfoRepository repository) {
        return new SaveLoginInfoInteractor(jobExecutor, postExecutionThread, repository);
    }

//    @Provides @Singleton
//    LoginInfoPersistenceDataSource provideLoginInfoPersistenceDataSource(OrmLiteHelper helper, Cache<LoginInfoEntity> cache, ListCache<LoginInfoEntity> listCache) {
//        return new LoginInfoPersistenceDataSourceImpl(helper.getLoginInfoDao(), cache, listCache);
//    }
}
