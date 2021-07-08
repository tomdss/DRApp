package com.example.mydagger1.di;

import com.example.mydagger1.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();
}
