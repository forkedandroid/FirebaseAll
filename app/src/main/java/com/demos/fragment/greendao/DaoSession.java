package com.demos.fragment.greendao;

/**
 * Created by prashant.patel on 11/24/2017.
 */

import com.demos.fragment.bean.LiveCategory;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

/**
 * {@inheritDoc}
 *
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig liveCategoryDaoConfig;

    private final LiveCategoryDao liveCategoryDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        liveCategoryDaoConfig = daoConfigMap.get(LiveCategoryDao.class).clone();
        liveCategoryDaoConfig.initIdentityScope(type);

        liveCategoryDao = new LiveCategoryDao(liveCategoryDaoConfig, this);

        registerDao(LiveCategory.class, liveCategoryDao);
    }

    public void clear() {
        liveCategoryDaoConfig.clearIdentityScope();
    }

    public LiveCategoryDao getLiveCategoryDao() {
        return liveCategoryDao;
    }

}
