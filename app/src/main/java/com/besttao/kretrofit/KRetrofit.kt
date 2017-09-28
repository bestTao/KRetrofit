package com.besttao.kretrofit

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Created by qiantao on 2017/8/24
 */

private val DEFAULT_TIMEOUT = 5L

private val BASE_URL = "http://sj.apidata.cn/"

private fun buildOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()

    //日志显示级别
    val level = HttpLoggingInterceptor.Level.BODY
    //新建log拦截器
    val loggingInterceptor = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message ->
                Log.d("HttpMethod", "OkHttp====Message:" + message)
            })
    loggingInterceptor.level = level
    //OkHttp进行添加拦截器loggingInterceptor
    builder.addInterceptor(loggingInterceptor)

    //OkHttp超时时间
    builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
    return builder.build()
}

private var retrofit: Retrofit = Retrofit.Builder()
        .client(buildOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

var service: HttpService = retrofit.create(HttpService::class.java)


private fun <T> HttpResult<T>.parse(): T {
    if (this.status != 1) {
        throw object : HttpException(this.status, this.message) {}
    } else {
        return this.data
    }
}

class RequestWrapper<T, R : Context> {
    var success: (T, R) -> Unit = { t, c -> Unit}
    var fail: (e: Exception) -> Unit = {}

    fun success(success: (T, R) -> Unit) {
        this.success = success
    }

    fun fail(fail: (e: Exception) -> Unit) {
        this.fail = fail
    }
}

fun <T, R : Context> R.httpRequest(call: (service: HttpService) -> Call<HttpResult<T>>,
                                   task: RequestWrapper<T, R>.() -> Unit): Call<HttpResult<T>> {
    val c = call(service)
    val wrap = RequestWrapper<T, R>()
    wrap.task()
    BackgroundExecutor.submit {
        try {
            val response = c.execute()
            if (response.isSuccessful) {
                val result = response.body().parse()
                uiThread { ref ->
                    wrap.success(result, ref)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            var errorMsg =
                    if (e.message != null && e.message?.isNotBlank() == true) "网络开小差~\\\\(≧▽≦)/"
                    else null
            if (e is HttpException) errorMsg = e.errorMsg
            if (errorMsg != null) {
                uiThread { ref ->
                    ref.toast(errorMsg!!)
                    wrap.fail(e)
                }
            }
        }
    }
    return c
}

private fun <R : Context> R.uiThread(f: (R) -> Unit): Boolean {
    val ref = WeakReference(this).get() ?: return false
    if (ref is Activity && ref.isFinishing) return false
    if (ref is Fragment && ref.isDetached) return false
    if (ContextHelper.mainThread == Thread.currentThread()) {
        f(ref)
    } else {
        ContextHelper.handler.post { f(ref) }
    }
    return true
}

object BackgroundExecutor {
    private var executor: ExecutorService =
            Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())

    fun <R> submit(task: () -> R): Future<R> = executor.submit(task)
}

private object ContextHelper {
    val handler = Handler(Looper.getMainLooper())
    val mainThread: Thread = Looper.getMainLooper().thread
}