package com.linccy.permissionhelper.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * 检查权限
 * @author lin.cx 957109587@qq.com
 * @version 3.0
 */
public class PermissionManager {
  private static volatile PermissionManager permissionManager;

  public PermissionManager() {
  }

  //DCL单例模式
  public static PermissionManager getInstance() {
    if (permissionManager == null) {
      synchronized (PermissionManager.class) {
        if (permissionManager == null) {
          permissionManager = new PermissionManager();
        }
      }
    }
    return permissionManager;
  }

  private static class InnerInsatance {
    public static final PermissionManager instance = new PermissionManager();
  }

  //内部类单例模式
  public static PermissionManager getInnerInstance() {
    synchronized (PermissionManager.class) {
      return InnerInsatance.instance;
    }
  }

  public boolean checkPermission(Context context, String permission) {
    Log.i("tag", "检查的权限：" + permission);
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
      return true;
    }
    return false;
  }

  public boolean checkPermissions(Context context, String[] permissions) {
    for(String permission : permissions) {
      if (!checkPermission(context, permission)) {
        return false;
      }
    }
    return true;
  }
}
