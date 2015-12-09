package com.ptmlb.ca.ahgroup.di.component;

import com.ptmlb.ca.ahgroup.AndroidApplication;
import com.ptmlb.ca.ahgroup.BaseActivity;
import com.ptmlb.ca.ahgroup.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2015/12/9.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity activity);
}
