package cn.coderpig.cppermission

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import cn.coderpig.cp.permission.logD
import cn.coderpig.cppermission.databinding.ActivityNewRequestPermissionBinding

class NewRequestPermissionActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityNewRequestPermissionBinding

    private val mPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                "已授予权限".logD()
            } else {
                "未授予权限".logD()
            }
        }

    private val mPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            it.entries.forEach { entry ->
                "权限：${entry.key} - 申请结果：${entry.value}".logD()
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_request_permission)
    }

    fun requestPermissionClick(v: View) {
        requestPermission(PermissionConstants.permissionList.random().name)
    }

    private fun requestPermission(permission: String) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, permission) -> {
                "已授予权限：{$permission}".logD()
            }
            else -> {
                "未授予权限：{$permission}, 尝试获取权限...".logD()
                mPermissionLauncher.launch(permission)
            }
        }
    }

    fun requestPermissionsClick(v: View) {
        requestPermissions(
            arrayOf(
                PermissionConstants.permissionList.random().name,
                PermissionConstants.permissionList.random().name,
                PermissionConstants.permissionList.random().name
            )
        )
    }

    private fun requestPermissions(permissions: Array<String>) {
        permissions.forEach { "查询权限：${it}".logD() }
        // 遍历权限列表中是否有为授权的权限
        val deniedIndex = permissions.indexOfFirst {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_DENIED
        }
        if (deniedIndex == -1) {
            "所有权限已授予".logD()
        } else {
            "存在权限未授予".logD()
            // 从未授权的下标开始
            mPermissionsLauncher.launch(permissions.copyOfRange(deniedIndex, permissions.size))
        }
    }

}


