package com.ptmlb.ca.ahgroup.data.cache.list;

import com.ptmlb.ca.ahgroup.data.cache.Cache;
import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Administrator on 2015/12/14.
 */

public class ListCacheTest {

    @Mock
    private Cache<LoginInfoEntity> mockCache;

    ListCache listCache;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        listCache = new ListCache<LoginInfoEntity>(mockCache);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testIsValid() throws Exception {

        LoginInfoEntity mockEntity1 = mock(LoginInfoEntity.class);
        LoginInfoEntity mockEntity2 = mock(LoginInfoEntity.class);

        List<LoginInfoEntity> entities = new ArrayList<>();
        entities.add(mockEntity1);
        entities.add(mockEntity2);

        given(mockCache.isValid(any(LoginInfoEntity.class))).willReturn(true);

        assertThat(listCache.isValid(entities), is(true));
        verify(mockCache, times(2)).isValid(any(LoginInfoEntity.class));

    }

    @Test
    public void testInvalidList() throws Exception {

        LoginInfoEntity mockEntity1 = mock(LoginInfoEntity.class);
        LoginInfoEntity mockEntity2 = mock(LoginInfoEntity.class);
        List<LoginInfoEntity> entities = new ArrayList<>();
        entities.add(mockEntity1);
        entities.add(mockEntity2);

        given(mockCache.isValid(mockEntity1)).willReturn(true);
        given(mockCache.isValid(mockEntity2)).willReturn(false);

        List<LoginInfoEntity> invalidList = listCache.invalidList(entities);
        assertThat(listCache.invalidList(entities).size(), is(1));
    }
}