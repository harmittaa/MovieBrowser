package com.github.harmittaa.moviebrowser.network

import com.github.harmittaa.moviebrowser.BuildConfig
import com.github.harmittaa.moviebrowser.data.MovieApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single { provideLoggingInterceptor() }
    factory { AuthInterceptor() }
    single { provideOkHttpClient(authInterceptor = get(), loggingInterceptor = get()) }

    single { provideMoshi() }
    factory { provideMoshiConverterFactory(moshi = get()) }

    single { provideRetrofit(okHttpClient = get(), moshiConverterFactory = get()) }

    single { provideMovieApi(retrofit = get()) }
}

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY
    return logger
}

fun provideRetrofit(
    okHttpClient: OkHttpClient,
    moshiConverterFactory: MoshiConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.TMDB_URL)
        .client(okHttpClient)
        .addConverterFactory(moshiConverterFactory)
        .build()
}

fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

private fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
    return MoshiConverterFactory.create(moshi)
}

fun provideOkHttpClient(
    authInterceptor: AuthInterceptor,
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor).build()
}

fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain) = chain.proceed(
        chain.request().newBuilder().addHeader("Authorization", "Bearer ${BuildConfig.TMDB_KEY}")
            .build()
    )
}
