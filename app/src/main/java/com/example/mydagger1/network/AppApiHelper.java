package com.example.mydagger1.network;

import com.example.mydagger1.model.Repo;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppApiHelper implements ApiHelper {

    private static AppApiHelper INSTANCE = null;
    private static ApiHelper apiHelper;

    private AppApiHelper() {
    }

    public static AppApiHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppApiHelper();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            apiHelper = retrofit.create(ApiHelper.class);
        }
        return INSTANCE;
    }



    @Override
    public Call<Repo> listRepos(String user) {
        return apiHelper.listRepos(user);
    }

    @Override
    public Single<Repo> rxListRepos(String user) {
        return apiHelper.rxListRepos(user);
    }
}
