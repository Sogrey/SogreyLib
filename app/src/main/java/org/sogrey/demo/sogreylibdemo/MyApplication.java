package org.sogrey.demo.sogreylibdemo;

import org.sogrey.base.BaseApplication;

/**
 * Created by Administrator on 2018/8/1.
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected boolean isLog() {
        return true;
    }

    @Override
    protected boolean isDebugLog() {
        return BuildConfig.DEBUG;
    }
}
