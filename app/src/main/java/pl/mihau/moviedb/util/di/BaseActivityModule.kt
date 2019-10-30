package pl.mihau.moviedb.util.di

import org.koin.dsl.module
import pl.mihau.moviedb.common.view.BaseActivity
import pl.mihau.moviedb.util.application.ProcessManager
import pl.mihau.moviedb.util.permission.PermissionManager

val baseActivityModule = module {
    factory { (activity: BaseActivity) -> PermissionManager(activity) }
    factory { (activity: BaseActivity) -> ProcessManager(activity) }
}
