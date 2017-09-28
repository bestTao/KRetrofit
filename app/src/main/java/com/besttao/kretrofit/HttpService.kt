package com.besttao.kretrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by qiantao on 2017/8/24 0024
 */
interface HttpService {
    @GET
    fun fetchMobileLocale(
            @Url url: String = "http://sj.apidata.cn/",
            @Query("mobile") mobileNumber: String
    ): Call<HttpResult<Mobile>>
}