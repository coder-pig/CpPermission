package cn.coderpig.cppermission

import androidx.appcompat.app.AppCompatActivity
import cn.coderpig.cp.permission.registerForPermissionResult
import cn.coderpig.cp.permission.registerForPermissionsResult

/**
 * Author: zpj
 * Date: 2022-05-13
 * Desc:
 */
abstract class BaseActivity: AppCompatActivity() {
    // 权限处理
    protected val mPermissionLauncher = registerForPermissionResult(
        onGranted = { permission -> permissionGranted(permission) },
        onDenied = { scope, permission -> permissionDenied(permission); scope.appSetting() },
        onShowRequestRationale = { scope, permission -> permissionTip(permission); scope.appSetting() }
    )

    protected val mPermissionsLauncher = registerForPermissionsResult(
        onAllGranted = { permissions -> permissionsGranted(permissions) },
        onDenied = { scope, permissions -> permissionsDenied(permissions); scope.appSetting() },
        onShowRequestRationale = { scope, permissions -> permissionsTip(permissions); scope.appSetting() }
    )

    // 权限相关回调，可按需实现
    protected open fun permissionGranted(permission: String) {}
    protected open fun permissionDenied(permission: String) {}
    protected open fun permissionTip(permission: String) {}
    protected open fun permissionsGranted(permissions: List<String>) {}
    protected open fun permissionsDenied(permissions: List<String>) {}
    protected open fun permissionsTip(permissions: List<String>) {}
}