package com.github.mjjaniec.time.updater.api;

import retrofit2.Call;
import retrofit2.http.GET;



public interface GithubApi {

    @GET("repos/mjjaniec/time/releases/latest")
    Call<Release> latestRelease();
}
