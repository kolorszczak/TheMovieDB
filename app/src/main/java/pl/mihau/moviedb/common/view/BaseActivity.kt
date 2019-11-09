package pl.mihau.moviedb.common.view

import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import pl.mihau.moviedb.util.application.DialogManager
import pl.mihau.moviedb.util.provider.SchedulerProvider

abstract class BaseActivity : AppCompatActivity() {

    val dialogManager: DialogManager by inject { parametersOf(this) }

    val schedulerProvider: SchedulerProvider by inject()
}
