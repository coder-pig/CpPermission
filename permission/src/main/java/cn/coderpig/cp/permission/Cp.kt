package cn.coderpig.cp.permission

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


/**
 * Author: zpj
 * Date: 2022-05-09
 * Desc:
 */
const val TAG = "CpPermission"

fun Context.shortToast(msg: String) =
    Toast.makeText(this.applicationContext, msg, Toast.LENGTH_SHORT).show()

fun String.logD() {
    if (BuildConfig.DEBUG) Log.d(TAG, this)
}

fun Context.genDefaultDialog(
    title: String? = null,
    message: String? = null,
    confirm: (() -> Unit)? = null,
    cancel: (() -> Unit)? = null,
): AlertDialog = AlertDialog.Builder(this).apply {
    title?.let { setTitle(it) }
    message?.let { setMessage(it) }
    cancel?.let { setNegativeButton("取消") { _, _ -> it() } }
    confirm?.let { setNegativeButton("确定") { _, _ -> it() } }
}.create()