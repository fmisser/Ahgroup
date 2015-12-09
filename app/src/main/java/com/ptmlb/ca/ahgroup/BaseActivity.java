package com.ptmlb.ca.ahgroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ptmlb.ca.ahgroup.di.component.ApplicationComponent;

/**
 * Created by Administrator on 2015/12/9.
 *
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();
    }

    protected void initializeInjector() {
        this.getApplicationComponent().inject(this);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication)getApplication()).getApplicationComponent();
    }
}
