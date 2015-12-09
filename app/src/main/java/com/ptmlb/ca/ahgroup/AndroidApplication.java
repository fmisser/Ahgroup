package com.ptmlb.ca.ahgroup;

import android.app.Application;

import com.ptmlb.ca.ahgroup.di.component.ApplicationComponent;
import com.ptmlb.ca.ahgroup.di.component.DaggerApplicationComponent;
import com.ptmlb.ca.ahgroup.di.module.ApplicationModule;

/**
 * Created by Administrator on 2015/12/9.
 */

public class AndroidApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    public ApplicationComponent getApplicationComponent() {
        return component;
    }

    private void initializeInjector() {
        component = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }
}
