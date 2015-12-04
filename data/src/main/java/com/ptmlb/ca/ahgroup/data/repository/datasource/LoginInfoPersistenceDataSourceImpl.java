package com.ptmlb.ca.ahgroup.data.repository.datasource;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.mobandme.android.transformer.Transformer;
import com.ptmlb.ca.ahgroup.data.cache.Cache;
import com.ptmlb.ca.ahgroup.data.cache.list.ListCache;
import com.ptmlb.ca.ahgroup.data.entity.LoginInfoEntity;
import com.ptmlb.ca.ahgroup.data.exception.DeletePersistenceDataException;
import com.ptmlb.ca.ahgroup.data.exception.GetPersistenceDataException;
import com.ptmlb.ca.ahgroup.data.exception.InvalidPersistenceDataException;
import com.ptmlb.ca.ahgroup.data.exception.PersistDataException;
import com.ptmlb.ca.ahgroup.data.exception.UnknownPersistenceException;
import com.ptmlb.ca.ahgroup.domain.entity.LoginInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 *
 */

public class LoginInfoPersistenceDataSourceImpl implements LoginInfoPersistenceDataSource {

    private Dao<LoginInfoEntity, String> dao;
    private Cache<LoginInfoEntity> cache;
    private ListCache<LoginInfoEntity> listCache;
    private static final Transformer transformer = new Transformer.Builder().build(LoginInfoEntity.class);

    public LoginInfoPersistenceDataSourceImpl(Dao<LoginInfoEntity, String> dao, Cache<LoginInfoEntity> cache, ListCache<LoginInfoEntity> listCache) {
        this.dao = dao;
        this.cache = cache;
        this.listCache = listCache;
    }

    @Override
    public List<LoginInfo> getLoginInfoList() throws GetPersistenceDataException, InvalidPersistenceDataException, UnknownPersistenceException {
        try {
            List<LoginInfoEntity> entities = dao.queryForAll();
            if (!listCache.isValid(entities)) {
                deleteLoginInfoEntities(entities);
                throw new InvalidPersistenceDataException();
            }
            List<LoginInfo> loginInfoList = new ArrayList<>();
            for (LoginInfoEntity entity : entities) {
                loginInfoList.add(transformer.transform(entity, LoginInfo.class));
            }
            return loginInfoList;
        } catch (SQLException e) {
            throw new GetPersistenceDataException();
        } catch (Exception e) {
            throw new UnknownPersistenceException();
        }
    }

    @Override
    public int save(LoginInfo loginInfo) throws PersistDataException, UnknownPersistenceException {

        LoginInfoEntity entity = transformer.transform(loginInfo, LoginInfoEntity.class);
        entity.setPersistedTime(System.currentTimeMillis());
        try {
            return dao.create(entity);
        } catch (SQLException e) {
            throw new PersistDataException();
        } catch (Exception e) {
            throw new UnknownPersistenceException();
        }
    }

    @Override
    public int delete(List<LoginInfo> loginInfoList) throws DeletePersistenceDataException, UnknownPersistenceException {

        List<String> deleteAccounts = new ArrayList<>();
        for (LoginInfo loginInfo : loginInfoList) {
            deleteAccounts.add(loginInfo.getAccount());
        }
        try {
            return internalDeleteLoginInfoEntities(deleteAccounts);
        } catch (SQLException e) {
            throw new DeletePersistenceDataException();
        } catch (Exception e) {
            throw new UnknownPersistenceException();
        }
    }

    private int deleteLoginInfoEntities(List<LoginInfoEntity> entities) throws SQLException {
        if (entities != null && entities.size() > 0) {
            List<String> deleteAccounts = new ArrayList<>();
            for (LoginInfoEntity entity : entities) {
                deleteAccounts.add(entity.getAccount());
            }
            return internalDeleteLoginInfoEntities(deleteAccounts);
        }
        return 0;
    }

    private int internalDeleteLoginInfoEntities(List<String> accounts) throws SQLException {
        DeleteBuilder<LoginInfoEntity, String> deleteBuilder = dao.deleteBuilder();
        deleteBuilder.where().in("account", accounts);
        return deleteBuilder.delete();
    }
}
