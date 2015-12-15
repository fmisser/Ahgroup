package com.ptmlb.ca.ahgroup.data.cache.nullsafe;

import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;

/**
 * Created by Administrator on 2015/12/15.
 *
 */

public class NotNullCacheTest {

    NotNullCache<LoginInfoEntity> cache;

    @Before
    public void setUp() throws Exception {
        cache = new NotNullCache<>();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testIsValid() throws Exception {

        assertThat(cache.isValid(null), is(false));

        LoginInfoEntity mockEntity = mock(LoginInfoEntity.class);
        assertThat(cache.isValid(mockEntity), is(true));
    }
}