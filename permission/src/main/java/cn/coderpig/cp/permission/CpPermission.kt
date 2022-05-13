package cn.coderpig.cp.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Author: zpj
 * Date: 2022-05-09
 * Desc: 权限申请相关扩展
 */

/**
 * 请求单个权限
 *
 * @param onGranted 已授权回调
 * @param onDenied 拒绝授权回调
 * @param onShowRequestRationale 描述请求原因的回调
 * */
fun ActivityResultCaller.registerForPermissionResult(
    onGranted: ((String) -> Unit)? = null,
    onDenied: ((IPermissionScope, String) -> Unit)? = null,
    onShowRequestRationale: ((IPermissionScope, String) -> Unit)? = null,
): ActivityResultLauncher<String> {
    // 初始化可能会用到的Launcher
    val appSettingLauncher = appSettingsLauncher()
    val backLocationLauncher = backLocationLauncher(appSettingLauncher, onGranted)
    val fineLocationLauncher = fineLocationLauncher(appSettingLauncher, backLocationLauncher)
    val installPackageLauncher = installPackageLauncher()
    // 生成权限申请的Launcher
    return registerForActivityResult(RequestPermissionContract()) { result ->
        // 初始化Scope
        val permissionScope = generatorDefaultScope(
            context(),
            appSettingLauncher,
            fineLocationLauncher,
            backLocationLauncher,
            installPackageLauncher
        )
        // 获取权限名
        val permission = result.first
        when {
            // 已授权
            result.second -> onGranted?.let { it -> it(permission) }
            // 提示授权
            permission.isNotEmpty() && ActivityCompat.shouldShowRequestPermissionRationale(
                context(),
                permission
            ) -> onShowRequestRationale?.let { it -> it(permissionScope, permission) }
            // 拒绝授权
            else -> onDenied?.let { it -> it(permissionScope, permission) }
        }
    }
}

/**
 * 请求多个权限
 * */
fun ActivityResultCaller.registerForPermissionsResult(
    onAllGranted: ((List<String>) -> Unit)? = null,
    onDenied: ((IPermissionScope, List<String>) -> Unit)? = null,
    onShowRequestRationale: ((IPermissionScope, List<String>) -> Unit)? = null
): ActivityResultLauncher<Array<String>> {
    // 初始化要用到的Launcher
    val appSettingLauncher = appSettingsLauncher()
    val backLocationLauncher = backLocationLauncher(appSettingLauncher) { s -> }
    val fineLocationLauncher = fineLocationLauncher(appSettingLauncher, backLocationLauncher)
    val installPackageLauncher = installPackageLauncher()
    return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->
        if (resultMap.containsValue(false)) {
            val context =
                if (this is ComponentActivity) this else (this as Fragment).requireActivity()
            val permissionScope =
                generatorDefaultScope(
                    context,
                    appSettingLauncher,
                    fineLocationLauncher,
                    backLocationLauncher,
                    installPackageLauncher
                )
            // 获得未授权权限列表、第一次拒绝列表
            val deniedList = mutableListOf<String>()
            for (entry in resultMap.entries) if (!entry.value) deniedList.add(entry.key)
            val explainableList = deniedList.filter {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    it
                )
            }
            if (explainableList.isNotEmpty()) {
                // 不为空回调提示的方法，并回传了一个List，可以按需处理，如发起第二次授权请求
                onShowRequestRationale?.let { it -> it(permissionScope, explainableList) }
            } else {
                // 回调拒绝权限的方法，同样回传未授权的List，可以做灵活判断
                onDenied?.let { it -> it(permissionScope, deniedList) }
            }
        } else {
            // 没有false说明授权通过
            onAllGranted?.let { it -> it(resultMap.keys.toList()) }
        }
    }
}

/**
 * 跳转应用设置页的Launcher
 * */
fun ActivityResultCaller.appSettingsLauncher() =
    registerForActivityResult(LaunchAppSettingsContract()) {}

/**
 * 获取普通定位权限的Launcher
 * */
fun ActivityResultCaller.fineLocationLauncher(
    appSettingLauncher: ActivityResultLauncher<Unit>,
    backLocationLauncher: ActivityResultLauncher<String>
) = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
    if (it) {
        "Request Permission ACCESS_FINE_LOCATION Success".logD()
        "Try request Permission ACCESS_BACKGROUND_LOCATION".logD()
        backLocationLauncher.launch(ACCESS_BACKGROUND_LOCATION)
    } else {
        "Use refuse Permission ACCESS_FINE_LOCATION".logD()
        context().genDefaultDialog(
            "提示信息",
            "当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。",
            confirm = { appSettingLauncher.launch(null) }).show()
    }
}

/**
 * 获取后台定位权限的Launcher
 * */
fun ActivityResultCaller.backLocationLauncher(
    appSettingLauncher: ActivityResultLauncher<Unit>,
    callback: ((String) -> Unit)? = null
) = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
    if (it) {
        "Request Permission ACCESS_BACKGROUND_LOCATION Success".logD()
        callback?.let { it(ACCESS_BACKGROUND_LOCATION) }
    } else {
        "Use refuse Permission ACCESS_BACKGROUND_LOCATION".logD()
        context().genDefaultDialog(
            "提示信息",
            "当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。",
            confirm = { appSettingLauncher.launch(null) }).show()
    }
}

