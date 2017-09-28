package com.besttao.kretrofit

/**
 * Created by qiantao on 2017/8/24 0024
 */
data class HttpResult<T>(
        var status: Int,
        var message: String,
        var data: T
)