package com.ptmlb.ca.ahgroup;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ptmlb.ca.ahgroup.di.component.DaggerLoginInfoComponent;
import com.ptmlb.ca.ahgroup.di.component.LoginInfoComponent;
import com.ptmlb.ca.ahgroup.di.module.ActivityModule;
import com.ptmlb.ca.ahgroup.model.LoginInfoModel;
import com.ptmlb.ca.ahgroup.presenter.SaveLoginInfoPresenter;
import com.ptmlb.ca.ahgroup.view.SaveLoginInfoView;

import java.util.Date;

import javax.inject.Inject;


public class MainActivity extends BaseActivity implements SaveLoginInfoView {

    LoginInfoComponent component;

    TextView textViewHello;

    @Inject
    SaveLoginInfoPresenter saveLoginInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewHello = (TextView) findViewById(R.id.tvHello);
        textViewHello.setOnClickListener(tvHelloClick);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        saveLoginInfoPresenter.setView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initializeInjector() {
        super.initializeInjector();

        component = DaggerLoginInfoComponent.builder()
                        .applicationComponent(getApplicationComponent())
                        .activityModule(new ActivityModule(this))
                        .build();
        component.inject(this);
    }

    public View.OnClickListener tvHelloClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String message = "onClick";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

            LoginInfoModel loginInfoModel = new LoginInfoModel();
            loginInfoModel.setAccount("18058159956");
            loginInfoModel.setPassword("06260513");
            loginInfoModel.setAccessToken("123456");
            loginInfoModel.setLastLoginDate(new Date());

            saveLoginInfoPresenter.saveLoginInfo(loginInfoModel);
        }
    };

    @Override
    public void update(LoginInfoModel loginInfo) {
        Toast.makeText(MainActivity.this, "update", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        Toast.makeText(MainActivity.this, "showLoading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        Toast.makeText(MainActivity.this, "hideLoading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getContext();
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(MainActivity.this, "showError:" + errorMsg, Toast.LENGTH_SHORT).show();
    }
}
