package com.linccy.permissionhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

/**
 * 通过透明的activity实现注解的请求和回调
 * intent key :"permissions", "requestcode"
 * @author lin.cx 957109587@qq.com
 * @version 3.0
 */


public class RequestPermissionActivity extends Activity {

  private String[] permissions;
  private int requestCode;
  private long requestPermissionStarTime;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent params = getIntent();
    permissions = params.getStringArrayExtra("permissions");
    requestCode = params.getIntExtra("requestcode", 0);
    setContentView(R.layout.act_request_permission);
    if (permissions != null && permissions.length > 0) {
      requestPermission(permissions);
    }
  }

  private void requestPermission(String[] permissions) {
    List<String> unAcceptPermission = new ArrayList<>();
    requestPermissionStarTime = System.currentTimeMillis();
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
        unAcceptPermission.add(permission);
      }
    }
    if (unAcceptPermission.size() == 0) {
      requestPermissionSuccess();
      return;
    }

    ActivityCompat.requestPermissions(this, unAcceptPermission.toArray(new String[]{}), requestCode);
  }

  private void requestPermissionSuccess() {
    setResult(RESULT_OK);
    finish();
  }

  private void requestPermissionFailed() {
    setResult(RESULT_CANCELED);
    finish();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == this.requestCode) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        requestPermissionSuccess();
      } else {
        long refuseSpendTime = System.currentTimeMillis() - requestPermissionStarTime;
        boolean alwaysHidePermission = false;
        for (int i = 0; i < grantResults.length; i++) {
          if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
            //判断是否勾选禁止后不再询问
            boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
            if (!showRequestPermission) {
              alwaysHidePermission = true;
            }
          }
        }
        Log.d("refuseSpendTime：", refuseSpendTime + ", " + alwaysHidePermission
        );
        if (alwaysHidePermission || refuseSpendTime < 500) {//由于勾选不在提示权限请求后，会马上被拒绝，所以可以根据被拒绝的时间来判断勾选
          Toast.makeText(this, "您可能已勾选不在提示权限请求，请前往管理页面开启权限后再试！", Toast.LENGTH_LONG).show();
        }
        requestPermissionFailed();
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
