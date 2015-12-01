package com.ptmlb.ca.ahgroup.data.cache.ttl;

import com.ptmlb.ca.ahgroup.data.cache.Cache;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

public class TtlCache<T extends TtlCacheObject> implements Cache<T> {

    private final long ttlMillis;

    public TtlCache(long ttl, TimeUnit timeUnit) {
        this.ttlMillis = timeUnit.toMillis(ttl);
    }

    @Override
    public boolean isValid(T t) {
        return (t.getPersistedTime() + ttlMillis) > System.currentTimeMillis();
    }
}
