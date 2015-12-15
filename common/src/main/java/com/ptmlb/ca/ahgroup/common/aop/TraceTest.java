package com.ptmlb.ca.ahgroup.common.aop;

/**
 * Created by Administrator on 2015/12/15.
 */

public class TraceTest {
    @DebugTrace
    public void testDebugTrace() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
