package pl.mihau.moviedb.util.di

import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import pl.mihau.moviedb.common.Keys
import pl.mihau.moviedb.common.view.BaseActivity
import pl.mihau.moviedb.util.application.DialogManager
import pl.mihau.moviedb.util.application.FavoritesManager

val baseActivityModule = module {
    factory { (activity: BaseActivity) -> DialogManager(activity) }
    single { FavoritesManager(get(), get()) }
    single { androidApplication().getSharedPreferences(Keys.SHARED_PREFS, Context.MODE_PRIVATE) }
}
