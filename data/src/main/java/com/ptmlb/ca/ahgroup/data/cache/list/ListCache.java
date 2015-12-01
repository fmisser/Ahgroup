package com.ptmlb.ca.ahgroup.data.cache.list;

import com.ptmlb.ca.ahgroup.data.cache.Cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ListCache<T> implements Cache<List<T>> {

    private final Cache<T> cache;

    public ListCache(Cache<T> cache) {
        this.cache = cache;
    }

    @Override
    public boolean isValid(List<T> ts) {

        if (ts == null || ts.size() == 0) {
            return false;
        }

        for (T t : ts) {
            if (!isValidSingle(t)) {
                return false;
            }
        }

        return true;
    }

    public List<T> invalidList(List<T> ts) {
        List<T> invalidList = new ArrayList<>();
        for (T t : ts) {
            if (!isValidSingle(t)) {
                invalidList.add(t);
            }
        }
        return invalidList;
    }

    private boolean isValidSingle(T t) {
        return cache.isValid(t);
    }
}
