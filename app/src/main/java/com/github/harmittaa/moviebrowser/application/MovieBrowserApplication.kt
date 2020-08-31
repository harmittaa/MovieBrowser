package com.github.harmittaa.moviebrowser.application

import android.app.Application
import com.github.harmittaa.moviebrowser.browse.viewModelModule
import com.github.harmittaa.moviebrowser.data.uc.storeRepositoryModule
import com.github.harmittaa.moviebrowser.data.uc.useCaseModule
import com.github.harmittaa.moviebrowser.network.networkModule
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalTime::class,
    ExperimentalStdlibApi::class
)
class MovieBrowserApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@MovieBrowserApplication)
            modules(listOf(networkModule, /*repositoryModule,*/ viewModelModule, storeRepositoryModule, useCaseModule))
        }
    }
}
