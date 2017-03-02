package com.example.qinhe.inspopup.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequestManager {

    public static final String URL = "http://gank.io/api/";
    public static boolean isAddLog;
    private static final int DEFAULT_TIMEOUT = 10;
    private Retrofit retrofit;
    private static RetrofitHttpClient apiService;
    private static HttpRequestManager httpRequestManager;
    private OkHttpClient.Builder httpClientBuilder;

    private HttpRequestManager() {
        httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(interceptor);
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(URL)
                .build();

        apiService = retrofit.create(RetrofitHttpClient.class);
    }


    public static RetrofitHttpClient getApiServices() {
        if (httpRequestManager == null) {
            synchronized (HttpRequestManager.class) {
                if (httpRequestManager == null) {
                    httpRequestManager = new HttpRequestManager();
                }
            }
        }
        return apiService;
    }
}
