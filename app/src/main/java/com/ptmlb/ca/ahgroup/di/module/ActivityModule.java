package com.ptmlb.ca.ahgroup.di.module;

import android.app.Activity;

import com.ptmlb.ca.ahgroup.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2015/12/9.
 *
 */

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides @ActivityScope
    Activity activity() {
        return activity;
    }
}
