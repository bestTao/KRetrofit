package com.besttao.kretrofit

/**
 * Created by qiantao on 2017/9/28 0028
 */

data class Mobile(
        var prefix: Int,
        var province: String,
        var city: String,
        var isp: String,
        var code: Int,
        var zipcode: Int,
        var types: String,
        var mobile: String
)