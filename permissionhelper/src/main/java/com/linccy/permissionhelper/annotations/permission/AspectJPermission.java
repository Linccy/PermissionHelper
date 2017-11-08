package com.linccy.permissionhelper.annotations.permission;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.linccy.permissionhelper.RequestPermissionActivity;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.ref.WeakReference;

/**
 * 处理切片
 *
 * @author lin.cx 957109587@qq.com
 * @version 3.0
 */
@Aspect
public class AspectJPermission {
  private static final String TAG = "tag";
  private static WeakReference<ProceedingJoinPoint> mCurrentJoinPoint;

  /**
   * 找到处理的切点
   * * *(..)  可以处理CheckLogin这个类所有的方法
   */
  @Pointcut("execution(@m.aicoin.tools.annotations.permission.CheckPermission  * *(..))")
  public void executionAspectJ() {

  }

  /**
   * 处理切面
   *
   * @param joinPoint
   * @return
   */
  @Around("executionAspectJ()")
  public Object aroundAspectJ(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Log.i(TAG, "aroundAspectJ(ProceedingJoinPoint joinPoint)");
    CheckPermission aspectJAnnotation = methodSignature.getMethod().getAnnotation(CheckPermission.class);
    String permission = aspectJAnnotation.value();
    Context context;
    if (PermissionSupport.class.isAssignableFrom(joinPoint.getThis().getClass())) {
      PermissionSupport support = (PermissionSupport) joinPoint.getThis();
      context = support.getContext();
    } else {
      throw new IllegalArgumentException("the annotationed class must implements PermissionSupport! but this is " + joinPoint.getThis().getClass().getName());
    }

    Object o = null;
    String result = "";
    if (PermissionManager.getInnerInstance().checkPermission(context, permission)) {
      o = joinPoint.proceed();
    } else {
      mCurrentJoinPoint = new WeakReference<>(joinPoint);
      Intent intent = new Intent();
      intent.setClass(context, RequestPermissionActivity.class);
      intent.putExtra("permissions", new String[]{permission});
      intent.putExtra("requestcode", 11);
      context.startActivity(intent);
    }
    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    return o;
  }
}  
