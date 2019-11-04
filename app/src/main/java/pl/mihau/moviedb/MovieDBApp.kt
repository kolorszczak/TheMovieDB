package pl.mihau.moviedb

import android.app.Application
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.mihau.moviedb.util.di.*
import pl.mihau.moviedb.util.lifecycle.ActivityLifecycleManager
import timber.log.Timber

class MovieDBApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeLogger()
        initializeDI()
        registerActivityLifecycleCallbacks(get<ActivityLifecycleManager>())
    }

    private fun initializeLogger() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initializeDI() {
        startKoin {
            androidContext(this@MovieDBApp)
            modules(modules)
        }
    }

    companion object {
        val modules = listOf(
            baseActivityModule,
            viewModelModule,
            repositoryModule,
            restModule,
            applicationUtilsModule
        )
    }
}