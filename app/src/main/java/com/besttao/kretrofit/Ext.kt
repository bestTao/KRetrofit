package com.besttao.kretrofit

import android.content.Context
import android.widget.Toast

/**
 * Created by qiantao on 2017/8/24 0024
 */
fun Context.toast(text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, length).show()
}