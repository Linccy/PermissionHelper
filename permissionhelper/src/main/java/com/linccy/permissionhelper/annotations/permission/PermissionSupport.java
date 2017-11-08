package com.linccy.permissionhelper.annotations.permission;

import android.content.Context;

/**
 * 使用@Permission 注解的类必须实现PermissionSupport接口
 * @author lin.cx 957109587@qq.com
 * @version 3.0
 */

public interface PermissionSupport {
  Context getContext();
}
