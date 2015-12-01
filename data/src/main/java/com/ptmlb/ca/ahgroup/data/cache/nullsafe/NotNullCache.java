package com.ptmlb.ca.ahgroup.data.cache.nullsafe;

import com.ptmlb.ca.ahgroup.data.cache.Cache;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

public class NotNullCache<T> implements Cache<T> {
    @Override
    public boolean isValid(T t) {
        return t != null;
    }
}
