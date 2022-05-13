package cn.coderpig.cppermission

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.coderpig.cp.permission.*
import cn.coderpig.cppermission.databinding.ActivityTestCpPermissionBinding

/**
 * Author: zpj
 * Date: 2022-05-09
 * Desc:
 */
class TestCpPermissionActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityTestCpPermissionBinding
    // 注册Launcher
    private val mPermissionLauncher = registerForPermissionResult(
        onGranted = { shortToast("权限已授予") },
        onDenied = { scope, permission ->
            shortToast("$permission 权限未授予");
            when(permission) {
                ACCESS_BACKGROUND_LOCATION -> scope.backLocation()
                REQUEST_INSTALL_PACKAGES -> scope.installPackage() { shortToast("已获得安装包权限") }
                else -> scope.appSetting()
            }
        },
        onShowRequestRationale = { scope, permission ->
            shortToast("$permission 需要授权提示");
        }
    )
    private val mPermissionsLauncher = registerForPermissionsResult(
        onAllGranted = { shortToast("所有权限已授予") },
        onDenied = { scope, permissions ->
            shortToast("$permissions 权限未授予")
            when {
                permissions.contains(ACCESS_BACKGROUND_LOCATION) -> scope.backLocation()
                permissions.contains(REQUEST_INSTALL_PACKAGES) -> scope.installPackage() { shortToast("已获得安装包权限") }
                else -> scope.appSetting()

            }
        },
        onShowRequestRationale = { scope, permissions ->
            shortToast("$permissions 需要授权提示");
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_cp_permission)
    }


    fun click(v: View) {
        when (v.id) {
            R.id.bt_request_permission -> mPermissionLauncher.launchX(PermissionConstants.permissionList.random().name)
            R.id.bt_request_permissions -> mPermissionsLauncher.launchX(
                arrayOf(
                    PermissionConstants.permissionList.random().name,
                    PermissionConstants.permissionList.random().name,
                    PermissionConstants.permissionList.random().name,
                )
            )
            R.id.bt_request_back_location_permission -> mPermissionLauncher.launchX(ACCESS_BACKGROUND_LOCATION)
            R.id.bt_request_install_package_permission -> mPermissionLauncher.launchX(REQUEST_INSTALL_PACKAGES)
        }
    }


}
