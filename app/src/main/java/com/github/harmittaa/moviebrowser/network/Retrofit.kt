package com.github.harmittaa.moviebrowser.network

/*
import com.github.harmittaa.moviebrowser.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

fun provideRetrofit()


fun provideOkHttpClient(authInterceptor: AuthInterceptor, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).addInterceptor(loggingInterceptor).build()
}

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BASIC
    return logger
}

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        req.headers.newBuilder().add("api_key", )
        val url = req.url.newBuilder().addQueryParameter("api_key", BuildConfig.).build()
    }

}
*/
