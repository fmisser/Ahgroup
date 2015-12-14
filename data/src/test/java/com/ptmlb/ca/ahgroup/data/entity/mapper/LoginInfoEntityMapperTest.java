package com.ptmlb.ca.ahgroup.data.entity.mapper;

import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Administrator on 2015/12/14.
 */
public class LoginInfoEntityMapperTest {

    private static String Fake_Account = "18058159956";
    private static String Fake_Password = "123456";
    private static String Fake_AccessToken = "654321";
    private static Date Fake_CurrentData = new Date();

    private LoginInfoEntityMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new LoginInfoEntityMapper();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testTransform() throws Exception {
        LoginInfoEntity entity = createFakeLoginInfoEntity();
        LoginInfo loginInfo = mapper.transform(entity);

        Assert.assertThat(loginInfo, is(instanceOf(LoginInfo.class)));
        assertEquals(Fake_Account, loginInfo.getAccount());
        assertEquals(Fake_Password, loginInfo.getPassword());
        assertEquals(Fake_AccessToken, loginInfo.getAccessToken());
        assertEquals(Fake_CurrentData, loginInfo.getLastLoginDate());
    }

    @Test
    public void testTransformList() throws Exception {
        LoginInfoEntity mockEntity1 = mock(LoginInfoEntity.class);
        LoginInfoEntity mockEntity2 = mock(LoginInfoEntity.class);

        List<LoginInfoEntity> entities = new ArrayList<>(5);
        entities.add(mockEntity1);
        entities.add(mockEntity2);

        List<LoginInfo> loginInfoList = mapper.transformList(entities);
        assertThat(loginInfoList.get(0), is(instanceOf(LoginInfo.class)));
        assertThat(loginInfoList.get(1), is(instanceOf(LoginInfo.class)));
        assertThat(loginInfoList.size(), is(2));
    }

    @Test
    public void testTransformToEntity() throws Exception {
        LoginInfo loginInfo = createFakeLoginInfo();
        LoginInfoEntity entity = mapper.transformToEntity(loginInfo);
        assertEquals(Fake_Account, entity.getAccount());
        assertEquals(Fake_Password, entity.getPassword());
        assertEquals(Fake_AccessToken, entity.getAccessToken());
        assertEquals(Fake_CurrentData, entity.getLastLoginDate());
    }

    @Test
    public void testTransformToEntityList() throws Exception {
        LoginInfo mockLoginInfo1 = mock(LoginInfo.class);
        LoginInfo mockLoginInfo2 = mock(LoginInfo.class);

        List<LoginInfo> loginInfoList = new ArrayList<>(5);
        loginInfoList.add(mockLoginInfo1);
        loginInfoList.add(mockLoginInfo2);

        List<LoginInfoEntity> entities = mapper.transformToEntityList(loginInfoList);
        assertThat(entities.get(0), is(instanceOf(LoginInfoEntity.class)));
        assertThat(entities.get(1), is(instanceOf(LoginInfoEntity.class)));
        assertThat(entities.size(), is(2));
    }

    private LoginInfoEntity createFakeLoginInfoEntity() {
        LoginInfoEntity entity = new LoginInfoEntity();
        entity.setAccount(Fake_Account);
        entity.setPassword(Fake_Password);
        entity.setAccessToken(Fake_AccessToken);
        entity.setLastLoginDate(Fake_CurrentData);
        return entity;
    }

    private LoginInfo createFakeLoginInfo() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setAccount(Fake_Account);
        loginInfo.setPassword(Fake_Password);
        loginInfo.setAccessToken(Fake_AccessToken);
        loginInfo.setLastLoginDate(Fake_CurrentData);
        return loginInfo;
    }
}