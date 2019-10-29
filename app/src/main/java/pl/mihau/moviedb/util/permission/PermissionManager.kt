package pl.mihau.moviedb.util.permission

import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import pl.mihau.moviedb.common.Permissions
import pl.mihau.moviedb.common.activity.BaseActivity

class PermissionManager(private val activity: BaseActivity) {

    fun hasRequiredPermissions() = Permissions.requiredPermissions.all {
        RxPermissions(activity).isGranted(it)
    }

    fun requestAllAppPermissions(): Observable<Permission> =
        RxPermissions(activity).requestEachCombined(*Permissions.requiredPermissions)
}
