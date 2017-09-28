package com.besttao.kretrofit

/**
 * Created by qiantao on 2017/8/24
 */
open class HttpException(
        var errorCode: Int,
        var errorMsg: String
) : RuntimeException(errorCode.toString())