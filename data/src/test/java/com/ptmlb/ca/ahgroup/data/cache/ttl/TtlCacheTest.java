package com.ptmlb.ca.ahgroup.data.cache.ttl;

import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Administrator on 2015/12/15.
 *
 */

public class TtlCacheTest {

    private TtlCache<LoginInfoEntity> cache;

    @Before
    public void setUp() throws Exception {
        cache = new TtlCache<>(60, TimeUnit.SECONDS);
    }

    @Test
    public void testIsValid() throws Exception {

        LoginInfoEntity mockEntity = new LoginInfoEntity();
        mockEntity.setPersistedTime(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(59));
        assertThat(cache.isValid(mockEntity), is(true));

        mockEntity.setPersistedTime(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(60));
        assertThat(cache.isValid(mockEntity), is(false));
    }
}