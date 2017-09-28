package com.besttao.kretrofit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvRequest.setOnClickListener {
            httpRequest(call = { service.fetchMobileLocale(mobileNumber = "13888888888") }, task = {
                success { mobile, activity ->
                    activity.toast(mobile.toString())
                }
            })
        }
    }
}
