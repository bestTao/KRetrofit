package com.besttao.kretrofit

/**
 * Created by qiantao on 2017/8/24 0024
 */
data class HttpResult<T>(
        var retCode: String,
        var retMsg: String,
        var retContent: T
)