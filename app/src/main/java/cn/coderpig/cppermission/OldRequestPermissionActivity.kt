package cn.coderpig.cppermission

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import cn.coderpig.cp.permission.logD
import cn.coderpig.cppermission.databinding.ActivityOldRequestPermissionBinding
import java.lang.StringBuilder

class OldRequestPermissionActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityOldRequestPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_old_request_permission)
    }

    fun requestPermissionClick(v: View) {
        requestPermission(PermissionConstants.permissionList.random())
    }

    // 检查是否已获得权限
    private fun checkPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        return (ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(permission: PermissionWrapper) {
        if (checkPermission(permission.name)) {
            "已授予权限：{$permission}".logD()
        } else {
            "未授予权限：{$permission}, 尝试获取权限...".logD()
            ActivityCompat.requestPermissions(this, arrayOf(permission.name), permission.code)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        StringBuilder().apply {
            append("请求码${requestCode}")
            append(" - 权限：")
            permissions.forEach { append(it) }
            append(" - 授权结果：")
            grantResults.forEach { append(it) }
        }.toString().logD()

    }
}