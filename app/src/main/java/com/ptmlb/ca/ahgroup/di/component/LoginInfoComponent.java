package com.ptmlb.ca.ahgroup.di.component;

import com.ptmlb.ca.ahgroup.MainActivity;
import com.ptmlb.ca.ahgroup.di.module.ActivityModule;
import com.ptmlb.ca.ahgroup.di.module.LoginInfoModule;
import com.ptmlb.ca.ahgroup.di.scope.ActivityScope;

import dagger.Component;

/**
 * Created by Administrator on 2015/12/9.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, LoginInfoModule.class})
public interface LoginInfoComponent extends ActivityComponent {
    void inject(MainActivity activity);
}
