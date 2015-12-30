package com.ptmlb.ca.ahgroup.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ptmlb.ca.ahgroup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceContentFragment extends Fragment {


    public ServiceContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_content, container, false);
    }

}
