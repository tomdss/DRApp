package com.example.mydagger1.network;

import com.example.mydagger1.model.Repo;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiHelper {
    @GET("users/{user}")
    Call<Repo> listRepos(@Path("user") String user);

    @GET("users/{user}")
    Single<Repo> rxListRepos(@Path("user") String user);
}