fun ActivityResultCaller.installPackageLauncher() =
    registerForActivityResult(LaunchInstallPackageContract()) {}



/**
 * 单个权限的协定，传入权限字符串，返回Pair<权限，授权结果>
 * */
class RequestPermissionContract : ActivityResultContract<String, Pair<String, Boolean>>() {
    private lateinit var mPermission: String

    override fun createIntent(context: Context, input: String): Intent {
        mPermission = input
        return Intent(ActivityResultContracts.RequestMultiplePermissions.ACTION_REQUEST_PERMISSIONS).putExtra(
            ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSIONS, arrayOf(input)
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<String, Boolean> {
        if (intent == null || resultCode != Activity.RESULT_OK) return mPermission to false
        val grantResults =
            intent.getIntArrayExtra(ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSION_GRANT_RESULTS)
        return mPermission to
            if (grantResults == null || grantResults.isEmpty()) false
            else grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    override fun getSynchronousResult(
        context: Context,
        input: String?
    ): SynchronousResult<Pair<String, Boolean>>? =
        when {
            null == input -> SynchronousResult("" to false)
            ContextCompat.checkSelfPermission(
                context,
                input
            ) == PackageManager.PERMISSION_GRANTED -> {
                SynchronousResult(input to true)
            }
            else -> null
        }
}

/**
 * 跳转设置页的协定
 * */
class LaunchAppSettingsContract : ActivityResultContract<Unit, Unit>() {
    override fun createIntent(context: Context, input: Unit?) =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", context.packageName, null))

    override fun parseResult(resultCode: Int, intent: Intent?) = Unit
}

/**
 * 跳转安装未知应用页的协定
 * */
class LaunchInstallPackageContract : ActivityResultContract<Unit, Unit>() {
    override fun createIntent(context: Context, input: Unit?): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                .setData(Uri.fromParts("package", context.packageName, null))
        } else {
            Intent()
        }
    }

        override fun parseResult(resultCode: Int, intent: Intent?) = Unit
    }


    /**
     * 获取上下文
     * */
    fun ActivityResultCaller.context() =
        if (this is ComponentActivity) this else (this as Fragment).requireActivity()

    /**
     * 特殊权限过滤
     * */
    fun ActivityResultLauncher<String>.launchX(permissionStr: String) {
        "Request Permission：${permissionStr}".logD()
        // 针对后台权限的特殊处理
        if (permissionStr == ACCESS_BACKGROUND_LOCATION) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                launch(ACCESS_FINE_LOCATION)
                return
            }
        } else if(permissionStr == REQUEST_INSTALL_PACKAGES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                launch(REQUEST_INSTALL_PACKAGES)
                return
            }
        }
        launch(permissionStr)

    }

    /**
     * 特殊权限过滤
     * */
    fun ActivityResultLauncher<Array<String>>.launchX(permissionsArray: Array<String>) {
        "Request Permissions：${permissionsArray}".logD()
        // 转化为集合去重
        val permissionsSet = permissionsArray.toMutableSet()
        // 针对后台权限的特殊处理
        if (permissionsSet.contains(ACCESS_BACKGROUND_LOCATION)) {
            // Android 10下直接移除这个权限，添加普通定位权限
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                permissionsSet.remove(ACCESS_BACKGROUND_LOCATION)
                permissionsSet.add(ACCESS_FINE_LOCATION)
            }
        }
        launch(permissionsSet.toTypedArray())
    }

    /**
     * 生成默认权限后续处理
     * */
    fun generatorDefaultScope(
        context: Context,
        appSettingLauncher: ActivityResultLauncher<Unit>,
        fineLocationLauncher: ActivityResultLauncher<String>,
        backLocationLauncher: ActivityResultLauncher<String>,
        installPackageLauncher: ActivityResultLauncher<Unit>,
    ) = object : IPermissionScope {

        override fun appSetting() {
            context.genDefaultDialog(
                "提示信息",
                "当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。",
                confirm = { appSettingLauncher.launch(null) }).show()
        }

        override fun backLocation() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val selfPermission =
                    ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
                when {
                    selfPermission == PackageManager.PERMISSION_GRANTED -> backLocationLauncher.launch(
                        ACCESS_BACKGROUND_LOCATION
                    )
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        ACCESS_FINE_LOCATION
                    ) -> {
                        fineLocationLauncher.launch(ACCESS_FINE_LOCATION)
                    }
                    else -> fineLocationLauncher.launch(ACCESS_FINE_LOCATION)
                }
            }
        }

        override fun installPackage(callback: () -> Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (!context.packageManager.canRequestPackageInstalls()) {
                    installPackageLauncher.launch(null)
                } else {
                    callback()
                }
            }
        }
    }

    /**
     * 申请权限相关的后续操作接口
     * */
    interface IPermissionScope {
        fun appSetting()
        fun backLocation()
        fun installPackage(callback: () -> Unit)
    }

