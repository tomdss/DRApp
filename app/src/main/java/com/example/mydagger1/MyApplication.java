package com.example.mydagger1;

import com.example.mydagger1.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class MyApplication extends DaggerApplication {

    private static MyApplication mSelf;

    public static MyApplication self() {
        return mSelf;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSelf = this;
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
