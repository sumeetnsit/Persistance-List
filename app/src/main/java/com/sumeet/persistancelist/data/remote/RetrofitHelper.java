package com.sumeet.persistancelist.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.sumeet.persistancelist.MemeApplication;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {


    protected <T> T create(Class<T> clazz, String baseUrl) {
        return retrofit(baseUrl).create(clazz);
    }

    private Retrofit retrofit(String baseUrl) {
        OkHttpClient okHttpClient =
                new OkHttpClient()
                        .newBuilder()
                        .addInterceptor(new ChuckInterceptor(MemeApplication.getApplicationInstance()))
                        .build();

        GsonBuilder gsonBuilder = new GsonBuilder();

        Gson customGson = gsonBuilder.create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(customGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}

