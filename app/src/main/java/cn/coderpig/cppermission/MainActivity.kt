package cn.coderpig.cppermission

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.coderpig.cp.permission.logD
import cn.coderpig.cppermission.databinding.ActivityMainBinding

/**
 * Author: zpj
 * Date: 2022-05-09
 * Desc:
 */
class MainActivity: AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.clickListener = ClickHandler()
    }
}

class ClickHandler {
    fun showOldRequestPermissionAct(v: View) {
        v.context.let { it.startActivity(Intent(it, OldRequestPermissionActivity::class.java)) }
    }

    fun showNewRequestPermissionAct(v: View) {
        v.context.let { it.startActivity(Intent(it, NewRequestPermissionActivity::class.java)) }
    }

    fun showCpTestPermissionAct(v: View) {
        v.context.let { it.startActivity(Intent(it, TestCpPermissionActivity::class.java)) }
    }

    fun showCpTestBaseAct(v: View) {
        v.context.let { it.startActivity(Intent(it, TestBaseActivity::class.java)) }
    }
}
