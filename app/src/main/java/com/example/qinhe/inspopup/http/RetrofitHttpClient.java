package com.example.qinhe.inspopup.http;

import com.example.qinhe.inspopup.GankIoDataBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by QinHe on 2017/3/1.
 */

public interface RetrofitHttpClient {

    @GET("data/{type}/{pre_page}/{page}")
    Observable<GankIoDataBean> getGankIoData(@Path("type") String id
            , @Path("page") int page, @Path("pre_page") int pre_page);
}
