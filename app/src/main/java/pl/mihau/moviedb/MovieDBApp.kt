package pl.mihau.moviedb

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.mihau.moviedb.util.di.*
import pl.mihau.moviedb.util.lifecycle.ActivityLifecycleManager
import timber.log.Timber

class MovieDBApp : Application() {

    private val activityLifecycleManager by inject<ActivityLifecycleManager>()

    override fun onCreate() {
        super.onCreate()
        initializeLogger()
        initializeDI()
        registerActivityLifecycleCallbacks(activityLifecycleManager)
    }

    private fun initializeLogger() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initializeDI() {
        startKoin {
            androidContext(this@MovieDBApp)
            modules(
                baseActivityModule,
                viewModelModule,
                repositoryModule,
                rxModule,
                restModule,
                storageModule,
                applicationUtilsModule
            )
        }
    }
}