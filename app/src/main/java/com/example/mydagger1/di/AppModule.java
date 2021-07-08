package com.example.mydagger1.di;

import com.example.mydagger1.network.AppApiHelper;
import com.example.mydagger1.utils.ImageLoader;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    AppApiHelper provideAppApiHelper() {
        return AppApiHelper.getInstance();
    }

    @Provides
    ImageLoader provideImageLoader() {
        return new ImageLoader();
    }

    // scope : khi xét scope cho 1 đối tượng thì khi activity đó bị hủy thì đối tượng đó cũng bị hủy,khi class khởi tạo lại thì đối tượng cũng đc khởi tạo lại.
    // còn nếu là singleton thì sau khi activity khởi tạo lại thì vẫn là đối tượng cũ

}
