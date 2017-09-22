package com.besttao.kretrofit

/**
 * Created by qiantao on 2017/8/24
 */
open class HttpException(
        var errorCode: String,
        var errorMsg: String
) : RuntimeException(errorCode)