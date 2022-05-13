package cn.coderpig.cppermission;

import static cn.coderpig.cp.permission.PermissionConstantsKt.ACCESS_BACKGROUND_LOCATION;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.coderpig.cp.permission.CpPermissionKt;
import cn.coderpig.cp.permission.IPermissionScope;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

/**
 * Author: zpj
 * Date: 2022-05-11
 * Desc:
 */
public class JavaTestActivity extends AppCompatActivity {
    ActivityResultLauncher<String> launcher = CpPermissionKt.registerForPermissionResult(
            this,
            () -> {
                Log.e("Test", "授权成功");
                return null;
            }, (scope, permission) -> {
                Log.e("Test", "拒绝授权，跳转设置");
                scope.appSetting();
                return null;
            }, null);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher.launch(ACCESS_BACKGROUND_LOCATION);
    }
}
