package com.besttao.kretrofit

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by qiantao on 2017/8/24 0024
 */
interface HttpService {
    @GET("user/getBeijingTime")
    fun fetchBeijingTime(): Call<HttpResult<BeijingTime>>
}