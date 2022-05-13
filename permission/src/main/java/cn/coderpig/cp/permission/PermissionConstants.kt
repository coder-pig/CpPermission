package cn.coderpig.cp.permission

/**
 * Author: zpj
 * Date: 2022-05-10
 * Desc: 权限相关常量
 */

/**
 * 在后台获取位置（需要 Android 10.0 及以上）
 *
 * 注意事项：
 * 1. 一旦你申请了该权限，在授权的时候，需要选择 "始终允许"，而不能选择 "仅在使用中允许"
 * 2. 如果你的 App 只在前台状态下使用定位功能，请不要申请该权限（后台定位权限）
 * */
const val ACCESS_FINE_LOCATION  = "android.permission.ACCESS_FINE_LOCATION"
const val ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION"


/**
 * 申请安装包权限 (需要Android 8及以上)，跳转允许来自此来源的应用页
 * */
const val REQUEST_INSTALL_PACKAGES = "android.permission.REQUEST_INSTALL_PACKAGES"
