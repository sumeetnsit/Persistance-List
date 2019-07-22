package com.sumeet.persistancelist;

import android.app.Application;

public class MemeApplication extends Application {

    public static MemeApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static MemeApplication getApplicationInstance() {
        return mApp;
    }
}

