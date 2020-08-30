package com.github.harmittaa.moviebrowser.application

import android.app.Application
import com.github.harmittaa.moviebrowser.browse.viewModelModule
import com.github.harmittaa.moviebrowser.data.repositoryModule
import com.github.harmittaa.moviebrowser.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MovieBrowserApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@MovieBrowserApplication)
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}
