package com.github.harmittaa.moviebrowser.application

import android.app.Application
import com.github.harmittaa.moviebrowser.browse.di.viewModelModule
import com.github.harmittaa.moviebrowser.data.di.storeRepositoryModule
import com.github.harmittaa.moviebrowser.data.di.useCaseModule
import com.github.harmittaa.moviebrowser.db.di.databaseModule
import com.github.harmittaa.moviebrowser.epoxy.di.epoxyModule
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
            modules(
                listOf(
                    networkModule,
                    viewModelModule,
                    storeRepositoryModule,
                    useCaseModule,
                    databaseModule,
                    epoxyModule
                )
            )
        }
    }
}
