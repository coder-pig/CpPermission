package cn.coderpig.cppermission

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import cn.coderpig.cp.permission.ACCESS_BACKGROUND_LOCATION
import cn.coderpig.cp.permission.REQUEST_INSTALL_PACKAGES
import cn.coderpig.cp.permission.launchX
import cn.coderpig.cp.permission.logD
import cn.coderpig.cppermission.databinding.ActivityTestBaseBinding

/**
 * Author: zpj
 * Date: 2022-05-13
 * Desc:
 */
class TestBaseActivity: BaseActivity() {
    private lateinit var mBinding: ActivityTestBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_base)
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
            R.id.bt_request_back_location_permission -> mPermissionLauncher.launchX(
                ACCESS_BACKGROUND_LOCATION
            )
            R.id.bt_request_install_package_permission -> mPermissionLauncher.launchX(
                REQUEST_INSTALL_PACKAGES
            )
        }
    }

    override fun permissionGranted(permission: String) {
        "获得授权：${permission}".logD()
    }

    override fun permissionDenied(permission: String) {
        "拒绝授权：${permission}".logD()
    }

    override fun permissionTip(permission: String) {
        "授权提示：${permission}".logD()
    }

    override fun permissionsGranted(permissions: List<String>) {
        "获得授权：${permissions}".logD()
    }

    override fun permissionsDenied(permissions: List<String>) {
        "拒绝授权：${permissions}".logD()
    }

    override fun permissionsTip(permissions: List<String>) {
        "授权提示：${permissions}".logD()
    }
}