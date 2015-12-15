package com.ptmlb.ca.ahgroup.common.aop;

import android.util.Log;

/**
 * Created by Administrator on 2015/12/15.
 */

public class DebugLog {
    private DebugLog() {}

    public static void log(String tag, String message) {
        Log.i(tag, message);
    }
}
