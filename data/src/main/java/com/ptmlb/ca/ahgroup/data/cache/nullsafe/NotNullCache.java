package com.ptmlb.ca.ahgroup.data.cache.nullsafe;

import com.ptmlb.ca.ahgroup.data.cache.Cache;

import javax.inject.Inject;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

public class NotNullCache<T> implements Cache<T> {

    @Inject
    public NotNullCache() {

    }

    @Override
    public boolean isValid(T t) {
        return t != null;
    }
}
