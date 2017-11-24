package com.demos.fragment;

import android.app.Application;
import android.content.Context;

import com.demos.fragment.greendao.DaoMaster;
import com.demos.fragment.greendao.DaoSession;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


/**
 * Created by prashant.patel on 11/24/2017.
 */


public class App extends Application {

    private static final String BUGLY_ID  = "28aeafeef1----";

    private DaoMaster.DevOpenHelper mHelper;

    private DaoSession mDaoSession;

    private AppComponent mAppComponent;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

      //  MultiDex.install(base);
        //Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDatabase();
        // 调试时，将第三个参数改为true
       //111 Bugly.init(this,BUGLY_ID,false);
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this, Constants.BASE_URL)).build();

       /* NeverCrash.init(new NeverCrash.CrashHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                CrashReport.postCatchedException(e);
            }
        });*/
    }



    public void initDatabase(){
        mHelper = new DaoMaster.DevOpenHelper(this,"tv-db",null);

        DaoMaster daoMaster = new DaoMaster(mHelper.getWritableDatabase());

        mDaoSession = daoMaster.newSession();
    }

    public AppComponent getAppCommponent(){
        return mAppComponent;
    }

    public DaoSession getDaoSession(){
        return mDaoSession;
    }

}
