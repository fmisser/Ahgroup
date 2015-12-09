package com.ptmlb.ca.ahgroup.di.component;

import android.app.Activity;

import com.ptmlb.ca.ahgroup.di.module.ActivityModule;
import com.ptmlb.ca.ahgroup.di.scope.ActivityScope;

import dagger.Component;

/**
 * Created by Administrator on 2015/12/9.
 *
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}
