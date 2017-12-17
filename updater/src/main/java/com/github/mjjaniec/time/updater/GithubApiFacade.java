package com.github.mjjaniec.time.updater;

import com.github.mjjaniec.time.updater.api.GithubApi;
import com.github.mjjaniec.time.updater.api.Release;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Optional;

public class GithubApiFacade {

    private GithubApi api = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory
                   .create())
            .build().create(GithubApi.class);

    public Optional<Release> latest() {
        try {
            Call<Release> call = api.latestRelease();
            Response<Release> response = call.execute();
            return Optional.ofNullable(response.body());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
