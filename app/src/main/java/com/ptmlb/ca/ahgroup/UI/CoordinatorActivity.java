package com.ptmlb.ca.ahgroup.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ptmlb.ca.ahgroup.R;

public class CoordinatorActivity extends AppCompatActivity {


    private LinearLayout headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        headerLayout = (LinearLayout)findViewById(R.id.collapsing_layout);
    }
}
