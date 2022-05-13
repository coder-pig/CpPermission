package cn.coderpig.cppermission

import android.Manifest
import java.util.concurrent.atomic.AtomicInteger

/**
 * Author: zpj
 * Date: 2022-05-09
 * Desc: 权限常量类
 */
object PermissionConstants {
    private val mPermissionCode = AtomicInteger()

    val permissionList = arrayListOf(
        PermissionWrapper(Manifest.permission.INTERNET, mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.ACCESS_BACKGROUND_LOCATION,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.READ_CALENDAR,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.WRITE_CALENDAR,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.READ_CALL_LOG,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.WRITE_CALL_LOG,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.PROCESS_OUTGOING_CALLS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.CAMERA,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.READ_CONTACTS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.WRITE_CONTACTS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.GET_ACCOUNTS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.ACCESS_FINE_LOCATION,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.ACCESS_COARSE_LOCATION,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.RECORD_AUDIO,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.READ_PHONE_STATE,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.READ_PHONE_NUMBERS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.CALL_PHONE,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.ANSWER_PHONE_CALLS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.ADD_VOICEMAIL,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.USE_SIP,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.BODY_SENSORS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.SEND_SMS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.RECEIVE_SMS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.READ_SMS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.RECEIVE_WAP_PUSH,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.RECEIVE_MMS,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.READ_EXTERNAL_STORAGE,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.WRITE_EXTERNAL_STORAGE,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.SYSTEM_ALERT_WINDOW,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.REQUEST_INSTALL_PACKAGES,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.BLUETOOTH_SCAN,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.BLUETOOTH_ADVERTISE,mPermissionCode.getAndIncrement()),
        PermissionWrapper(Manifest.permission.BLUETOOTH_CONNECT,mPermissionCode.getAndIncrement()),
    )
}

data class PermissionWrapper(val name: String, val code: Int)