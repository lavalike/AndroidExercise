package com.android.exercise.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * 动态权限申请工具类
 * Created by wangzhen on 16/7/21.
 */
public class PermissionManager {
    /**
     * Activity请求相应权限
     *
     * @param permission
     * @return 是否请求成功
     */
    public static <T extends AppCompatActivity> void requestPermission(T object, String permission, OnPermissionCallback callback) {
        if (object instanceof OnBaseCallback) {
            ((OnBaseCallback) object).setPermissionCallback(callback);
        }
        // 6.0版本以上
        if (Build.VERSION.SDK_INT >= 23) {
            //判断权限是否被授予
            int permissionStatus = ActivityCompat.checkSelfPermission(object, permission);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                object.requestPermissions(new String[]{permission}, 1);
            } else {
                callback.onGranted();
            }

        } else {
            callback.onGranted();
        }
    }

    /**
     * Fragment请求相应权限
     *
     * @param permission
     * @return 是否请求成功
     */
    public static <T extends Fragment> void requestPermission(T object, String permission, OnPermissionCallback callback) {
        if (object instanceof OnBaseCallback) {
            ((OnBaseCallback) object).setPermissionCallback(callback);
        }

        // 6.0版本以上
        if (Build.VERSION.SDK_INT >= 23) {
            //判断权限是否被授予
            int permissionStatus = ActivityCompat.checkSelfPermission(object.getActivity(), permission);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                object.requestPermissions(new String[]{permission}, 1);
            } else {
                callback.onGranted();
            }

        } else {
            callback.onGranted();
        }
    }

    public static void showAdvice(Context context) {
        showAdvice(context, "需要开启权限才能使用此功能");
    }

    public static void showAdvice(final Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentSetting = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intentSetting.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intentSetting);
            }
        });
        builder.show();
    }

    public interface OnPermissionCallback {
        void onGranted();

        void onDenied();
    }

    public interface OnBaseCallback {
        void setPermissionCallback(OnPermissionCallback callback);
    }
}
