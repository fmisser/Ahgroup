package com.ptmlb.ca.ahgroup.data.cache;

/**
 * Created by Administrator on 2015/12/1.
 */
public interface Cache<T> {
    boolean isValid(T t);
}
